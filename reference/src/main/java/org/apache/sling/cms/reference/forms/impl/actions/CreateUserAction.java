/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sling.cms.reference.forms.impl.actions;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.jackrabbit.oak.spi.security.principal.PrincipalImpl;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.cms.reference.forms.FormAction;
import org.apache.sling.cms.reference.forms.FormActionResult;
import org.apache.sling.cms.reference.forms.FormConstants;
import org.apache.sling.cms.reference.forms.FormException;
import org.apache.sling.cms.reference.forms.FormRequest;
import org.apache.sling.cms.reference.forms.FormUtils;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = { FormAction.class })
@Designate(ocd = CreateUserAction.Config.class)
public class CreateUserAction implements FormAction {

    private static final Logger log = LoggerFactory.getLogger(CreateUserAction.class);
    public static final String DEFAULT_RESOURCE_TYPE = "reference/components/forms/actions/createuser";
    public static final String PROFILE_PROPERTIES = "profileProperties";
    public static final String GROUPS = "groups";

    private final ResourceResolverFactory factory;
    private final Config config;

    @Activate
    public CreateUserAction(@Reference ResourceResolverFactory factory, Config config) {
        this.factory = factory;
        this.config = config;
    }

    @Override
    public FormActionResult handleForm(final Resource actionResource, final FormRequest request) throws FormException {
        final StringSubstitutor sub = new StringSubstitutor(request.getFormData());

        final ValueMap properties = actionResource.getValueMap();

        String username = request.getFormData().get("username", String.class);
        String password = request.getFormData().get(FormConstants.PN_PASSWORD, String.class);

        String intermediatePath = properties.get("intermediatePath", String.class);

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return FormActionResult.failure("Empty username / password");
        }

        try {
            try (ResourceResolver adminResolver = factory.getServiceResourceResolver(
                    Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, FormConstants.SERVICE_USER))) {
                JackrabbitSession session = (JackrabbitSession) adminResolver.adaptTo(Session.class);
                final UserManager userManager = session.getUserManager();

                if (userManager.getAuthorizable(new PrincipalImpl(username)) == null) {

                    log.debug("Creating user {}", username);
                    User user = userManager.createUser(username, password, new PrincipalImpl(username),
                            intermediatePath);

                    String[] groups = properties.get(GROUPS, new String[0]);
                    for (String g : groups) {
                        String groupName = sub.replace(g);
                        Authorizable group = userManager.getAuthorizable(new PrincipalImpl(groupName));
                        if (group == null || !group.isGroup()) {
                            log.error("Could not find group {}", groupName);
                            return FormActionResult.failure("Could not find group: " + groupName);
                        } else {
                            ((Group) group).addMember(user);
                        }

                    }
                    log.debug("Updating profile for {}", username);
                    updateProfile(adminResolver, user, properties.get(PROFILE_PROPERTIES, new String[0]),
                            request.getFormData());

                    log.debug("Saving changes!");
                    adminResolver.commit();
                    return FormActionResult.success("User " + username + " created successfully");
                } else {
                    log.error("Failed to create user, {} already exists", username);
                    return FormActionResult.failure("User " + username + " already exists");
                }
            }
        } catch (LoginException le) {
            log.error("Failed to get user manager service user", le);
            return FormActionResult.failure("Failed to get service user");
        } catch (PersistenceException | RepositoryException e) {
            log.error("Failed to create user " + username, e);
            return FormActionResult.failure("Failed to create user " + username);
        }
    }

    private void updateProfile(ResourceResolver adminResolver, User user, String @NotNull [] toset, ValueMap formData)
            throws PersistenceException, RepositoryException {
        if (toset.length > 0) {
            Map<String, Object> properties = new HashMap<>();
            Arrays.stream(toset).filter(k -> formData.keySet().contains(k))
                    .forEach(k -> properties.put(k, formData.get(k)));
            properties.put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
            ResourceUtil.getOrCreateResource(adminResolver, user.getPath() + "/profile", properties,
                    JcrConstants.NT_UNSTRUCTURED, false);
        }

    }

    @Override
    public boolean handles(Resource actionResource) {
        return FormUtils.handles(config.supportedTypes(), actionResource);
    }

    @ObjectClassDefinition(name = "%cms.reference.createuser.name", description = "%cms.reference.createuser.description", localization = "OSGI-INF/l10n/bundle")
    public @interface Config {

        @AttributeDefinition(name = "%cms.reference.supportedTypes.name", description = "%cms.reference.supportedTypes.description", defaultValue = {
                DEFAULT_RESOURCE_TYPE })
        String[] supportedTypes() default { DEFAULT_RESOURCE_TYPE };
    }

}
