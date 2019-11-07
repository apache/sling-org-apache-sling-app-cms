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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.util.IOUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.cms.transformer.OutputFileFormat;
import org.apache.sling.cms.transformer.Transformer;
import org.apache.sling.cms.transformer.helpers.SlingCMSContextHelper;
import org.apache.sling.cms.transformer.internal.models.TransformationImpl;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.apache.sling.testing.resourceresolver.MockResource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

public class TransformerImplTest {
    @Rule
    public final SlingContext context = new SlingContext();
    private Transformer transformer;

    @Before
    public void init() {
        SlingCMSContextHelper.initContext(context);
        transformer = new TransformerImpl();

        ((TransformerImpl) transformer).addTransformationHandler(new CropHandler() {
            public String getResourceType() {
                return "sling-cms/components/caconfig/transformationhandlers/crop";
            }
        });
        ((TransformerImpl) transformer).addTransformationHandler(new SizeHandler() {
            public String getResourceType() {
                return "sling-cms/components/caconfig/transformationhandlers/size";
            }
        });

        ((TransformerImpl) transformer).addThumbnailProvider(new ImageThumbnailProvider());
        ((TransformerImpl) transformer).addThumbnailProvider(new PdfThumbnailProvider());
    }

    @Test
    public void testImageThumbnail() throws IOException {
        context.currentResource("/content/apache/sling-apache-org/index/apache.png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TransformationImpl transformation = new TransformationImpl();
        List<Resource> handlers = new ArrayList<>();

        Map<String, Object> size = new HashMap<>();
        size.put(SizeHandler.PN_WIDTH, 200);
        size.put(SizeHandler.PN_HEIGHT, 200);
        size.put(ResourceResolver.PROPERTY_RESOURCE_TYPE, "sling-cms/components/caconfig/transformationhandlers/size");
        handlers.add(new MockResource("/conf", size, Mockito.mock(ResourceResolver.class)));

        Map<String, Object> crop = new HashMap<>();
        crop.put(CropHandler.PN_POSITION, "center");
        crop.put(ResourceResolver.PROPERTY_RESOURCE_TYPE, "sling-cms/components/caconfig/transformationhandlers/crop");
        handlers.add(new MockResource("/conf", crop, Mockito.mock(ResourceResolver.class)));

        transformation.sethandlers(handlers);
        transformer.transform(context.currentResource(), transformation, OutputFileFormat.PNG, baos);
        assertNotNull(baos);

        IOUtils.copy(new ByteArrayInputStream(baos.toByteArray()), new File("src/test/resources/thumbnail.png"));
    }

}
