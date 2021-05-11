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
package org.apache.sling.cms.reference.forms.impl;

import java.lang.annotation.Annotation;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.SlingRequestEvent;
import org.apache.sling.api.request.SlingRequestListener;
import org.apache.sling.api.request.SlingRequestEvent.EventType;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.cms.reference.SearchService;
import org.apache.sling.cms.reference.impl.SearchServiceImpl;
import org.apache.sling.cms.reference.impl.SearchServiceImpl.Config;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SearchServiceTest {

    private ResourceResolverFactory factory;
    private SlingHttpServletRequest request;
    private ResourceResolver resolver;

    @Before
    public void init() {
        factory = Mockito.mock(ResourceResolverFactory.class);
        request = Mockito.mock(SlingHttpServletRequest.class);
        resolver = Mockito.mock(ResourceResolver.class);
    }

    @Test
    public void testNoConfig() {
        SearchService search = new SearchServiceImpl(factory, new ConfigImpl(null));
        search.getResourceResolver(request);
        Mockito.verify(request).getResourceResolver();
    }

    @Test
    public void testWithConfig() throws LoginException {
        SearchService search = new SearchServiceImpl(factory, new ConfigImpl("hello-world"));
        search.getResourceResolver(request);
        Mockito.verify(factory).getServiceResourceResolver(Mockito.anyMap());
    }

    @Test
    public void testInvalidUser() throws LoginException {
        SearchService search = new SearchServiceImpl(factory, new ConfigImpl("invalid"));
        Mockito.when(factory.getServiceResourceResolver(Mockito.anyMap())).thenThrow(new LoginException("Bad user"));
        search.getResourceResolver(request);
        Mockito.verify(request).getResourceResolver();
    }

    @Test
    public void testRequestListener() throws LoginException {
        String name = SearchServiceImpl.class.getName() + ":ResourceResolver";
        SlingRequestListener listener = new SearchServiceImpl(factory, new ConfigImpl("invalid"));

        listener.onEvent(new SlingRequestEvent(null, request, EventType.EVENT_DESTROY));
        Mockito.verify(resolver, Mockito.never()).close();

        Mockito.when(request.getAttribute(name)).thenReturn(null);
        listener.onEvent(new SlingRequestEvent(null, request, EventType.EVENT_DESTROY));
        Mockito.verify(resolver, Mockito.never()).close();

        listener.onEvent(new SlingRequestEvent(null, request, EventType.EVENT_INIT));
        Mockito.verify(resolver, Mockito.never()).close();

        Mockito.when(request.getAttribute(name)).thenReturn(resolver);
        listener.onEvent(new SlingRequestEvent(null, request, EventType.EVENT_DESTROY));
        Mockito.verify(resolver).close();
    }

}

class ConfigImpl implements Config {
    private final String name;

    ConfigImpl(String name) {
        this.name = name;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }

    @Override
    public String searchServiceUsername() {
        return name;
    }

}