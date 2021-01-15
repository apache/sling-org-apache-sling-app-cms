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

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

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
import org.apache.sling.cms.reference.forms.FormUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = { FormAction.class })
@Designate(ocd = ResetPasswordAction.Config.class)
public class ResetPasswordAction implements FormAction {

    public static final String DEFAULT_RESOURCE_TYPE = "reference/components/forms/actions/resetpassword";
    private static final Logger log = LoggerFactory.getLogger(ResetPasswordAction.class);
    private ResourceResolverFactory factory;
    private Config config;

    @Activate
    public ResetPasswordAction(@Reference ResourceResolverFactory factory, Config config) {
        this.factory = factory;
        this.config = config;
    }

    @Override
    public FormActionResult handleForm(Resource actionResource, FormRequest request) throws FormException {
        String email = request.getFormData().get(FormConstants.PN_EMAIL, String.class);
        String resetToken = request.getFormData().get(FormConstants.PN_RESETTOKEN, String.class);
        String password = request.getFormData().get("password", String.class);

        try (ResourceResolver adminResolver = factory.getServiceResourceResolver(
                Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, FormConstants.SERVICE_USER))) {

            JackrabbitSession session = (JackrabbitSession) adminResolver.adaptTo(Session.class);
            final UserManager userManager = session.getUserManager();

            User user = (User) userManager.getAuthorizable(email);

            if (user == null) {
                return FormActionResult.failure("No user found for " + email);
            }

            String storedToken = getValue(user.getProperty(FormConstants.PN_RESETTOKEN), String.class);
            Calendar resetTimeout = getValue(user.getProperty(FormConstants.PN_RESETTIMEOUT), Calendar.class);
            if (storedToken == null || !storedToken.equals(resetToken)) {
                return FormActionResult.failure("Failed to validate token");
            }
            if (Calendar.getInstance().after(resetTimeout)) {
                return FormActionResult.failure("Timeout already passed");
            }
            user.changePassword(password);

            log.debug("Saving changes!");
            adminResolver.commit();

            return FormActionResult.success("Password reset successfully!");
        } catch (Exception e) {
            throw new FormException("Failed to complete password reset", e);
        }
    }

    private <E> E getValue(Value[] property, Class<E> clazz) throws IllegalStateException, RepositoryException {
        if (property != null && property.length > 0) {
            Value v = property[0];
            if (clazz.isAssignableFrom(String.class)) {
                return clazz.cast(v.getString());
            }
            if (clazz.isAssignableFrom(Calendar.class)) {
                return clazz.cast(v.getDate());
            }
        }
        return null;
    }

    @Override
    public boolean handles(Resource actionResource) {
        return FormUtils.handles(config.supportedTypes(), actionResource);
    }

    @ObjectClassDefinition(name = "%cms.reference.resetpassword.name", description = "%cms.reference.resetpassword.description", localization = "OSGI-INF/l10n/bundle")
    public @interface Config {

        @AttributeDefinition(name = "%cms.reference.supportedTypes.name", description = "%cms.reference.supportedTypes.description", defaultValue = {
                DEFAULT_RESOURCE_TYPE })
        String[] supportedTypes() default { DEFAULT_RESOURCE_TYPE };
    }

}