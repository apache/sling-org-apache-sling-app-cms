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
import org.apache.sling.cms.reference.forms.impl.fields.TextareaHandler;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class TextareaHandlerTest {

    @Rule
    public final SlingContext context = new SlingContext();
    private TextareaHandler handler;
    private ResourceResolver resolver;

    @Before
    public void init() {
        SlingContextHelper.initContext(context);
        context.request().setResource(context.resourceResolver().getResource("/form/jcr:content/container/form"));

        resolver = context.resourceResolver();
        handler = new TextareaHandler();
    }

    @Test
    public void testHandles() {

        assertTrue(handler
                .handles(resolver.getResource("/form/jcr:content/container/form/fields/fieldset/fields/textarea")));

        assertFalse(handler.handles(resolver.getResource("/form/jcr:content/container/form/fields/fieldset/fields")));

        assertFalse(handler
                .handles(resolver.getResource("/form/jcr:content/container/form/fields/fieldset/fields/textfield")));
    }

    @Test
    public void testNotRequiredNoValue() throws FormException {
        ResourceResolver resolver = context.resourceResolver();

        context.request().getParameterMap().put("textarea", new String[] {});

        Map<String, Object> formData = new HashMap<>();
        Resource fieldResource = resolver
                .getResource("/form/jcr:content/container/form/fields/fieldset/fields/textarea");
        handler.handleField(context.request(), fieldResource, formData);
        assertFalse(formData.containsKey("textarea"));
    }

    @Test
    public void testNotRequiredWithValue() throws FormException {
        ResourceResolver resolver = context.resourceResolver();

        context.request()
                .setParameterMap(ImmutableMap.<String, Object>builder().put("textarea", "Hello World").build());

        Map<String, Object> formData = new HashMap<>();
        Resource fieldResource = resolver
                .getResource("/form/jcr:content/container/form/fields/fieldset/fields/textarea");
        handler.handleField(context.request(), fieldResource, formData);
        assertEquals("Hello World", formData.get("textarea"));
    }

    @Test
    public void testRequiresNoValue() {
        ResourceResolver resolver = context.resourceResolver();

        context.request().getParameterMap().put("textarea", new String[0]);

        Map<String, Object> formData = new HashMap<>();
        Resource fieldResource = resolver
                .getResource("/form/jcr:content/container/form/fields/fieldset/fields/requiredtextarea");
        try {
            handler.handleField(context.request(), fieldResource, formData);
            fail();
        } catch (FormException fe) {

        }
    }

    @Test
    public void testRequiresWithValue() throws FormException {
        ResourceResolver resolver = context.resourceResolver();

        context.request()
                .setParameterMap(ImmutableMap.<String, Object>builder().put("requiredtextarea", "Hello World").build());

        Map<String, Object> formData = new HashMap<>();
        Resource fieldResource = resolver
                .getResource("/form/jcr:content/container/form/fields/fieldset/fields/requiredtextarea");
        handler.handleField(context.request(), fieldResource, formData);
        assertEquals("Hello World", formData.get("requiredtextarea"));
    }
}
