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
package org.apache.sling.cms.transformer.internal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.transformer.ThumbnailProvider;
import org.apache.sling.cms.transformer.helpers.SlingCMSContextHelper;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlideShowThumbnailProviderTest {

    private static final Logger log = LoggerFactory.getLogger(SlideShowThumbnailProviderTest.class);

    @Rule
    public final SlingContext context = new SlingContext();

    private Resource docxFile;
    private Resource pptFile;
    private Resource pptxFile;

    private ThumbnailProvider provider;

    @Before
    public void init() {
        SlingCMSContextHelper.initContext(context);
        docxFile = context.resourceResolver().getResource("/content/apache/sling-apache-org/index/Sling.docx");
        pptxFile = context.resourceResolver().getResource("/content/apache/sling-apache-org/index/Sling.pptx");
        pptFile = context.resourceResolver().getResource("/content/apache/sling-apache-org/index/Sling.ppt");
        provider = new SlideShowThumbnailProvider();
    }

    @Test
    public void testApplies() throws IOException {
        log.info("testApplies");
        assertTrue(provider.applies(pptxFile));
        assertTrue(provider.applies(pptFile));
        assertFalse(provider.applies(docxFile));
    }

    @Test
    public void testGetThumbnail() throws IOException {
        log.info("testGetThumbnail");
        assertNotNull(provider.getThumbnail(pptxFile));
        assertNotNull(provider.getThumbnail(pptFile));
    }

}
