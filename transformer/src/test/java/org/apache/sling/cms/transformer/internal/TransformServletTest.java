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
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.sling.cms.transformer.ThumbnailProvider;
import org.apache.sling.cms.transformer.TransformationHandler;
import org.apache.sling.cms.transformer.helpers.SlingCMSContextHelper;
import org.apache.sling.cms.transformer.internal.CropHandler;
import org.apache.sling.cms.transformer.internal.FileThumbnailTransformerImpl;
import org.apache.sling.cms.transformer.internal.ImageThumbnailProvider;
import org.apache.sling.cms.transformer.internal.PdfThumbnailProvider;
import org.apache.sling.cms.transformer.internal.SizeHandler;
import org.apache.sling.cms.transformer.internal.TransformServlet;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class TransformServletTest {

    private static final Logger log = LoggerFactory.getLogger(TransformServletTest.class);
    
    private TransformServlet ts = new TransformServlet();;

    @Rule
    public final SlingContext context = new SlingContext();

    @Before
    public void init() throws IllegalAccessException {
        SlingCMSContextHelper.initContext(context);

        FileThumbnailTransformerImpl transformer = new FileThumbnailTransformerImpl();
        transformer.setHandlers(Lists.asList(new CropHandler(), new TransformationHandler[] { new SizeHandler() }));
        transformer.setThumbnailProviders(
                Lists.asList(new ImageThumbnailProvider(), new ThumbnailProvider[] { new PdfThumbnailProvider() }));
        
        FieldUtils.writeDeclaredField(ts,"transformer", transformer, true);
    }

    @Test
    public void testValid() throws IOException, ServletException {
        log.info("testContentTypes");

        context.currentResource("/content/apache/sling-apache-org/index/apache.png");
        context.requestPathInfo().setSuffix("/size-200-200/crop-center.png");
        context.requestPathInfo().setExtension("transform");

        ts.doGet(context.request(), context.response());

        byte[] thumb = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("thumbnail.png"));
        assertArrayEquals(thumb, context.response().getOutput());
    }

    @Test
    public void testInvalid() throws IOException, ServletException {
        log.info("testContentTypes");

        context.currentResource("/content/apache/sling-apache-org/index/apache.png");
        context.requestPathInfo().setSuffix("/size-200-200/crop-left.png");
        context.requestPathInfo().setExtension("transform");

        ts.doGet(context.request(), context.response());

        assertEquals(400, context.response().getStatus());
    }

}
