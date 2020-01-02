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

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.transformer.helpers.SlingCMSContextHelper;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TikaFallbackProviderTest {

    private static final Logger log = LoggerFactory.getLogger(TikaFallbackProviderTest.class);

    @Rule
    public final SlingContext context = new SlingContext();

    private Resource docxFile;

    private Resource largeFile;

    @Before
    public void init() {
        SlingCMSContextHelper.initContext(context);
        docxFile = context.resourceResolver().getResource("/content/apache/sling-apache-org/index/Sling.docx");
        largeFile = context.resourceResolver().getResource("/content/apache/sling-apache-org/index/editor.min.css");
    }

    @Test
    public void testTikaProvider() throws IOException {
        log.info("testTikaProvider");
        TikaFallbackProvider tfp = new TikaFallbackProvider();
        assertNotNull(tfp.getThumbnail(docxFile));
    }


    @Test
    public void testLargeFile() throws IOException {
        log.info("testLargeFile");
        TikaFallbackProvider tfp = new TikaFallbackProvider();
        assertNotNull(tfp.getThumbnail(largeFile));
    }

}
