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

import java.util.Calendar;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Stream;

import javax.jcr.Session;
import javax.jcr.ValueFactory;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.cms.reference.forms.FormAction;
import org.apache.sling.cms.reference.forms.FormActionResult;
import org.apache.sling.cms.reference.forms.FormConstants;
import org.apache.sling.cms.reference.forms.FormException;
import org.apache.sling.cms.reference.forms.FormRequest;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = { FormAction.class })
@Designate(ocd = RequestPasswordResetAction.Config.class)
public class RequestPasswordResetAction implements FormAction {

    public static final String DEFAULT_RESOURCE_TYPE = "reference/components/forms/actions/requestpasswordreset";
    public static final String PN_RESETTOKEN = "resettoken";
    public static final String PN_RESETTIMEOUT = "resettimeout";
    private static final Logger log = LoggerFactory.getLogger(RequestPasswordResetAction.class);
    private ResourceResolverFactory factory;
    private Config config;

    @Activate
    public RequestPasswordResetAction(@Reference ResourceResolverFactory factory, Config config) {
        this.factory = factory;
        this.config = config;
    }

    @Override
    public FormActionResult handleForm(Resource actionResource, FormRequest request) throws FormException {
        String email = request.getFormData().get(FormConstants.PN_EMAIL, String.class);
        int resetTimeout = actionResource.getValueMap().get(PN_RESETTIMEOUT, Integer.class);

        try (ResourceResolver adminResolver = factory.getServiceResourceResolver(
                Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, FormConstants.SERVICE_USER))) {

            JackrabbitSession session = (JackrabbitSession) adminResolver.adaptTo(Session.class);
            final UserManager userManager = session.getUserManager();

            if (userManager.getAuthorizable(email) != null) {

                User user = (User) userManager.getAuthorizable(email);

                String resetToken = UUID.randomUUID().toString();
                Calendar deadline = Calendar.getInstance();
                deadline.add(Calendar.SECOND, resetTimeout);

                ValueFactory vf = session.getValueFactory();

                user.setProperty(PN_RESETTOKEN, vf.createValue(resetToken));
                user.setProperty(PN_RESETTIMEOUT, vf.createValue(deadline));

                request.getFormData().put(PN_RESETTOKEN, resetToken);

                adminResolver.commit();

            } else {
                log.warn("Unable to find user {}", email);
                return FormActionResult.failure("Unable to find user");
            }
        } catch (Exception e) {
            throw new FormException("Failed to initiate password reset", e);
        }
        return FormActionResult.success("Reset token created");
    }

    @Override
    public boolean handles(Resource actionResource) {
        return Stream.of(config.supportedTypes()).anyMatch(t -> t.equals(actionResource.getResourceType()));
    }

    @ObjectClassDefinition(name = "%cms.reference.requestpasswordreset.name", description = "%cms.reference.requestpasswordreset.description", localization = "OSGI-INF/l10n/bundle")
    public @interface Config {

        @AttributeDefinition(name = "%cms.reference.supportedTypes.name", description = "%cms.reference.supportedTypes.description", defaultValue = {
                DEFAULT_RESOURCE_TYPE })
        String[] supportedTypes() default { DEFAULT_RESOURCE_TYPE };
    }

}