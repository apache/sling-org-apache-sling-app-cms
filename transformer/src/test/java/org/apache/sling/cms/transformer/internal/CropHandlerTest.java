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
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.resourceresolver.MockResource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;

public class CropHandlerTest {

    private Builder<? extends InputStream> builder;
    private CropHandler cropper;

    @Before
    public void init() {
        builder = Thumbnails.of(getClass().getClassLoader().getResourceAsStream("apache.png"));
        builder.size(200, 200);
        cropper = new CropHandler();
    }

    @Test
    public void testCrop() throws IOException {
        Resource config = new MockResource("/conf", Collections.singletonMap(CropHandler.PN_POSITION, "CENTER"),
                Mockito.mock(ResourceResolver.class));
        cropper.handle(builder, config);
        assertNotNull(builder.asBufferedImage());
    }

    @Test
    public void testCropLower() throws IOException {

        Resource config = new MockResource("/conf", Collections.singletonMap(CropHandler.PN_POSITION, "center"),
                Mockito.mock(ResourceResolver.class));
        cropper.handle(builder, config);
        assertNotNull(builder.asBufferedImage());
    }

    @Test
    public void testInvalidParam() throws IOException {
        try {

            Resource config = new MockResource("/conf", Collections.singletonMap(CropHandler.PN_POSITION, "centerz"),
                    Mockito.mock(ResourceResolver.class));
            cropper.handle(builder, config);
            fail();
        } catch (IOException e) {
        }
    }

}
