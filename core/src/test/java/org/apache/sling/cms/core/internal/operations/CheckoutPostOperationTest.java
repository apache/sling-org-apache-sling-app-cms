/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.sling.cms.core.internal.operations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.version.VersionManager;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.servlets.post.Modification;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Rule;
import org.junit.Test;

public class CheckoutPostOperationTest {

    @Rule
    public SlingContext context = new SlingContext();

    private CheckoutPostOperation operation = new CheckoutPostOperation();

    @Test
    public void testNotApplicable() throws Exception {

        context.request().addRequestParameter(":checkoutPostOp", "false");

        List<Modification> changes = new ArrayList<>();

        operation.process(context.request(), changes);

        assertEquals(0, changes.size());

    }

    @Test
    public void testNotNode() throws Exception {
        Resource resource = mock(Resource.class);
        context.currentResource(resource);
        context.request().addRequestParameter(":checkoutPostOp", "true");
        List<Modification> changes = new ArrayList<>();
        operation.process(context.request(), changes);

        verify(resource).adaptTo(Node.class);
        assertEquals(0, changes.size());
    }

    @Test
    public void testCheckout() throws Exception {
        Resource resource = mock(Resource.class);
        context.currentResource(resource);

        Node node = mock(Node.class);
        when(node.getPath()).thenReturn("/content");
        when(resource.adaptTo(Node.class)).thenReturn(node);

        Session session = mock(Session.class);
        when(node.getSession()).thenReturn(session);

        Workspace workspace = mock(Workspace.class);
        when(session.getWorkspace()).thenReturn(workspace);

        VersionManager versionManager = mock(VersionManager.class);
        when(workspace.getVersionManager()).thenReturn(versionManager);

        context.request().addRequestParameter(":checkoutPostOp", "true");
        List<Modification> changes = new ArrayList<>();
        operation.process(context.request(), changes);

        assertEquals(1, changes.size());

        verify(versionManager).checkout("/content");
    }

}
