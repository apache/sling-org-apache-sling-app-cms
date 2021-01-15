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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.servlethelpers.MockSlingHttpServletRequest;
import org.apache.sling.testing.resourceresolver.MockResource;
import org.junit.Test;
import org.mockito.Mockito;

public class RequestParametersValueProviderTest {

    @Test
    public void testAccepts() {
        RequestParametersValueProvider rpp = new RequestParametersValueProvider();

        Resource validResource = Mockito.mock(Resource.class);
        Mockito.when(validResource.getResourceType())
                .thenReturn("reference/components/forms/providers/requestparameters");
        assertTrue(rpp.handles(validResource));

        Resource invalidResource = Mockito.mock(Resource.class);
        Mockito.when(invalidResource.getResourceType())
                .thenReturn("reference/components/forms/providers/someotherprovider");
        assertFalse(rpp.handles(invalidResource));
    }

    @Test
    public void testRequestParameters() {
        RequestParametersValueProvider rpp = new RequestParametersValueProvider();

        MockResource mockResource = new MockResource("/apps/rpv",
                Collections.singletonMap("allowedParameters", new String[] { "name1", "name3", "name4" }), null);

        MockSlingHttpServletRequest mockRequest = new MockSlingHttpServletRequest(null);
        mockRequest.addRequestParameter("name1", "value1");
        mockRequest.addRequestParameter("name2", "value2");
        mockRequest.addRequestParameter("name4", "value1");
        mockRequest.addRequestParameter("name4", "value2");

        Map<String, Object> formData = new HashMap<>();
        rpp.loadValues(mockRequest, mockResource, formData);

        assertTrue(formData.containsKey("name1"));
        assertEquals("value1", formData.get("name1"));

        assertFalse(formData.containsKey("name2"));
        assertFalse(formData.containsKey("name3"));

        assertTrue(formData.containsKey("name1"));
        assertTrue(Arrays.equals(new String[] { "value1", "value2" }, (String[]) formData.get("name4")));
    }
}
