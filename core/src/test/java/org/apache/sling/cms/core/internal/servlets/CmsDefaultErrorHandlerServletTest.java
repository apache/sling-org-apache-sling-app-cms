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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestDispatcherOptions;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.wrappers.SlingHttpServletRequestWrapper;
import org.apache.sling.cms.Site;
import org.apache.sling.cms.SiteManager;
import org.apache.sling.servlethelpers.MockRequestDispatcherFactory;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class CmsDefaultErrorHandlerServletTest {

    @Rule
    public final SlingContext context = new SlingContext();

    private CmsDefaultErrorHandlerServlet servlet;

    private ResourceResolverFactory factory;

    private MockRequestDispatcherFactory requestDispatcherFactory;

    private @NotNull MockSlingHttpServletResponse spiedResponse;

    private RequestDispatcher requestDispatcher;

    @Before
    public void before() {
        factory = mock(ResourceResolverFactory.class);
        servlet = new CmsDefaultErrorHandlerServlet(factory);

        requestDispatcherFactory = mock(MockRequestDispatcherFactory.class);
        context.request().setRequestDispatcherFactory(requestDispatcherFactory);

        requestDispatcher = mock(RequestDispatcher.class);
        when(requestDispatcherFactory.getRequestDispatcher(any(Resource.class), any(RequestDispatcherOptions.class)))
                .thenReturn(requestDispatcher);

        spiedResponse = spy(context.response());

    }

    @Test
    public void testDefault() throws IOException, ServletException {

        context.create().resource("/static/sling-cms/errorhandling/default");
        context.create()
                .resource("/static/sling-cms/errorhandling/default/jcr:content");

        servlet.service(context.request(), spiedResponse);

        verify(spiedResponse).reset();
        verify(spiedResponse).setContentType("text/html");
        verify(spiedResponse).setStatus(500);

        verify(requestDispatcherFactory).getRequestDispatcher(
                argThat((Resource res) -> "/static/sling-cms/errorhandling/default/jcr:content".equals(res.getPath())),
                any(RequestDispatcherOptions.class));
        verify(requestDispatcher).include(any(SlingHttpServletRequestWrapper.class),
                any(SlingHttpServletResponse.class));
    }

    @Test
    public void testErrorCodeSet() throws IOException, ServletException {

        context.create().resource("/static/sling-cms/errorhandling/403");
        context.create()
                .resource("/static/sling-cms/errorhandling/403/jcr:content");

        context.request().setAttribute(SlingConstants.ERROR_STATUS, 403);

        servlet.service(context.request(), spiedResponse);

        verify(spiedResponse).reset();
        verify(spiedResponse).setContentType("text/html");
        verify(spiedResponse).setStatus(403);

        verify(requestDispatcherFactory).getRequestDispatcher(
                argThat((Resource res) -> "/static/sling-cms/errorhandling/403/jcr:content".equals(res.getPath())),
                any(RequestDispatcherOptions.class));
        verify(requestDispatcher).include(any(SlingHttpServletRequestWrapper.class),
                any(SlingHttpServletResponse.class));
    }

    @Test
    public void testSiteErrorPage() throws IOException, ServletException {

        Resource siteResource = context.create().resource("/content/site");
        context.create().resource("/content/site/errors/default");
        context.create()
                .resource("/content/site/errors/default/jcr:content");

        context.request().setAttribute(SlingConstants.ERROR_STATUS, 403);
        Resource resource = mock(Resource.class);
        context.request().setResource(resource);

        SiteManager siteManager = mock(SiteManager.class);
        Site site = mock(Site.class);
        when(site.getResource()).thenReturn(siteResource);
        when(site.getPath()).thenReturn("/content/site");
        when(siteManager.getSite()).thenReturn(site);
        when(resource.adaptTo(SiteManager.class)).thenReturn(siteManager);

        servlet.service(context.request(), spiedResponse);

        verify(spiedResponse).reset();
        verify(spiedResponse).setContentType("text/html");
        verify(spiedResponse).setStatus(403);

        verify(requestDispatcherFactory).getRequestDispatcher(
                argThat((Resource res) -> "/content/site/errors/default/jcr:content".equals(res.getPath())),
                any(RequestDispatcherOptions.class));
        verify(requestDispatcher).include(any(SlingHttpServletRequestWrapper.class),
                any(SlingHttpServletResponse.class));
    }


    @Test
    public void testSiteNoErrorPages() throws IOException, ServletException {

        Resource siteResource = context.create().resource("/content/site");
        context.create().resource("/static/sling-cms/errorhandling/403");
        context.create()
                .resource("/static/sling-cms/errorhandling/403/jcr:content");

        context.request().setAttribute(SlingConstants.ERROR_STATUS, 403);
        Resource resource = mock(Resource.class);
        context.request().setResource(resource);

        SiteManager siteManager = mock(SiteManager.class);
        Site site = mock(Site.class);
        when(site.getResource()).thenReturn(siteResource);
        when(site.getPath()).thenReturn("/content/site");
        when(siteManager.getSite()).thenReturn(site);
        when(resource.adaptTo(SiteManager.class)).thenReturn(siteManager);

        servlet.service(context.request(), spiedResponse);

        verify(spiedResponse).reset();
        verify(spiedResponse).setContentType("text/html");
        verify(spiedResponse).setStatus(403);

        verify(requestDispatcherFactory).getRequestDispatcher(
                argThat((Resource res) -> "/static/sling-cms/errorhandling/403/jcr:content".equals(res.getPath())),
                any(RequestDispatcherOptions.class));
        verify(requestDispatcher).include(any(SlingHttpServletRequestWrapper.class),
                any(SlingHttpServletResponse.class));
    }

}
