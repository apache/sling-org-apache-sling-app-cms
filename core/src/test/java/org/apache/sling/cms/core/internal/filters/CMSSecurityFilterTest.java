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
package org.apache.sling.cms.core.internal.filters;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.cms.core.helpers.SlingCMSTestHelper;
import org.apache.sling.servlethelpers.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

public class CMSSecurityFilterTest {

    @Rule
    public SlingContext context = new SlingContext();
    private CMSSecurityFilter securityFilter;

    @Before
    public void init() throws UnsupportedRepositoryOperationException, RepositoryException, IOException {
        SlingCMSTestHelper.initAuthContext(context);
        securityFilter = new CMSSecurityFilter();
    }

    @Test
    public void testNotMatchingDomain() throws IOException, ServletException {
        context.request().setServerName("localhost");
        securityFilter.activate(new CMSSecurityFilterConfig() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String[] hostDomains() {
                return new String[] { "cms.danklco.com" };
            }

            @Override
            public String[] allowedPatterns() {
                return new String[0];
            }

            @Override
            public String group() {
                return null;
            }

        });
        securityFilter.doFilter(context.request(), context.response(), Mockito.mock(FilterChain.class));
        assertEquals(200, context.response().getStatus());
    }

    @Test
    public void testMatchingDomain() throws IOException, ServletException {
        ResourceResolver resolver = Mockito.mock(ResourceResolver.class);
        Mockito.when(resolver.getUserID()).thenReturn("anonymous");
        MockSlingHttpServletRequest request = new MockSlingHttpServletRequest(resolver);
        request.setServerName("cms.danklco.com");
        request.setServletPath("/content/apache/sling-apache/org.html");

        securityFilter.activate(new CMSSecurityFilterConfig() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String[] hostDomains() {
                return new String[] { "cms.danklco.com" };
            }

            @Override
            public String[] allowedPatterns() {
                return new String[0];
            }

            @Override
            public String group() {
                return null;
            }

        });
        securityFilter.doFilter(request, context.response(), Mockito.mock(FilterChain.class));
        assertEquals(401, context.response().getStatus());
    }

}