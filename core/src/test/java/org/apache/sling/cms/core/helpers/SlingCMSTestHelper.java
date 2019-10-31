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
package org.apache.sling.cms.core.helpers;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.jcr.AccessDeniedException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.cms.ResourceTree;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.mockito.Mockito;

import com.google.common.base.Function;

public class SlingCMSTestHelper {

    public static final void initContext(SlingContext context) {
        context.addModelsForPackage("org.apache.sling.cms.core.internal.models");
        context.addModelsForPackage("org.apache.sling.cms.core.models");

        context.load().json("/content.json", "/content");
        context.load().binaryResource("/apache.png", "/content/apache/sling-apache-org/index/apache.png/jcr:content");

        context.registerAdapter(Resource.class, InputStream.class, new Function<Resource, InputStream>() {
            public InputStream apply(Resource input) {
                return input.getValueMap().get("jcr:content/jcr:data", InputStream.class);
            }
        });
    }

    public static final Map<String, Authorizable> AUTH_REGISTRY = new HashMap<>();

    public static final void initAuthContext(SlingContext context)
            throws AccessDeniedException, UnsupportedRepositoryOperationException, RepositoryException {
        initContext(context);
        context.load().json("/auth.json", "/home");

        JackrabbitSession session = Mockito.mock(JackrabbitSession.class);

        AUTH_REGISTRY.clear();

        ResourceTree.stream(context.resourceResolver().getResource("/home/users"), "rep:User").forEach(u -> {

            User user = Mockito.mock(User.class);
            try {
                Mockito.when(user.getID()).thenReturn(u.getResource().getValueMap().get("rep:principalName", ""));
                Mockito.when(user.getPath()).thenReturn(u.getResource().getPath());
                Mockito.when(user.declaredMemberOf()).thenAnswer((ans) -> {
                    final List<Group> groups = new ArrayList<>();
                    AUTH_REGISTRY.values().forEach(a -> {
                        if (a instanceof Group) {
                            try {
                                ((Group) a).getDeclaredMembers().forEachRemaining(m -> {
                                    if (m == user) {
                                        groups.add((Group) a);
                                    }
                                });
                            } catch (RepositoryException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                    return groups.iterator();
                });
            } catch (RepositoryException e) {
                throw new RuntimeException(e);
            }
            AUTH_REGISTRY.put(u.getResource().getPath(), user);
        });

        ResourceTree.stream(context.resourceResolver().getResource("/home/groups/sling-cms"), "rep:Group")
                .forEach(g -> {

                    Group group = Mockito.mock(Group.class);
                    try {
                        Mockito.when(group.getID())
                                .thenReturn(g.getResource().getValueMap().get("rep:principalName", ""));
                        Mockito.when(group.getPath()).thenReturn(g.getResource().getPath());
                        Mockito.when(group.isGroup()).thenReturn(true);
                        Mockito.when(group.declaredMemberOf()).thenAnswer((ans) -> {
                            final List<Group> groups = new ArrayList<>();
                            AUTH_REGISTRY.values().forEach(a -> {
                                if (a instanceof Group) {
                                    try {
                                        ((Group) a).getDeclaredMembers().forEachRemaining(m -> {
                                            if (m == group) {
                                                groups.add((Group) a);
                                            }
                                        });
                                    } catch (RepositoryException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            });
                            return groups.iterator();
                        });
                        Mockito.when(group.getDeclaredMembers()).thenAnswer((ans) -> {
                            final List<Authorizable> members = new ArrayList<>();
                            for (String member : g.getResource().getValueMap().get("members", new String[0])) {
                                if (AUTH_REGISTRY.containsKey(member)) {
                                    members.add(AUTH_REGISTRY.get(member));
                                }
                            }

                            return members.iterator();
                        });
                    } catch (RepositoryException e) {
                        throw new RuntimeException(e);
                    }

                    AUTH_REGISTRY.put(g.getResource().getPath(), group);
                });

        UserManager userManager = Mockito.mock(UserManager.class);
        Mockito.when(session.getUserManager()).thenReturn(userManager);
        Mockito.when(userManager.getAuthorizableByPath(Mockito.anyString())).thenAnswer((ans) -> {
            String path = ans.getArgument(0);
            return AUTH_REGISTRY.get(path);
        });
        context.registerAdapter(ResourceResolver.class, Session.class, session);
    }

    public static final <I> Stream<I> toStream(Iterator<I> iterator) {
        Iterable<I> iterable = () -> iterator;
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}
