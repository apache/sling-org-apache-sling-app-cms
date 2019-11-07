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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.transformer.helpers.SlingCMSContextHelper;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageThumbnailProviderTest {

    private static final Logger log = LoggerFactory.getLogger(ImageThumbnailProviderTest.class);

    @Rule
    public final SlingContext context = new SlingContext();

    private Resource imageFile;
    private Resource pdfFile;

    @Before
    public void init() {
        SlingCMSContextHelper.initContext(context);

        imageFile = context.resourceResolver().getResource("/content/apache/sling-apache-org/index/apache.png");

        pdfFile = context.resourceResolver().getResource("/content/apache/sling-apache-org/index/sling.pdf");
    }

    @Test
    public void testContentTypes() throws IOException {
        log.info("testContentTypes");
        ImageThumbnailProvider itp = new ImageThumbnailProvider();

        assertTrue(itp.applies(imageFile));
        assertFalse(itp.applies(pdfFile));
    }

    @Test
    public void testImageThumbnailProvider() throws IOException {
        log.info("testImageThumbnailProvider");
        ImageThumbnailProvider itp = new ImageThumbnailProvider();

        assertNotNull(itp.getThumbnail(imageFile));
        assertArrayEquals(IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("apache.png")),
                IOUtils.toByteArray(itp.getThumbnail(imageFile)));
    }

}
