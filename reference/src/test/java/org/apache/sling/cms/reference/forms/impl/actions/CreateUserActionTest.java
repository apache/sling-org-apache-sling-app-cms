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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.AccessDeniedException;
import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.ValueFactory;
import javax.jcr.ValueFormatException;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.jackrabbit.value.AbstractValueFactory;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.cms.reference.forms.FormActionResult;
import org.apache.sling.cms.reference.forms.FormConstants;
import org.apache.sling.cms.reference.forms.FormException;
import org.apache.sling.cms.reference.forms.FormRequest;
import org.apache.sling.cms.reference.forms.impl.FormRequestImpl;
import org.apache.sling.servlethelpers.MockSlingHttpServletRequest;
import org.apache.sling.testing.resourceresolver.MockResource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CreateUserActionTest {

    private ResourceResolverFactory factory;
    private ResourceResolver resolver;
    private MockResource actionResource;

    @Before
    public void init() throws FormException, LoginException, AccessDeniedException,
            UnsupportedRepositoryOperationException, RepositoryException {

        factory = Mockito.mock(ResourceResolverFactory.class);

        resolver = Mockito.mock(ResourceResolver.class);
        Mockito.when(resolver.getResource(Mockito.any())).thenReturn(Mockito.mock(Resource.class));

        Mockito.when(factory.getServiceResourceResolver(Mockito.anyMap())).thenReturn(resolver);

        JackrabbitSession session = Mockito.mock(JackrabbitSession.class);
        Mockito.when(resolver.adaptTo(Mockito.any())).thenReturn(session);

        ValueFactory vf = new AbstractValueFactory() {

            @Override
            protected void checkPathFormat(String pathValue) throws ValueFormatException {
                // TODO Auto-generated method stub

            }

            @Override
            protected void checkNameFormat(String nameValue) throws ValueFormatException {
                // TODO Auto-generated method stub

            }

        };
        Mockito.when(session.getValueFactory()).thenReturn(vf);

        UserManager userManager = Mockito.mock(UserManager.class);
        Mockito.when(session.getUserManager()).thenReturn(userManager);

        User existingUser = Mockito.mock(User.class);
        Mockito.when(userManager.getAuthorizable("existing@email.com")).thenReturn(existingUser);
        Mockito.when(existingUser.getPath()).thenReturn("/home");

        Group agroup = Mockito.mock(Group.class);
        Mockito.when(agroup.isGroup()).thenReturn(true);
        Mockito.when(userManager.getAuthorizable("group1")).thenReturn(agroup);

        Map<String, Object> properties = new HashMap<>();
        properties.put(CreateUserAction.PN_INTERMEDIATE_PATH, "app");
        properties.put(CreateUserAction.GROUPS, new String[] { "group1" });
        properties.put(CreateUserAction.PROFILE_PROPERTIES, new String[] { "profile", "anotherfield" });
        actionResource = new MockResource("/content", properties, null);

        Mockito.when(
                userManager.createUser(Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.anyString()))
                .thenReturn(existingUser);

    }

    @Test
    public void testHandleForm() throws FormException, RepositoryException {

        CreateUserAction action = new CreateUserAction(factory);

        Mockito.when(resolver.getUserID()).thenReturn("valid@email.com");

        FormRequest request = new FormRequestImpl(new MockSlingHttpServletRequest(resolver), null, null);
        request.getFormData().put(CreateUserAction.PN_USERNAME, "new@email.com");
        request.getFormData().put(FormConstants.PN_PASSWORD, "password1");
        request.getFormData().put("profile", "value");
        request.getFormData().put(CreateUserAction.GROUPS, new String[] { "group1" });

        FormActionResult result = action.handleForm(actionResource, request);
        assertTrue(result.isSucceeded());
    }

    @Test
    public void testExistingUser() throws FormException, RepositoryException {

        CreateUserAction action = new CreateUserAction(factory);

        FormRequest request = new FormRequestImpl(new MockSlingHttpServletRequest(resolver), null, null);
        request.getFormData().put(CreateUserAction.PN_USERNAME, "existing@email.com");
        request.getFormData().put(FormConstants.PN_PASSWORD, "password1");

        FormActionResult result = action.handleForm(actionResource, request);
        assertFalse(result.isSucceeded());
    }

    @Test
    public void testHandles() throws FormException {
        CreateUserAction action = new CreateUserAction(factory);
        Resource validResource = Mockito.mock(Resource.class);
        Mockito.when(validResource.getResourceType()).thenReturn(CreateUserAction.RESOURCE_TYPE);
        assertTrue(action.handles(validResource));

        Resource inValidResource = Mockito.mock(Resource.class);
        Mockito.when(inValidResource.getResourceType()).thenReturn("something/else");
        assertFalse(action.handles(inValidResource));
    }

}
