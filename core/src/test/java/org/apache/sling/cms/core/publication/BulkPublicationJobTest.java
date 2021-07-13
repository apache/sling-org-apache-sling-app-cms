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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.sling.cms.core.helpers.SlingCMSTestHelper;
import org.apache.sling.cms.publication.PublicationException;
import org.apache.sling.cms.publication.PublicationManager;
import org.apache.sling.cms.publication.PublicationManagerFactory;
import org.apache.sling.cms.publication.PublicationType;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobExecutionContext;
import org.apache.sling.event.jobs.consumer.JobExecutionContext.ResultBuilder;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

public class BulkPublicationJobTest {

    @Rule
    public final SlingContext context = new SlingContext();
    private PublicationManager publicationManager;

    @Before
    public void init() {
        SlingCMSTestHelper.initContext(context);

        PublicationManagerFactory publicationManagerFactory = Mockito.mock(PublicationManagerFactory.class);
        publicationManager = Mockito.mock(PublicationManager.class);
        Mockito.when(publicationManagerFactory.getPublicationManager()).thenReturn(publicationManager);
        context.registerService(PublicationManagerFactory.class, publicationManagerFactory);
    }

    @Test
    public void testShallow() throws PublicationException {

        BulkPublicationJob pubJob = context.registerInjectActivateService(new BulkPublicationJob());

        JobExecutionContext jobContext = Mockito.mock(JobExecutionContext.class);
        ResultBuilder result = Mockito.mock(ResultBuilder.class);
        Mockito.when(jobContext.result()).thenReturn(result);
        Job job = Mockito.mock(Job.class);

        Mockito.when(job.getProperty(Mockito.matches("paths"), Mockito.any())).thenReturn(new String[] { "/content" });
        Mockito.when(job.getProperty(Mockito.matches("type"), Mockito.any()))
                .thenReturn(PublicationType.ADD.toString());
        Mockito.when(job.getProperty(Mockito.matches("deep"), Mockito.anyBoolean())).thenReturn(false);

        pubJob.doProcess(job, jobContext, context.resourceResolver());

        Mockito.verify(publicationManager).publish(Mockito.any());
        Mockito.verify(result).succeeded();
    }

    @Test
    public void testDeep() throws PublicationException {

        BulkPublicationJob pubJob = context.registerInjectActivateService(new BulkPublicationJob());

        JobExecutionContext jobContext = Mockito.mock(JobExecutionContext.class);
        ResultBuilder result = Mockito.mock(ResultBuilder.class);
        Mockito.when(jobContext.result()).thenReturn(result);
        Job job = Mockito.mock(Job.class);

        Mockito.when(job.getProperty(Mockito.matches("paths"), Mockito.any())).thenReturn(new String[] { "/content" });
        Mockito.when(job.getProperty(Mockito.matches("type"), Mockito.any()))
                .thenReturn(PublicationType.ADD.toString());
        Mockito.when(job.getProperty(Mockito.matches("deep"), Mockito.anyBoolean())).thenReturn(true);

        pubJob.doProcess(job, jobContext, context.resourceResolver());

        Mockito.verify(publicationManager, Mockito.atLeast(5)).publish(Mockito.any());
        Mockito.verify(result).succeeded();
    }

    @Test
    public void testMethods() throws PublicationException {
        BulkPublicationJob pubJob = context.registerInjectActivateService(new BulkPublicationJob());
        assertEquals("/mnt/overlay/sling-cms/content/publication/bulk", pubJob.getConfigurationPath());
        assertNotNull(pubJob.getResolverFactory());
        assertEquals("Bulk Publication", pubJob.getTitleKey());
        assertEquals(BulkPublicationJob.TOPIC, pubJob.getTopic());
    }

}
