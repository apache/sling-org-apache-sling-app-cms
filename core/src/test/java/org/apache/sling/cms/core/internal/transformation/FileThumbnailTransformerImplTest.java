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
package org.apache.sling.cms.core.internal.transformation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.poi.util.IOUtils;
import org.apache.sling.cms.core.helpers.SlingCMSContextHelper;
import org.apache.sling.cms.transformation.FileThumbnailTransformer;
import org.apache.sling.cms.transformation.ThumbnailProvider;
import org.apache.sling.cms.transformation.TransformationHandler;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.common.collect.Lists;

public class FileThumbnailTransformerImplTest {
    @Rule
    public final SlingContext context = new SlingContext();
    private FileThumbnailTransformer transformer;

    @Before
    public void init() {
        SlingCMSContextHelper.initContext(context);
        transformer = new FileThumbnailTransformerImpl();
        ((FileThumbnailTransformerImpl) transformer)
                .setHandlers(Lists.asList(new CropHandler(), new TransformationHandler[] { new SizeHandler() }));
        ((FileThumbnailTransformerImpl) transformer).setThumbnailProviders(
                Lists.asList(new ImageThumbnailProvider(), new ThumbnailProvider[] { new PdfThumbnailProvider() }));
    }

    @Test
    public void testGetHandler() {
        assertNotNull(transformer.getTransformationHandler("size-200-200"));
        assertNull(transformer.getTransformationHandler("sizey-200-200"));
    }


    @Test
    public void testImageThumbnail() throws IOException {
        context.requestPathInfo().setSuffix("/not-a-command/size-200-200/crop-center.png");
        context.currentResource("/content/apache/sling-apache-org/index/apache.png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        transformer.transformFile(context.request(), baos);
        assertNotNull(baos);
        
        IOUtils.copy(new ByteArrayInputStream(baos.toByteArray()), new File("src/test/resources/thumbnail.png"));
    }

}
