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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

import javax.jcr.RepositoryException;

import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.cms.AuthorizableWrapper;
import org.apache.sling.cms.core.internal.CommonUtils;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the AuthorizableWrapper Sling Model.
 */
@Model(adaptables = { Resource.class, ResourceResolver.class }, adapters = AuthorizableWrapper.class)
public class AuthorizableWrapperImpl implements AuthorizableWrapper {

    private static final Logger log = LoggerFactory.getLogger(AuthorizableWrapperImpl.class);
    private final Authorizable authorizable;

    public AuthorizableWrapperImpl(ResourceResolver resolver) throws RepositoryException {
        authorizable = CommonUtils.getUserManager(resolver).getAuthorizable(resolver.getUserID());
    }

    public AuthorizableWrapperImpl(Resource resource) throws RepositoryException {
        this.authorizable = CommonUtils.getUserManager(resource.getResourceResolver())
                .getAuthorizableByPath(resource.getPath());
        if (authorizable == null) {
            throw new RepositoryException("Failed to get authorizable from " + resource);
        }
    }

    @Override
    public Authorizable getAuthorizable() {
        return authorizable;
    }

    @Override
    public Iterator<Authorizable> getDeclaredMembers() {
        try {
            if (authorizable.isGroup()) {
                return ((Group) authorizable).getDeclaredMembers();
            } else {
                List<Authorizable> empty = Collections.emptyList();
                return empty.iterator();
            }
        } catch (RepositoryException e) {
            log.error("Failed to get declared members of authorizable: {}", authorizable, e);
            return Collections.emptyIterator();
        }
    }

    @Override
    public Iterator<Group> getDeclaredMembership() {
        try {
            return authorizable.declaredMemberOf();
        } catch (RepositoryException e) {
            log.error("Failed to get declared membership of authorizable: {}", authorizable, e);
            return Collections.emptyIterator();
        }
    }

    @Override
    public Iterator<String> getGroupNames() {
        Iterable<Group> iterable = this::getMembership;
        return StreamSupport.stream(iterable.spliterator(), false).map(g -> {
            try {
                return g.getPrincipal().getName();
            } catch (RepositoryException e) {
                log.error("Failed to get name from group: {}", g, e);
                return null;
            }
        }).iterator();
    }

    @Override
    public String getId() {
        try {
            return authorizable.getID();
        } catch (RepositoryException e) {
            log.error("Failed to get ID from authorizable: {}", authorizable, e);
            return null;
        }
    }

    @Override
    public Iterator<Authorizable> getMembers() {
        try {
            if (authorizable.isGroup()) {
                return ((Group) authorizable).getMembers();
            } else {
                List<Authorizable> empty = Collections.emptyList();
                return empty.iterator();
            }
        } catch (RepositoryException e) {
            log.error("Failed to get membership of authorizable: {}", authorizable, e);
            return Collections.emptyIterator();
        }
    }

    @Override
    public Iterator<Group> getMembership() {
        try {
            return authorizable.memberOf();
        } catch (RepositoryException e) {
            log.error("Failed to get membership of authorizable: {}", authorizable, e);
            return Collections.emptyIterator();
        }
    }

    @Override
    public boolean isAdministrator() {
        try {
            return !authorizable.isGroup() && ("admin".equals(authorizable.getID()) || isMember("administrators"));
        } catch (RepositoryException e) {
            log.error("Failed to check if authorizable is an administrator", e);
            return false;
        }
    }

    @Override
    public boolean isMember(String groupName) {
        Iterable<Group> iterable = () -> getMembership();
        return StreamSupport.stream(iterable.spliterator(), false).anyMatch(g -> {
            try {
                return groupName.equals(g.getID());
            } catch (RepositoryException e) {
                log.error("Failed to get ID from authorizable: {}", g, e);
                return false;
            }
        });
    }

}
