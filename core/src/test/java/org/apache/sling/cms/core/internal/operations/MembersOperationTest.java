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

import java.util.ArrayList;
import java.util.Collections;

import javax.jcr.AccessDeniedException;
import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;

import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.sling.cms.core.helpers.SlingCMSTestHelper;
import org.apache.sling.servlets.post.JSONResponse;
import org.apache.sling.servlets.post.PostResponse;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

public class MembersOperationTest {

    @Rule
    public SlingContext context = new SlingContext();
    private ArrayList<String> added;
    private ArrayList<String> removed;

    @Before
    public void init() throws AccessDeniedException, UnsupportedRepositoryOperationException, RepositoryException {
        SlingCMSTestHelper.initAuthContext(context);

        Group group = (Group) SlingCMSTestHelper.AUTH_REGISTRY.get("/home/groups/sling-cms/authors");

        added = new ArrayList<>();
        removed = new ArrayList<>();

        Mockito.when(group.addMember(Mockito.any())).then((ans) -> {
            added.add(ans.getArgument(0, Authorizable.class).getPath());
            return true;
        });

        Mockito.when(group.removeMember(Mockito.any())).then((ans) -> {
            removed.add(ans.getArgument(0, Authorizable.class).getPath());
            return true;
        });
    }

    @Test
    public void testModifyOperation() throws RepositoryException {
        MembersOperation membersOperation = new MembersOperation();
        PostResponse response = new JSONResponse();

        context.currentResource("/home/groups/sling-cms/authors");
        context.request().setParameterMap(
                Collections.singletonMap(":members", new String[] { "/home/users/test2", "/home/users/test3" }));

        membersOperation.run(context.request(), response, null);

        assertNull(response.getError());

        assertEquals("/home/groups/sling-cms/authors", response.getPath());

        assertEquals(1, added.size());
        assertEquals("/home/users/test2", added.get(0));

        assertEquals(1, removed.size());
        assertEquals("/home/users/test", removed.get(0));
    }

    @Test
    public void testNotGroup() throws RepositoryException {
        MembersOperation membersOperation = new MembersOperation();
        PostResponse response = new JSONResponse();

        context.currentResource("/home/users/test2");
        context.request().setParameterMap(
                Collections.singletonMap(":members", new String[] { "/home/users/test2", "/home/users/test3" }));

        membersOperation.run(context.request(), response, null);

        assertNotNull(response.getError());
    }

    @Test
    public void testInvalidPath() throws RepositoryException {
        MembersOperation membersOperation = new MembersOperation();
        PostResponse response = new JSONResponse();

        context.currentResource("/home/groups/sling-cms/authors");
        context.request().setParameterMap(
                Collections.singletonMap(":members", new String[] { "/home/users/test2", "/home/users/test4" }));

        membersOperation.run(context.request(), response, null);

        assertNotNull(response.getError());
    }

}
