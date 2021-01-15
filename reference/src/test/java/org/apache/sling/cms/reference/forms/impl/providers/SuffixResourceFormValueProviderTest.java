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
package org.apache.sling.cms.reference.forms.impl.providers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.resourceresolver.MockResource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SuffixResourceFormValueProviderTest {

    public Resource providerResource;

    @Before
    public void init() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("basePath", "/content/apath");
        properties.put("allowedProperties", new String[] { "name1", "name3", "name4" });

        providerResource = new MockResource("/apps/rpv", properties, null);

    }

    @Test
    public void testAccepts() {
        SuffixResourceFormValueProvider sr = new SuffixResourceFormValueProvider();

        Resource validResource = Mockito.mock(Resource.class);
        Mockito.when(validResource.getResourceType()).thenReturn("reference/components/forms/providers/suffixresource");
        assertTrue(sr.handles(validResource));

        Resource invalidResource = Mockito.mock(Resource.class);
        Mockito.when(invalidResource.getResourceType())
                .thenReturn("reference/components/forms/providers/someotherprovider");
        assertFalse(sr.handles(invalidResource));
    }

    @Test
    public void testNoSuffix() {
        SuffixResourceFormValueProvider sr = new SuffixResourceFormValueProvider();

        Map<String, Object> formData = new HashMap<>();

        SlingHttpServletRequest mockRequest = Mockito.mock(SlingHttpServletRequest.class);
        RequestPathInfo rpi = Mockito.mock(RequestPathInfo.class);
        Mockito.when(mockRequest.getRequestPathInfo()).thenReturn(rpi);
        sr.loadValues(mockRequest, providerResource, formData);

        assertEquals(0, formData.entrySet().size());
        Mockito.verify(rpi).getSuffixResource();

    }

    @Test
    public void testValidSuffix() {
        SuffixResourceFormValueProvider sr = new SuffixResourceFormValueProvider();

        Map<String, Object> formData = new HashMap<>();

        Map<String, Object> properties = new HashMap<>();
        properties.put("name1", "value1");
        properties.put("name2", "value2");

        Resource suffixResource = new MockResource("/content/apath/apage", properties, null);

        SlingHttpServletRequest mockRequest = Mockito.mock(SlingHttpServletRequest.class);
        RequestPathInfo rpi = Mockito.mock(RequestPathInfo.class);
        Mockito.when(rpi.getSuffixResource()).thenReturn(suffixResource);
        Mockito.when(mockRequest.getRequestPathInfo()).thenReturn(rpi);
        sr.loadValues(mockRequest, providerResource, formData);

        assertEquals(2, formData.entrySet().size());
        assertEquals("value1", formData.get("name1"));
        assertEquals("/content/apath/apage", formData.get("suffixResource"));
        Mockito.verify(rpi).getSuffixResource();

    }

    @Test
    public void testAllAllowed() {
        SuffixResourceFormValueProvider sr = new SuffixResourceFormValueProvider();

        Map<String, Object> formData = new HashMap<>();

        Map<String, Object> properties = new HashMap<>();
        properties.put("name1", "value1");
        properties.put("name2", "value2");

        Map<String, Object> providerProperties = new HashMap<>();
        providerProperties.put("basePath", "/content/apath");

        Resource pr2 = new MockResource("/apps/rpv", providerProperties, null);

        Resource suffixResource = new MockResource("/content/apath/apage", properties, null);

        SlingHttpServletRequest mockRequest = Mockito.mock(SlingHttpServletRequest.class);
        RequestPathInfo rpi = Mockito.mock(RequestPathInfo.class);
        Mockito.when(rpi.getSuffixResource()).thenReturn(suffixResource);
        Mockito.when(mockRequest.getRequestPathInfo()).thenReturn(rpi);
        sr.loadValues(mockRequest, pr2, formData);

        assertEquals(3, formData.entrySet().size());
        assertEquals("value1", formData.get("name1"));
        assertEquals("value2", formData.get("name2"));
        assertEquals("/content/apath/apage", formData.get("suffixResource"));
        Mockito.verify(rpi).getSuffixResource();

    }

    @Test
    public void testInvalidSuffix() {
        SuffixResourceFormValueProvider sr = new SuffixResourceFormValueProvider();

        Map<String, Object> formData = new HashMap<>();

        Map<String, Object> properties = new HashMap<>();
        properties.put("name1", "value1");
        properties.put("name2", "value2");

        Resource suffixResource = new MockResource("/content/anotherpath", properties, null);

        SlingHttpServletRequest mockRequest = Mockito.mock(SlingHttpServletRequest.class);
        RequestPathInfo rpi = Mockito.mock(RequestPathInfo.class);
        Mockito.when(rpi.getSuffixResource()).thenReturn(suffixResource);
        Mockito.when(mockRequest.getRequestPathInfo()).thenReturn(rpi);
        sr.loadValues(mockRequest, providerResource, formData);

        assertEquals(0, formData.entrySet().size());
        Mockito.verify(rpi).getSuffixResource();

    }
}
