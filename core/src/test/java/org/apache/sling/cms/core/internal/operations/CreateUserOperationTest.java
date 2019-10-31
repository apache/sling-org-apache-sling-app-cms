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
package org.apache.sling.cms.core.internal.operations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.security.Principal;

import javax.jcr.AccessDeniedException;
import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;

import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.cms.core.helpers.SlingCMSTestHelper;
import org.apache.sling.cms.core.internal.CommonUtils;
import org.apache.sling.servlets.post.JSONResponse;
import org.apache.sling.servlets.post.PostResponse;
import org.apache.sling.servlets.post.SlingPostConstants;
import org.apache.sling.servlets.post.SlingPostProcessor;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.ImmutableMap;

public class CreateUserOperationTest {

    @Rule
    public SlingContext context = new SlingContext();

    private User user;

    @Before
    public void init() throws AccessDeniedException, UnsupportedRepositoryOperationException, RepositoryException {
        SlingCMSTestHelper.initAuthContext(context);

        user = null;
        UserManager userManager = CommonUtils.getUserManager(context.resourceResolver());
        Mockito.when(
                userManager.createUser(Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.anyString()))
                .thenAnswer((ans) -> {
                    User user = Mockito.mock(User.class);
                    Mockito.when(user.getPath()).thenReturn("/home/users/tests");
                    this.user = user;
                    return user;
                });
    }

    @Test
    public void testCreate() throws RepositoryException {
        CreateUserOperation createUserOperation = new CreateUserOperation();
        PostResponse response = new JSONResponse();

        context.currentResource("/home/users");
        context.request().setParameterMap(ImmutableMap.<String, Object>builder()
                .put(SlingPostConstants.RP_NODE_NAME, "tests").put(CreateUserOperation.PN_PASSWORD, "test5").build());

        createUserOperation.run(context.request(), response,
                new SlingPostProcessor[] { Mockito.mock(SlingPostProcessor.class) });

        assertNull(response.getError());

        assertNotNull(user);
        assertEquals("/home/users/tests", user.getPath());

    }

    @Test
    public void testExisting() throws RepositoryException {
        CreateUserOperation createUserOperation = new CreateUserOperation();
        PostResponse response = new JSONResponse();

        context.currentResource("/home/users");
        context.request().setParameterMap(
                ImmutableMap.<String, Object>builder().put(SlingPostConstants.RP_NODE_NAME, "test5").build());

        UserManager userManager = CommonUtils.getUserManager(context.resourceResolver());
        Mockito.when(userManager.getAuthorizable(Mockito.any(Principal.class))).thenReturn(Mockito.mock(Group.class));

        createUserOperation.run(context.request(), response, null);

        assertNotNull(response.getError());
    }

}
