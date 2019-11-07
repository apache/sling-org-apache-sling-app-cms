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
import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.resourceresolver.MockResource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;

public class SizeHandlerTest {

    private Builder<? extends InputStream> builder;
    private SizeHandler sizer;

    @Before
    public void init() {
        builder = Thumbnails.of(getClass().getClassLoader().getResourceAsStream("apache.png"));
        sizer = new SizeHandler();
    }

    @Test
    public void testResize() throws IOException {

        Map<String, Object> config = new HashMap<>();
        config.put(SizeHandler.PN_WIDTH, 200);
        config.put(SizeHandler.PN_HEIGHT, 200);
        sizer.handle(builder, new MockResource("/conf", config, Mockito.mock(ResourceResolver.class)));
        assertNotNull(builder.asBufferedImage());
    }

    @Test
    public void testInvalidWidth() throws IOException {
        try {
            Map<String, Object> config = new HashMap<>();
            config.put(SizeHandler.PN_WIDTH, "K");
            config.put(SizeHandler.PN_HEIGHT, 200);
            sizer.handle(builder, new MockResource("/conf", config, Mockito.mock(ResourceResolver.class)));
            fail();
        } catch (IOException | IllegalArgumentException e) {
        }
    }

    @Test
    public void testInvalidHeight() throws IOException {
        try {
            Map<String, Object> config = new HashMap<>();
            config.put(SizeHandler.PN_WIDTH, 200);
            config.put(SizeHandler.PN_HEIGHT, "h");
            sizer.handle(builder, new MockResource("/conf", config, Mockito.mock(ResourceResolver.class)));
            fail();
        } catch (IOException | IllegalArgumentException e) {
        }
    }

}
