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

import java.lang.annotation.Annotation;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.AccessDeniedException;
import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Value;
import javax.jcr.ValueFactory;
import javax.jcr.ValueFormatException;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.jackrabbit.value.AbstractValueFactory;
import org.apache.jackrabbit.value.DateValue;
import org.apache.jackrabbit.value.StringValue;
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

public class ResetPasswordActionTest {

    private ResourceResolverFactory factory;
    private ResourceResolver resolver;

    @Before
    public void init() throws FormException, LoginException, AccessDeniedException,
            UnsupportedRepositoryOperationException, RepositoryException {

        factory = Mockito.mock(ResourceResolverFactory.class);

        resolver = Mockito.mock(ResourceResolver.class);
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

        User validUser = Mockito.mock(User.class);
        Mockito.when(validUser.getProperty(FormConstants.PN_RESETTOKEN))
                .thenReturn(new Value[] { new StringValue("123") });
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 24);
        Mockito.when(validUser.getProperty(FormConstants.PN_RESETTIMEOUT))
                .thenReturn(new Value[] { new DateValue(cal) });
        Mockito.when(userManager.getAuthorizable(Mockito.eq("valid@email.com"))).thenReturn(validUser);

        User invalidUser = Mockito.mock(User.class);
        Mockito.when(invalidUser.getProperty(FormConstants.PN_RESETTOKEN))
                .thenReturn(new Value[] { new StringValue("456") });
        Mockito.when(userManager.getAuthorizable(Mockito.eq("invalid@email.com"))).thenReturn(invalidUser);

        User expiredUser = Mockito.mock(User.class);
        Mockito.when(expiredUser.getProperty(FormConstants.PN_RESETTOKEN))
                .thenReturn(new Value[] { new StringValue("456") });
        cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -24);
        Mockito.when(expiredUser.getProperty(FormConstants.PN_RESETTIMEOUT))
                .thenReturn(new Value[] { new DateValue(cal) });
        Mockito.when(userManager.getAuthorizable(Mockito.eq("expired@email.com"))).thenReturn(expiredUser);

    }

    @Test
    public void testHandleForm() throws FormException {

        ResetPasswordAction action = new ResetPasswordAction(factory, null);

        FormRequest request = new FormRequestImpl(new MockSlingHttpServletRequest(resolver), null, null);
        request.getFormData().put("email", "valid@email.com");
        request.getFormData().put(FormConstants.PN_RESETTOKEN, "123");
        request.getFormData().put(FormConstants.PN_PASSWORD, "password1");

        Resource actionResource = new MockResource("/content",
                Collections.singletonMap(RequestPasswordResetAction.PN_RESETTIMEOUT, 1000000), null);

        FormActionResult result = action.handleForm(actionResource, request);
        assertTrue(result.isSucceeded());

    }

    public FormActionResult doReset(String email) throws FormException {

        ResetPasswordAction action = new ResetPasswordAction(factory, null);

        FormRequest request = new FormRequestImpl(new MockSlingHttpServletRequest(resolver), null, null);
        request.getFormData().put("email", email);

        Resource actionResource = new MockResource("/content",
                Collections.singletonMap(RequestPasswordResetAction.PN_RESETTIMEOUT, 2), null);

        return action.handleForm(actionResource, request);

    }

    @Test
    public void testInvalid() throws FormException {

        assertFalse(doReset("invalid@email.com").isSucceeded());
        assertFalse(doReset("expired@email.com").isSucceeded());
        assertFalse(doReset("test123@email.com").isSucceeded());

    }

    @Test
    public void testHandles() throws FormException {
        ResetPasswordAction action = new ResetPasswordAction(null, new ResetPasswordAction.Config() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String[] supportedTypes() {
                return new String[] { ResetPasswordAction.DEFAULT_RESOURCE_TYPE };
            }
        });
        Resource validResource = Mockito.mock(Resource.class);
        Mockito.when(validResource.getResourceType()).thenReturn(ResetPasswordAction.DEFAULT_RESOURCE_TYPE);
        assertTrue(action.handles(validResource));

        Resource inValidResource = Mockito.mock(Resource.class);
        Mockito.when(inValidResource.getResourceType()).thenReturn("something/else");
        assertFalse(action.handles(inValidResource));
    }

}
