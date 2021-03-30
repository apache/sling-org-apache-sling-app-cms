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
package org.apache.sling.cms.core.internal.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.cms.core.insights.impl.FakeRequest;
import org.apache.sling.engine.SlingRequestProcessor;
import org.apache.sling.engine.impl.request.SlingRequestPathInfo;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

public class PreviewFileServletTest {

    @Test
    public void testServlet() throws ServletException, IOException {

        String expectedPath = "/test";
        SlingRequestProcessor processor = Mockito.mock(SlingRequestProcessor.class);
        PreviewFileServlet pfs = new PreviewFileServlet(processor);

        ResourceResolver resolver = Mockito.mock(ResourceResolver.class);
        SlingHttpServletRequest slingRequest = Mockito.mock(SlingHttpServletRequest.class);
        Mockito.when(slingRequest.getResourceResolver()).thenReturn(resolver);
        SlingRequestPathInfo pathInfo = Mockito.mock(SlingRequestPathInfo.class);
        Mockito.when(pathInfo.getSuffix()).thenReturn(expectedPath);
        Mockito.when(slingRequest.getRequestPathInfo()).thenReturn(pathInfo);

        SlingHttpServletResponse slingResponse = Mockito.mock(SlingHttpServletResponse.class);

        pfs.doGet(slingRequest, slingResponse);

        Mockito.verify(processor).processRequest(Mockito.argThat(new ArgumentMatcher<HttpServletRequest>() {

            @Override
            public boolean matches(HttpServletRequest arg) {

                return arg instanceof FakeRequest && expectedPath.equals(arg.getRequestURI());
            }

        }), Mockito.eq(slingResponse), Mockito.eq(resolver));

    }
}
