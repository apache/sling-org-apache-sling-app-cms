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
import static org.junit.Assert.fail;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.Page;
import org.apache.sling.cms.core.helpers.SlingCMSTestHelper;
import org.apache.sling.cms.publication.PublicationException;
import org.apache.sling.cms.publication.PublicationManager;
import org.apache.sling.distribution.DistributionRequestState;
import org.apache.sling.distribution.DistributionResponse;
import org.apache.sling.distribution.Distributor;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.service.event.EventAdmin;

public class ContentDistributionPublicationManagerTest {
    @Rule
    public final SlingContext context = new SlingContext();

    @Before
    public void init() {
        SlingCMSTestHelper.initContext(context);
        context.currentResource("/content/apache/sling-apache-org/index");
    }

    @Test
    public void testPublish() throws PublicationException {
        EventAdmin eventAdmin = Mockito.mock(EventAdmin.class);
        Distributor distributor = Mockito.mock(Distributor.class);
        DistributionResponse response = Mockito.mock(DistributionResponse.class);
        Mockito.when(response.isSuccessful()).thenReturn(true);
        Mockito.when(distributor.distribute(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(response);
        PublicationManager pman = new ContentDistributionPublicationManager(distributor, new String[] { "testagent" },
                eventAdmin);

        pman.publish(context.currentResource().adaptTo(Page.class));
        Mockito.verify(eventAdmin).postEvent(Mockito.any());
        Mockito.verify(distributor).distribute(Mockito.eq("testagent"), Mockito.any(ResourceResolver.class),
                Mockito.any());
        ValueMap properties = context.currentResource().getChild("jcr:content").getValueMap();
        assertEquals(true, properties.get(CMSConstants.PN_PUBLISHED, Boolean.class));
        assertEquals("ADD", properties.get(CMSConstants.PN_LAST_PUBLICATION_TYPE, String.class));
    }

    @Test
    public void testUnpublish() throws PublicationException {
        EventAdmin eventAdmin = Mockito.mock(EventAdmin.class);
        Distributor distributor = Mockito.mock(Distributor.class);
        DistributionResponse response = Mockito.mock(DistributionResponse.class);
        Mockito.when(response.isSuccessful()).thenReturn(true);
        Mockito.when(distributor.distribute(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(response);
        PublicationManager pman = new ContentDistributionPublicationManager(distributor, new String[] { "testagent" },
                eventAdmin);

        pman.unpublish(context.currentResource().adaptTo(Page.class));
        Mockito.verify(distributor).distribute(Mockito.eq("testagent"), Mockito.any(ResourceResolver.class),
                Mockito.any());
        Mockito.verify(eventAdmin).postEvent(Mockito.any());
        ValueMap properties = context.currentResource().getChild("jcr:content").getValueMap();
        assertEquals(false, properties.get(CMSConstants.PN_PUBLISHED, Boolean.class));
        assertEquals("DELETE", properties.get(CMSConstants.PN_LAST_PUBLICATION_TYPE, String.class));
    }

    @Test
    public void testUnsuccessful() {
        EventAdmin eventAdmin = Mockito.mock(EventAdmin.class);
        Distributor distributor = Mockito.mock(Distributor.class);
        DistributionResponse response = Mockito.mock(DistributionResponse.class);
        Mockito.when(response.isSuccessful()).thenReturn(false);
        Mockito.when(response.getState()).thenReturn(DistributionRequestState.DROPPED);
        Mockito.when(response.getMessage()).thenReturn("Like its hot");
        Mockito.when(distributor.distribute(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(response);
        PublicationManager pman = new ContentDistributionPublicationManager(distributor, new String[] { "testagent" },
                eventAdmin);
        try {
            pman.publish(context.currentResource().adaptTo(Page.class));
            fail();
        } catch (PublicationException pe) {
            assertEquals("Failed to publish: [DROPPED: Like its hot]", pe.getMessage());
        }
    }
}