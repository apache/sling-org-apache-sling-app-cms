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
package org.apache.sling.cms.reference.forms.impl.providers;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.cms.reference.forms.FormValueProvider;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = FormValueProvider.class)
public class UserProfileFormValueProvider implements FormValueProvider {

    private static final Logger log = LoggerFactory.getLogger(UserProfileFormValueProvider.class);

    @Override
    public void loadValues(Resource providerResource, Map<String, Object> formData) {
        log.trace("loadFormData");
        try {
            ResourceResolver resolver = providerResource.getResourceResolver();
            String userId = resolver.getUserID();
            JackrabbitSession session = (JackrabbitSession) resolver.adaptTo(Session.class);
            if (session != null) {
                UserManager userManager = session.getUserManager();
                User user = (User) userManager.getAuthorizable(userId);

                String subpath = providerResource.getValueMap().get("subpath", "profile");
                log.debug("Loading profile data from: {}/{}", user.getPath(), subpath);

                Iterator<String> keys = user.getPropertyNames(subpath);
                while (keys.hasNext()) {

                    String key = keys.next();
                    log.debug("Loading key {}", key);
                    loadKey(formData, subpath, key, user);
                }
            } else {
                log.warn("Failed to load Jackrabbit session for request");
            }
        } catch (RepositoryException e) {
            log.warn("Exception loading values from user profile", e);
        }
    }

    private void loadKey(Map<String, Object> formData, String subpath, String key, User user) {
        Object value = null;

        try {
            Value[] v = user.getProperty(subpath + "/" + key);
            if (v.length > 1) {
                value = Arrays.stream(v).map(t -> {
                    try {
                        return t.getString();
                    } catch (IllegalStateException | RepositoryException e) {
                        log.warn("Failed to get string value for " + key, e);
                        return null;
                    }
                }).collect(Collectors.toList()).toArray(new String[0]);
            } else if (v[0].getType() == PropertyType.LONG) {
                value = v[0].getLong();
            } else if (v[0].getType() == PropertyType.DOUBLE) {
                value = v[0].getDouble();
            } else if (v[0].getType() == PropertyType.DATE) {
                value = v[0].getDate();
            } else {
                value = v[0].getString();
            }
            formData.put(key, value);
        } catch (RepositoryException e) {
            log.warn("Failed to get string value for " + key, e);
        }
    }

    @Override
    public boolean handles(Resource valueProviderResource) {
        return "reference/components/forms/providers/userprofile".equals(valueProviderResource.getResourceType());
    }
}
