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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;

import org.apache.sling.cms.transformer.internal.SizeHandler;
import org.junit.Before;
import org.junit.Test;

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
    public void testApplies() {
        assertTrue(sizer.applies("size-200-200"));
    }

    @Test
    public void testResize() throws IOException {
        sizer.handle(builder, "size-200-200");
        assertNotNull(builder.asBufferedImage());
    }
    
    @Test
    public void testInvalidParam() throws IOException {
        try {
            sizer.handle(builder, "size-k-200");
            fail();
        }catch(IOException e) {
        }
        try {
            sizer.handle(builder, "size-222-h");
            fail();
        }catch(IOException e) {
        }
    }

}
