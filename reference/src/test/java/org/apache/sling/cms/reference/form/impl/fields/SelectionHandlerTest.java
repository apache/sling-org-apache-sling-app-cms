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
package org.apache.sling.cms.reference.form.impl.fields;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.cms.reference.form.impl.SlingContextHelper;
import org.apache.sling.cms.reference.forms.FormException;
import org.apache.sling.cms.reference.forms.impl.fields.SelectionHandler;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class SelectionHandlerTest {

    @Rule
    public final SlingContext context = new SlingContext();
    private SelectionHandler handler;
    private ResourceResolver resolver;

    @Before
    public void init() {
        SlingContextHelper.initContext(context);
        context.request().setResource(context.resourceResolver().getResource("/form/jcr:content/container/form"));

        resolver = context.resourceResolver();
        handler = new SelectionHandler();
    }

    @Test
    public void testHandles() {

        assertTrue(handler
                .handles(resolver.getResource("/form/jcr:content/container/form/fields/fieldset/fields/singleselect")));

        assertFalse(handler.handles(resolver.getResource("/form/jcr:content/container/form/fields/fieldset/fields")));

        assertTrue(handler
                .handles(resolver.getResource("/form/jcr:content/container/form/fields/fieldset/fields/multiselect")));
    }

    @Test
    public void testSingleSelect() throws FormException {
        ResourceResolver resolver = context.resourceResolver();

        context.request()
                .setParameterMap(ImmutableMap.<String, Object>builder().put("singleselect", "Hello World").build());

        Map<String, Object> formData = new HashMap<>();
        Resource fieldResource = resolver
                .getResource("/form/jcr:content/container/form/fields/fieldset/fields/singleselect");

        handler.handleField(context.request(), fieldResource, formData);

        assertEquals("Hello World", formData.get("singleselect"));
    }

    @Test
    public void testMissingSingleSelect() throws FormException {
        ResourceResolver resolver = context.resourceResolver();

        context.request().getParameterMap().put("singleselect", new String[] {});

        Map<String, Object> formData = new HashMap<>();
        Resource fieldResource = resolver
                .getResource("/form/jcr:content/container/form/fields/fieldset/fields/singleselect");
        try {
            handler.handleField(context.request(), fieldResource, formData);
            fail();
        } catch (Exception e) {
        }
        assertFalse(formData.containsKey("singleselect"));
    }

    @Test
    public void testMultipleSelect() throws FormException {
        ResourceResolver resolver = context.resourceResolver();

        context.request().setParameterMap(ImmutableMap.<String, Object>builder().put("multiselect", "").build());

        Map<String, Object> formData = new HashMap<>();
        Resource fieldResource = resolver
                .getResource("/form/jcr:content/container/form/fields/fieldset/fields/multiselect");
        handler.handleField(context.request(), fieldResource, formData);
        assertFalse(formData.containsKey("multiselect"));

        context.request().setParameterMap(ImmutableMap.<String, Object>builder()
                .put("multiselect", new String[] { "Thing 1", "Thing 2" }).build());
        handler.handleField(context.request(), fieldResource, formData);
        assertTrue(formData.containsKey("multiselect"));
    }

}
