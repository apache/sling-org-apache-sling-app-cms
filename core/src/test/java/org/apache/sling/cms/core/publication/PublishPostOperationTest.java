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
package org.apache.sling.cms.core.publication;

import org.apache.sling.cms.core.helpers.SlingCMSTestHelper;
import org.apache.sling.cms.publication.PublicationException;
import org.apache.sling.cms.publication.PublicationManager;
import org.apache.sling.cms.publication.PublicationManagerFactory;
import org.apache.sling.servlets.post.PostResponse;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

public class PublishPostOperationTest {

    @Rule
    public final SlingContext context = new SlingContext();

    @Before
    public void init() {
        SlingCMSTestHelper.initContext(context);
        context.currentResource("/content/apache/sling-apache-org/index");
    }

    @Test
    public void testPublish() throws PublicationException {
        PublicationManagerFactory publicationManagerFactory = Mockito.mock(PublicationManagerFactory.class);
        PublicationManager publicationManager = Mockito.mock(PublicationManager.class);
        Mockito.when(publicationManagerFactory.getPublicationManager()).thenReturn(publicationManager);
        context.registerService(PublicationManagerFactory.class, publicationManagerFactory);
        PublishPostOperation publishPostOperation = context.registerInjectActivateService(new PublishPostOperation());
        PostResponse response = Mockito.mock(PostResponse.class);

        publishPostOperation.run(context.request(), response, null);

        Mockito.verify(publicationManager).publish(Mockito.any());

        Mockito.verify(response, Mockito.never()).setError(Mockito.any());
        Mockito.verify(response).setPath(context.currentResource().getPath());
    }

    @Test
    public void testPublishFailure() throws PublicationException {
        PublicationManagerFactory publicationManagerFactory = Mockito.mock(PublicationManagerFactory.class);
        PublicationManager publicationManager = Mockito.mock(PublicationManager.class);
        PublicationException ex = new PublicationException("OH NO MR BILL");
        Mockito.doThrow(ex).when(publicationManager).publish(Mockito.any());
        Mockito.when(publicationManagerFactory.getPublicationManager()).thenReturn(publicationManager);
        context.registerService(PublicationManagerFactory.class, publicationManagerFactory);
        PublishPostOperation publishPostOperation = context.registerInjectActivateService(new PublishPostOperation());
        PostResponse response = Mockito.mock(PostResponse.class);

        publishPostOperation.run(context.request(), response, null);

        Mockito.verify(response).setError(ex);
    }

}