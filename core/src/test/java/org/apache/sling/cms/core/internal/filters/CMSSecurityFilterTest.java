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

import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.PublishableResource;
import org.apache.sling.cms.core.helpers.SlingCMSTestHelper;
import org.apache.sling.cms.publication.PUBLICATION_MODE;
import org.apache.sling.cms.publication.PublicationManagerFactory;
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
    }

    @Test
    public void testNotEnabled() throws IOException, ServletException {

        PublicationManagerFactory factory = Mockito.mock(PublicationManagerFactory.class);
        Mockito.when(factory.getPublicationMode()).thenReturn(PUBLICATION_MODE.CONTENT_DISTRIBUTION);

        context.registerService(PublicationManagerFactory.class, factory);

        securityFilter = context.registerInjectActivateService(new CMSSecurityFilter());

        securityFilter.doFilter(context.request(), context.response(), Mockito.mock(FilterChain.class));
        assertEquals(200, context.response().getStatus());
    }

    @Test
    public void testNotMatchingDomain() throws IOException, ServletException {

        PublicationManagerFactory factory = Mockito.mock(PublicationManagerFactory.class);
        Mockito.when(factory.getPublicationMode()).thenReturn(PUBLICATION_MODE.STANDALONE);
        context.registerService(PublicationManagerFactory.class, factory);

        CMSSecurityConfigInstance config = new CMSSecurityConfigInstance();
        config.activate(new CMSSecurityFilterConfig() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String[] hostDomains() {
                return new String[] { "cms.apache.org" };
            }

            @Override
            public String[] allowedPatterns() {
                return null;
            }

            @Override
            public String group() {
                return null;
            }

        });
        context.registerService(CMSSecurityConfigInstance.class, config);

        securityFilter = context.registerInjectActivateService(new CMSSecurityFilter());

        context.request().setRemoteHost("www.apache.org");
        securityFilter.doFilter(context.request(), context.response(), Mockito.mock(FilterChain.class));
        assertEquals(200, context.response().getStatus());
    }

    @Test
    public void testAllowedPath() throws IOException, ServletException {

        PublicationManagerFactory factory = Mockito.mock(PublicationManagerFactory.class);
        Mockito.when(factory.getPublicationMode()).thenReturn(PUBLICATION_MODE.STANDALONE);
        context.registerService(PublicationManagerFactory.class, factory);

        CMSSecurityConfigInstance config = new CMSSecurityConfigInstance();
        config.activate(new CMSSecurityFilterConfig() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String[] hostDomains() {
                return new String[] { "cms.apache.org" };
            }

            @Override
            public String[] allowedPatterns() {
                return new String[] { "\\/static\\/.*" };
            }

            @Override
            public String group() {
                return null;
            }

        });
        context.registerService(CMSSecurityConfigInstance.class, config);

        securityFilter = context.registerInjectActivateService(new CMSSecurityFilter());

        context.request().setRemoteHost("cms.apache.org");
        context.request().setServletPath("/static/test1.txt");

        securityFilter.doFilter(context.request(), context.response(), Mockito.mock(FilterChain.class));
        assertEquals(200, context.response().getStatus());
    }

    @Test
    public void testPublished() throws IOException, ServletException {

        PublicationManagerFactory factory = Mockito.mock(PublicationManagerFactory.class);
        Mockito.when(factory.getPublicationMode()).thenReturn(PUBLICATION_MODE.STANDALONE);
        context.registerService(PublicationManagerFactory.class, factory);

        CMSSecurityConfigInstance config = new CMSSecurityConfigInstance();
        config.activate(new CMSSecurityFilterConfig() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String[] hostDomains() {
                return new String[] { "cms.apache.org" };
            }

            @Override
            public String[] allowedPatterns() {
                return new String[] { "\\/static\\/.*" };
            }

            @Override
            public String group() {
                return null;
            }

        });
        context.registerService(CMSSecurityConfigInstance.class, config);

        securityFilter = context.registerInjectActivateService(new CMSSecurityFilter());

        context.request().setRemoteHost("cms.apache.org");
        context.request().setServletPath("/content/test1.txt");

        PublishableResource published = Mockito.mock(PublishableResource.class);
        Mockito.when(published.isPublished()).thenReturn(true);
        context.registerAdapter(Resource.class, PublishableResource.class, published);

        securityFilter.doFilter(context.request(), context.response(), Mockito.mock(FilterChain.class));
        assertEquals(200, context.response().getStatus());
    }
}