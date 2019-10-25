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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.cms.reference.form.impl.SlingContextHelper;
import org.apache.sling.cms.reference.forms.FormException;
import org.apache.sling.cms.reference.forms.impl.fields.TextfieldHandler;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class TextfieldHandlerTest {

    @Rule
    public final SlingContext context = new SlingContext();
    private TextfieldHandler handler;
    private ResourceResolver resolver;

    @Before
    public void init() {
        SlingContextHelper.initContext(context);
        context.request().setResource(context.resourceResolver().getResource("/form/jcr:content/container/form"));

        resolver = context.resourceResolver();
        handler = new TextfieldHandler();
    }

    @Test
    public void testDatefield() throws FormException {
        ResourceResolver resolver = context.resourceResolver();

        context.request()
                .setParameterMap(ImmutableMap.<String, Object>builder().put("datefield", "2019-02-12").build());

        Map<String, Object> formData = new HashMap<>();
        Resource fieldResource = resolver
                .getResource("/form/jcr:content/container/form/fields/fieldset/fields/datefield");
        handler.handleField(context.request(), fieldResource, formData);
        assertTrue(formData.get("datefield") instanceof Calendar);

        context.request().setParameterMap(ImmutableMap.<String, Object>builder().put("datefield", "df-02-12").build());

        try {
            handler.handleField(context.request(), fieldResource, formData);
            fail();
        } catch (FormException pe) {

        }

        Resource invalidDate = resolver.getResource("/form/jcr:content/container/invaliddate");
        context.request()
                .setParameterMap(ImmutableMap.<String, Object>builder().put("invalidate", "2019-02-12").build());
        try {
            handler.handleField(context.request(), invalidDate, formData);
            fail();
        } catch (FormException pe) {

        }

    }

    @Test
    public void testDouble() throws FormException {
        ResourceResolver resolver = context.resourceResolver();

        context.request().setParameterMap(ImmutableMap.<String, Object>builder().put("double", "123.23").build());

        Map<String, Object> formData = new HashMap<>();
        Resource fieldResource = resolver.getResource("/form/jcr:content/container/form/fields/fieldset/fields/double");
        handler.handleField(context.request(), fieldResource, formData);
        assertEquals(123.23, formData.get("double"));

        context.request().setParameterMap(ImmutableMap.<String, Object>builder().put("double", "b").build());

        try {
            handler.handleField(context.request(), fieldResource, formData);
            fail();
        } catch (FormException pe) {

        }

    }

    @Test
    public void testHandles() {

        assertTrue(handler
                .handles(resolver.getResource("/form/jcr:content/container/form/fields/fieldset/fields/textfield")));

        assertFalse(handler.handles(resolver.getResource("/form/jcr:content/container/form/fields/fieldset/fields")));

        assertFalse(handler
                .handles(resolver.getResource("/form/jcr:content/container/form/fields/fieldset/fields/textarea")));
    }

    @Test
    public void testInteger() throws FormException {
        ResourceResolver resolver = context.resourceResolver();

        context.request().setParameterMap(ImmutableMap.<String, Object>builder().put("integer", "123").build());

        Map<String, Object> formData = new HashMap<>();
        Resource fieldResource = resolver
                .getResource("/form/jcr:content/container/form/fields/fieldset/fields/integer");
        handler.handleField(context.request(), fieldResource, formData);
        assertEquals(123, formData.get("integer"));

        context.request().setParameterMap(ImmutableMap.<String, Object>builder().put("integer", "b").build());
        try {
            handler.handleField(context.request(), fieldResource, formData);
            fail();
        } catch (FormException pe) {

        }

    }

    @Test
    public void testNotRequiredNoValue() throws FormException {
        ResourceResolver resolver = context.resourceResolver();

        context.request().getParameterMap().put("textfield", new String[] {});

        Map<String, Object> formData = new HashMap<>();
        Resource fieldResource = resolver
                .getResource("/form/jcr:content/container/form/fields/fieldset/fields/textfield");
        handler.handleField(context.request(), fieldResource, formData);
        assertFalse(formData.containsKey("textfield"));
    }

    @Test
    public void testNotRequiredWithValue() throws FormException {
        ResourceResolver resolver = context.resourceResolver();

        context.request().setParameterMap(ImmutableMap.<String, Object>builder().put("textfield", "Hello World")
                .put("textfield2", "Hello World").build());

        Map<String, Object> formData = new HashMap<>();
        Resource fieldResource = resolver
                .getResource("/form/jcr:content/container/form/fields/fieldset/fields/textfield");
        handler.handleField(context.request(), fieldResource, formData);
        assertEquals("Hello World", formData.get("textfield"));
        assertFalse(formData.containsKey("textfield2"));
    }

    @Test
    public void testPatternfield() throws FormException {
        ResourceResolver resolver = context.resourceResolver();

        context.request().getParameterMap().put("patternfield", new String[] { "valasd" });

        Map<String, Object> formData = new HashMap<>();
        Resource fieldResource = resolver
                .getResource("/form/jcr:content/container/form/fields/fieldset/fields/patternfield");
        try {
            handler.handleField(context.request(), fieldResource, formData);
            fail();
        } catch (FormException fe) {

        }

        context.request().setParameterMap(ImmutableMap.<String, Object>builder().put("patternfield", "123").build());

        handler.handleField(context.request(), fieldResource, formData);
        assertEquals("123", formData.get("patternfield"));
    }

    @Test
    public void testRequiresNoValue() {
        ResourceResolver resolver = context.resourceResolver();

        context.request().getParameterMap().put("money", new String[0]);

        Map<String, Object> formData = new HashMap<>();
        Resource fieldResource = resolver
                .getResource("/form/jcr:content/container/form/fields/fieldset/fields/requiredtextfield");
        try {
            handler.handleField(context.request(), fieldResource, formData);
            fail();
        } catch (FormException fe) {

        }
    }

    @Test
    public void testRequiresWithValue() throws FormException {
        ResourceResolver resolver = context.resourceResolver();

        context.request().setParameterMap(ImmutableMap.<String, Object>builder().put("money", "123").build());

        Map<String, Object> formData = new HashMap<>();
        Resource fieldResource = resolver
                .getResource("/form/jcr:content/container/form/fields/fieldset/fields/requiredtextfield");
        handler.handleField(context.request(), fieldResource, formData);
        assertEquals(123, formData.get("money"));
    }
}
