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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.cms.transformer.helpers.SlingCMSContextHelper;
import org.apache.sling.cms.transformer.internal.models.TransformationImpl;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.apache.sling.testing.resourceresolver.MockResource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransformServletTest {

    private static final Logger log = LoggerFactory.getLogger(TransformServletTest.class);

    private TransformServlet ts = new TransformServlet();

    @Rule
    public final SlingContext context = new SlingContext();

    @Before
    public void init() throws IllegalAccessException, LoginException {
        SlingCMSContextHelper.initContext(context);

        ResourceResolverFactory factory = Mockito.mock(ResourceResolverFactory.class);
        ResourceResolver resolver = Mockito.mock(ResourceResolver.class);

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

        Mockito.when(resolver.findResources(Mockito.anyString(), Mockito.anyString())).thenAnswer((ans) -> {
            List<Resource> resources = new ArrayList<>();
            if (ans.getArgument(0, String.class).contains("test'")) {
                Resource resource = Mockito.mock(Resource.class);
                Mockito.when(resource.adaptTo(Mockito.any())).thenReturn(transformation);
                resources.add(resource);
            }
            return resources.iterator();
        });

        Mockito.when(factory.getServiceResourceResolver(Mockito.any())).thenReturn(resolver);
        TransformationServiceUser tsu = new TransformationServiceUser();
        tsu.setResolverFactory(factory);
        ts.setTransformationServiceUser(tsu);

        TransformerImpl transformer = new TransformerImpl();
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

        ts.setTransformer(transformer);

    }

    @Test
    public void testValid() throws IOException, ServletException {
        log.info("testContentTypes");

        context.currentResource("/content/apache/sling-apache-org/index/apache.png");
        context.requestPathInfo().setSuffix("/test.png");
        context.requestPathInfo().setExtension("transform");

        ts.doGet(context.request(), context.response());

        assertNotNull(context.response().getOutput());
    }

    @Test
    public void testInvalid() throws IOException, ServletException {
        log.info("testContentTypes");

        context.currentResource("/content/apache/sling-apache-org/index/apache.png");
        context.requestPathInfo().setSuffix("/test2.png");
        context.requestPathInfo().setExtension("transform");

        ts.doGet(context.request(), context.response());

        assertEquals(400, context.response().getStatus());
    }

}
