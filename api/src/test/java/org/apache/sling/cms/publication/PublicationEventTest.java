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
package org.apache.sling.cms.publication;

import static org.junit.Assert.assertEquals;

import org.apache.sling.api.SlingConstants;
import org.apache.sling.cms.PublishableResource;
import org.junit.Test;
import org.mockito.Mockito;

public class PublicationEventTest {

    @Test
    public void testPublish() {

        PublishableResource resource = Mockito.mock(PublishableResource.class);
        Mockito.when(resource.getPath()).thenReturn("/content");
        PublicationEvent evt = PublicationEvent.publish(resource, false);

        assertEquals(PublicationEvent.EVENT_PUBLISH, evt.getTopic());
        assertEquals("/content", evt.getProperty(SlingConstants.PROPERTY_PATH));
        assertEquals(false, evt.getProperty(PublicationEvent.PN_DEEP));
        assertEquals(PublicationType.ADD.toString(), evt.getProperty(PublicationEvent.PN_PUBLICATION_TYPE));
    }

    @Test
    public void testUnpublish() {

        PublishableResource resource = Mockito.mock(PublishableResource.class);
        Mockito.when(resource.getPath()).thenReturn("/content2");
        PublicationEvent evt = PublicationEvent.unpublish(resource, true);

        assertEquals(PublicationEvent.EVENT_UNPUBLISH, evt.getTopic());
        assertEquals("/content2", evt.getProperty(SlingConstants.PROPERTY_PATH));
        assertEquals(true, evt.getProperty(PublicationEvent.PN_DEEP));
        assertEquals(PublicationType.DELETE.toString(), evt.getProperty(PublicationEvent.PN_PUBLICATION_TYPE));
    }
}