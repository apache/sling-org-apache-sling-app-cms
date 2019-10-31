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

public class CreateGroupOperationTest {

    @Rule
    public SlingContext context = new SlingContext();

    private Group group;

    @Before
    public void init() throws AccessDeniedException, UnsupportedRepositoryOperationException, RepositoryException {
        SlingCMSTestHelper.initAuthContext(context);

        group = null;
        UserManager userManager = CommonUtils.getUserManager(context.resourceResolver());
        Mockito.when(userManager.createGroup(Mockito.anyString(), Mockito.any(), Mockito.anyString()))
                .thenAnswer((ans) -> {
                    Group group = Mockito.mock(Group.class);
                    Mockito.when(group.getPath()).thenReturn("/home/groups/tests");
                    this.group = group;
                    return group;
                });
    }

    @Test
    public void testCreate() throws RepositoryException {
        CreateGroupOperation createGroupOperation = new CreateGroupOperation();
        PostResponse response = new JSONResponse();

        context.currentResource("/home/users");
        context.request().setParameterMap(
                ImmutableMap.<String, Object>builder().put(SlingPostConstants.RP_NODE_NAME, "test5").build());

        createGroupOperation.run(context.request(), response,
                new SlingPostProcessor[] { Mockito.mock(SlingPostProcessor.class) });

        assertNull(response.getError());

        assertNotNull(group);
        assertEquals("/home/groups/tests", group.getPath());

    }

    @Test
    public void testExisting() throws RepositoryException {
        CreateGroupOperation createGroupOperation = new CreateGroupOperation();
        PostResponse response = new JSONResponse();

        context.currentResource("/home/users");
        context.request().setParameterMap(
                ImmutableMap.<String, Object>builder().put(SlingPostConstants.RP_NODE_NAME, "test5").build());

        UserManager userManager = CommonUtils.getUserManager(context.resourceResolver());
        Mockito.when(userManager.getAuthorizable(Mockito.any(Principal.class))).thenReturn(Mockito.mock(Group.class));

        createGroupOperation.run(context.request(), response, null);

        assertNotNull(response.getError());
    }

}
