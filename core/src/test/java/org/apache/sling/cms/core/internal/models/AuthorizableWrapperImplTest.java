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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.Principal;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Collectors;

import javax.jcr.AccessDeniedException;
import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.AuthorizableExistsException;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.cms.AuthorizableWrapper;
import org.apache.sling.cms.core.helpers.SlingCMSTestHelper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class AuthorizableWrapperImplTest {

    private static final String ADMIN_PATH = "/home/users/admin";
    private static final String GROUP_PATH = "/home/groups/group1";
    private static final String GROUP2_PATH = "/home/groups/group2";
    private static final String USER_PATH = "/home/users/user1";
    private static final String THROWY_PATH = "/home/users/throwy";
    private Resource adminResource;
    private Resource contentResource;
    private Group group;
    private Group group2;
    private Resource groupResource;
    private User user;
    private Resource userResource;
    private Authorizable throwy;
    private Resource throwyResource;
    private Resource group2Resource;

    @Before
    public void init() throws AccessDeniedException, AuthorizableExistsException,
            UnsupportedRepositoryOperationException, RepositoryException {

        userResource = Mockito.mock(Resource.class);
        Mockito.when(userResource.getPath()).thenReturn(USER_PATH);
        groupResource = Mockito.mock(Resource.class);
        Mockito.when(groupResource.getPath()).thenReturn(GROUP_PATH);
        contentResource = Mockito.mock(Resource.class);
        Mockito.when(contentResource.getPath()).thenReturn("/content");
        adminResource = Mockito.mock(Resource.class);
        Mockito.when(adminResource.getPath()).thenReturn(ADMIN_PATH);
        throwyResource = Mockito.mock(Resource.class);
        Mockito.when(throwyResource.getPath()).thenReturn(THROWY_PATH);
        group2Resource = Mockito.mock(Resource.class);
        Mockito.when(group2Resource.getPath()).thenReturn(GROUP2_PATH);

        ResourceResolver resolver = Mockito.mock(ResourceResolver.class);
        Mockito.when(userResource.getResourceResolver()).thenReturn(resolver);
        Mockito.when(groupResource.getResourceResolver()).thenReturn(resolver);
        Mockito.when(contentResource.getResourceResolver()).thenReturn(resolver);
        Mockito.when(adminResource.getResourceResolver()).thenReturn(resolver);
        Mockito.when(throwyResource.getResourceResolver()).thenReturn(resolver);
        Mockito.when(group2Resource.getResourceResolver()).thenReturn(resolver);
        Mockito.when(resolver.getUserID()).thenReturn("123");

        JackrabbitSession session = Mockito.mock(JackrabbitSession.class);
        Mockito.when(resolver.adaptTo(Mockito.any())).thenReturn(session);

        UserManager userManager = Mockito.mock(UserManager.class);
        Mockito.when(session.getUserManager()).thenReturn(userManager);

        throwy = Mockito.mock(Authorizable.class, inv -> {
            throw new RepositoryException("YAY JCR!");
        });
        Mockito.when(userManager.getAuthorizableByPath(THROWY_PATH)).thenReturn(throwy);

        User adminUser = Mockito.mock(User.class);
        Mockito.when(adminUser.getID()).thenReturn("admin");

        Mockito.when(userManager.getAuthorizableByPath(ADMIN_PATH)).thenReturn(adminUser);

        user = Mockito.mock(User.class);
        Mockito.when(user.getID()).thenReturn("123");
        Mockito.when(userManager.getAuthorizableByPath(USER_PATH)).thenReturn(user);
        Mockito.when(userManager.getAuthorizable("123")).thenReturn(user);

        group = Mockito.mock(Group.class);
        Mockito.when(group.getID()).thenReturn("456");
        Mockito.when(group.isGroup()).thenReturn(true);
        Mockito.when(group.getMembers()).thenReturn(Collections.singletonList((Authorizable) user).iterator());
        Mockito.when(group.getPrincipal()).thenReturn(new Principal() {
            @Override
            public String getName() {
                return "456";
            }
        });
        Mockito.when(user.memberOf()).thenReturn(Collections.singletonList(group).iterator());
        Mockito.when(userManager.getAuthorizableByPath(GROUP_PATH)).thenReturn(group);

        group2 = Mockito.mock(Group.class);
        Mockito.when(group2.isGroup()).thenReturn(true);
        Mockito.when(adminUser.declaredMemberOf()).thenReturn(Collections.singletonList(group2).iterator());
        Mockito.when(group2.getDeclaredMembers())
                .thenReturn(Collections.singletonList((Authorizable) adminUser).iterator());
        Mockito.when(userManager.getAuthorizableByPath(GROUP2_PATH)).thenReturn(group2);

    }

    @Test
    public void testResourceResolver()
            throws AccessDeniedException, UnsupportedRepositoryOperationException, RepositoryException {

        AuthorizableWrapper authWrapper = new AuthorizableWrapperImpl(userResource.getResourceResolver());
        assertNotNull(authWrapper.getAuthorizable());
        assertEquals(user, authWrapper.getAuthorizable());
    }

    @Test
    public void testGetAuthorizable()
            throws AccessDeniedException, UnsupportedRepositoryOperationException, RepositoryException {

        AuthorizableWrapper authWrapper = new AuthorizableWrapperImpl(userResource);
        assertNotNull(authWrapper);
        assertEquals(user, authWrapper.getAuthorizable());

        authWrapper = new AuthorizableWrapperImpl(groupResource);
        assertNotNull(authWrapper);
        assertEquals(group, authWrapper.getAuthorizable());

        try {
            authWrapper = new AuthorizableWrapperImpl(contentResource);
            fail();
        } catch (RepositoryException re) {
            // expected
        }
    }

    @Test
    public void testGetDeclaredMembers()
            throws AccessDeniedException, UnsupportedRepositoryOperationException, RepositoryException {
        AuthorizableWrapper authWrapper = new AuthorizableWrapperImpl(group2Resource);

        Iterator<Authorizable> members = authWrapper.getDeclaredMembers();
        assertTrue(members.hasNext());
        assertEquals(1, SlingCMSTestHelper.toStream(members).count());

        authWrapper = new AuthorizableWrapperImpl(throwyResource);
        assertFalse(authWrapper.getDeclaredMembers().hasNext());

        authWrapper = new AuthorizableWrapperImpl(adminResource);
        assertFalse(authWrapper.getDeclaredMembers().hasNext());
    }

    @Test
    public void testGetMembers()
            throws AccessDeniedException, UnsupportedRepositoryOperationException, RepositoryException {
        AuthorizableWrapper authWrapper = new AuthorizableWrapperImpl(groupResource);

        Iterator<Authorizable> members = authWrapper.getMembers();
        assertTrue(members.hasNext());
        assertEquals(1, SlingCMSTestHelper.toStream(members).count());

        authWrapper = new AuthorizableWrapperImpl(throwyResource);
        assertFalse(authWrapper.getMembers().hasNext());

        authWrapper = new AuthorizableWrapperImpl(userResource);
        assertFalse(authWrapper.getMembers().hasNext());
    }

    @Test
    public void testGetGroupNames()
            throws AccessDeniedException, UnsupportedRepositoryOperationException, RepositoryException {
        AuthorizableWrapper authWrapper = new AuthorizableWrapperImpl(userResource);

        assertEquals(Collections.singletonList("456"),
                SlingCMSTestHelper.toStream(authWrapper.getGroupNames()).collect(Collectors.toList()));

        authWrapper = new AuthorizableWrapperImpl(throwyResource);
        assertFalse(authWrapper.getGroupNames().hasNext());
    }

    @Test
    public void testGetId() throws AccessDeniedException, UnsupportedRepositoryOperationException, RepositoryException {
        AuthorizableWrapper authWrapper = new AuthorizableWrapperImpl(userResource);

        assertEquals("123", authWrapper.getId());

        authWrapper = new AuthorizableWrapperImpl(throwyResource);
        assertNull(authWrapper.getId());
    }

    @Test
    public void testIsAdmin()
            throws AccessDeniedException, UnsupportedRepositoryOperationException, RepositoryException {
        AuthorizableWrapper authWrapper = new AuthorizableWrapperImpl(userResource);

        assertFalse(authWrapper.isAdministrator());

        authWrapper = new AuthorizableWrapperImpl(adminResource);
        assertTrue(authWrapper.isAdministrator());

        authWrapper = new AuthorizableWrapperImpl(throwyResource);
        assertFalse(authWrapper.isAdministrator());
    }

    @Test
    public void testIsMember()
            throws AccessDeniedException, UnsupportedRepositoryOperationException, RepositoryException {
        AuthorizableWrapper authWrapper = new AuthorizableWrapperImpl(userResource);
        assertTrue(authWrapper.isMember("456"));

        authWrapper = new AuthorizableWrapperImpl(throwyResource);
        assertFalse(authWrapper.isMember("456"));

    }

    @Test
    public void testDeclaredMembership()
            throws AccessDeniedException, UnsupportedRepositoryOperationException, RepositoryException {
        AuthorizableWrapper authWrapper = new AuthorizableWrapperImpl(adminResource);
        assertTrue(SlingCMSTestHelper.toStream(authWrapper.getDeclaredMembership()).anyMatch(g -> g == group2));

        authWrapper = new AuthorizableWrapperImpl(throwyResource);
        assertFalse(authWrapper.getDeclaredMembership().hasNext());
    }

    @Test
    public void testMembership()
            throws AccessDeniedException, UnsupportedRepositoryOperationException, RepositoryException {
        AuthorizableWrapper authWrapper = new AuthorizableWrapperImpl(userResource);
        assertTrue(SlingCMSTestHelper.toStream(authWrapper.getMembership()).anyMatch(g -> g == group));

        authWrapper = new AuthorizableWrapperImpl(throwyResource);
        assertFalse(authWrapper.getMembership().hasNext());
    }

    @Test
    public void testNonMember()
            throws AccessDeniedException, UnsupportedRepositoryOperationException, RepositoryException {
        AuthorizableWrapper authWrapper = new AuthorizableWrapperImpl(userResource);
        assertFalse(SlingCMSTestHelper.toStream(authWrapper.getMembership()).anyMatch(g -> g == group2));
    }

}
