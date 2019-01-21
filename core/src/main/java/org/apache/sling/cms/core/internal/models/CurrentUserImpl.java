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
package org.apache.sling.cms.core.internal.models;

import java.util.Collection;
import java.util.HashSet;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.CurrentUser;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = { ResourceResolver.class }, adapters = CurrentUser.class)
public class CurrentUserImpl implements CurrentUser {

    private ResourceResolver resolver;
    private UserManager userManager;

    private Collection<String> groupNames;

    private static final Logger log = LoggerFactory.getLogger(CurrentUserImpl.class);

    public CurrentUserImpl(ResourceResolver resolver) {
        this.resolver = resolver;

        try {
            Session session = resolver.adaptTo(Session.class);
            JackrabbitSession js = (JackrabbitSession) session;
            if (js != null) {
                userManager = js.getUserManager();
            }
        } catch (RepositoryException e) {
            log.warn("Failed to get user manager", e);
        }
    }

    @Override
    public String getId() {
        return resolver.getUserID();
    }

    @Override
    public Collection<String> getGroups() {
        if (groupNames == null) {
            groupNames = new HashSet<>();
            User user = null;
            try {
                user = (User) userManager.getAuthorizable(getId());
                user.memberOf().forEachRemaining(g -> {
                    try {
                        groupNames.add(g.getID());
                    } catch (RepositoryException e) {
                        log.warn("Failed to get group name", e);
                    }
                });
            } catch (RepositoryException re) {
                log.warn("Failed to get user", re);
            }
        }
        return groupNames;
    }

    @Override
    public boolean isMember(String groupName) {
        if (CMSConstants.USER_ADMIN.equals(getId()) || getGroups().contains(CMSConstants.GROUP_ADMINISTRATORS)
                || getGroups().contains(groupName)) {
            return true;
        }
        return false;
    }

}
