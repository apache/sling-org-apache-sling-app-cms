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
package org.apache.sling.cms.core.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.File;
import org.apache.sling.cms.FileMetadataExtractor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileMetadataExtractorImplTest {

    private static final Logger log = LoggerFactory.getLogger(FileMetadataExtractorImplTest.class);

    private File file;

    @Before
    public void init() {

        Resource resource = Mockito.mock(Resource.class);
        Mockito.when(resource.adaptTo(InputStream.class))
                .thenReturn(FileMetadataExtractorImplTest.class.getClassLoader().getResourceAsStream("apache.png"));

        file = Mockito.mock(File.class);
        Mockito.when(file.getResource()).thenReturn(resource);
    }

    @Test
    public void testExtractMetadata() throws IOException {
        FileMetadataExtractor extractor = new FileMetadataExtractorImpl();
        Map<String, Object> metadata = extractor.extractMetadata(file);

        assertNotNull(metadata);
        assertTrue(metadata.size() > 0);

        log.info("Extracted metadata: {}", metadata);
    }
}
