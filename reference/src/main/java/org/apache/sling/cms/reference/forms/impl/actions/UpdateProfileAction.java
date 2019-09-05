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
import java.util.Calendar;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.ValueFactory;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.cms.reference.forms.FormAction;
import org.apache.sling.cms.reference.forms.FormActionResult;
import org.apache.sling.cms.reference.forms.FormException;
import org.apache.sling.cms.reference.forms.FormRequest;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = FormAction.class)
public class UpdateProfileAction implements FormAction {

    private static final Logger log = LoggerFactory.getLogger(UpdateProfileAction.class);

    @Override
    public FormActionResult handleForm(Resource actionResource, FormRequest request) throws FormException {
        ResourceResolver resolver = request.getOriginalRequest().getResourceResolver();
        String userId = resolver.getUserID();
        JackrabbitSession session = (JackrabbitSession) resolver.adaptTo(Session.class);

        if (session != null) {
            try {
                final UserManager userManager = session.getUserManager();
                if (userManager.getAuthorizable(userId) != null) {

                    User user = (User) userManager.getAuthorizable(userId);
                    log.debug("Updating profile for {}", userId);

                    String subpath = actionResource.getValueMap().get("subpath", "profile");
                    ValueFactory valueFactory = session.getValueFactory();

                    for (Entry<String, Object> e : request.getFormData().entrySet()) {
                        Value value = null;
                        if (e.getValue() instanceof String[]) {
                            user.setProperty(subpath + "/" + e.getKey(), Arrays.stream((String[]) e.getValue())
                                    .map(valueFactory::createValue).collect(Collectors.toList()).toArray(new Value[0]));
                        } else {
                            if (e.getValue() instanceof Calendar) {
                                value = valueFactory.createValue((Calendar) e.getValue());
                            } else if (e.getValue() instanceof Double) {
                                value = valueFactory.createValue((Double) e.getValue());
                            } else if (e.getValue() instanceof Integer) {
                                value = valueFactory.createValue((Double) e.getValue());
                            } else {
                                value = valueFactory.createValue((String) e.getValue());
                            }
                            user.setProperty(subpath + "/" + e.getKey(), value);
                        }
                    }
                    log.debug("Saving changes!");
                    resolver.commit();

                    return FormActionResult.success("Profile Updated");
                } else {
                    log.warn("No profile found for {}", userId);
                    return FormActionResult.failure("No profile found for " + userId);
                }
            } catch (RepositoryException | PersistenceException e) {
                log.warn("Failed to update profile for {}", userId, e);
                return FormActionResult.failure("Failed to update profile for " + userId);
            }
        } else {
            log.warn("Failed to get session for {}", userId);
            return FormActionResult.failure("Failed to get session for " + userId);
        }
    }

    @Override
    public boolean handles(Resource actionResource) {
        return "reference/components/forms/actions/updateprofile".equals(actionResource.getResourceType());
    }

}
