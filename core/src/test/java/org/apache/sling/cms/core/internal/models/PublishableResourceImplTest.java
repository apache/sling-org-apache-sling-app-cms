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
package org.apache.sling.cms.core.internal.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.sling.cms.PublishableResource;
import org.apache.sling.cms.core.helpers.SlingCMSTestHelper;
import org.apache.sling.cms.publication.PublicationType;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class PublishableResourceImplTest {

    @Rule
    public final SlingContext context = new SlingContext();

    @Before
    public void init() {
        SlingCMSTestHelper.initContext(context);
    }

    @Test
    public void testValid() {
        context.currentResource("/content/apache/sling-apache-org/index");
        PublishableResource publishableResource = context.currentResource().adaptTo(PublishableResource.class);
        assertNotNull(publishableResource);
        assertNotNull(publishableResource.getContentResource());
        assertEquals("/content/apache/sling-apache-org/index/jcr:content",
                publishableResource.getContentResource().getPath());
        assertNotNull(publishableResource.getCreated());
        assertEquals("admin", publishableResource.getCreatedBy());
        assertNotNull(publishableResource.getLastModified());
        assertEquals("admin", publishableResource.getLastModifiedBy());
        assertNotNull(publishableResource.getLastPublication());
        assertEquals("admin", publishableResource.getLastPublicationBy());
        assertEquals(PublicationType.ADD, publishableResource.getLastPublicationType());
        assertEquals("index", publishableResource.getName());
        assertEquals(context.currentResource().getParent().getPath(), publishableResource.getParent().getPath());
        assertNotNull(publishableResource.getProperties());
        assertEquals("/index", publishableResource.getPublishedPath());
        assertEquals("https://sling.apache.org/index", publishableResource.getPublishedUrl());
        assertEquals(context.currentResource().getPath(), publishableResource.getResource().getPath());
        assertNotNull(publishableResource.getSite());
    }

    @Test
    public void testInValid() {
        context.currentResource("/content/apache/sling-apache-org/index/jcr:content");
        PublishableResource publishableResource = context.currentResource().adaptTo(PublishableResource.class);
        assertNotNull(publishableResource);
        assertNull(publishableResource.getContentResource());
    }
}