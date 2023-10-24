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
package org.apache.sling.cms.reference.forms.impl.fields;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.lang.annotation.Annotation;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.cms.reference.forms.FormException;
import org.apache.sling.cms.reference.forms.impl.SlingContextHelper;
import org.apache.sling.cms.reference.forms.impl.fields.TextfieldHandler.Config;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

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
        handler = new TextfieldHandler(new Config() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String[] supportedTypes() {
                return new String[] { TextfieldHandler.DEFAULT_RESOURCE_TYPE };
            }

        });
    }

    @Test
    public void testDatefield() throws FormException {
        ResourceResolver resolver = context.resourceResolver();

        context.request()
                .setParameterMap(Collections.singletonMap("datefield", "2019-02-12"));

        Map<String, Object> formData = new HashMap<>();
        Resource fieldResource = resolver
                .getResource("/form/jcr:content/container/form/fields/fieldset/fields/datefield");
        handler.handleField(context.request(), fieldResource, formData);
        assertTrue(formData.get("datefield") instanceof Calendar);

        context.request().setParameterMap(Collections.singletonMap("datefield", "df-02-12"));

        try {
            handler.handleField(context.request(), fieldResource, formData);
            fail();
        } catch (FormException pe) {
            // expected
        }

        context.request()
                .setParameterMap(Collections.singletonMap("invaliddate", "2019-99-99"));

        Resource invalidType = resolver.getResource("/form/jcr:content/container/invalidtype");
        try {
            handler.handleField(context.request(), invalidType, formData);
            fail();
        } catch (FormException pe) {

        }

        Resource invalidDate = resolver.getResource("/form/jcr:content/container/invaliddate");
        try {
            handler.handleField(context.request(), invalidDate, formData);
            fail();
        } catch (FormException pe) {

        }

    }

    @Test
    public void testDouble() throws FormException {
        ResourceResolver resolver = context.resourceResolver();

        context.request().setParameterMap(Collections.singletonMap("double", "123.23"));

        Map<String, Object> formData = new HashMap<>();
        Resource fieldResource = resolver.getResource("/form/jcr:content/container/form/fields/fieldset/fields/double");
        handler.handleField(context.request(), fieldResource, formData);
        assertEquals(123.23, formData.get("double"));

        context.request().setParameterMap(Collections.singletonMap("double", "b"));

        try {
            handler.handleField(context.request(), fieldResource, formData);
            fail();
        } catch (FormException pe) {
            // expected
        }

        fieldResource = resolver
                .getResource("/form/jcr:content/container/form/fields/fieldset/fields/unvalidateddouble");
        try {
            handler.handleField(context.request(), fieldResource, formData);
            fail();
        } catch (FormException pe) {
            // expected
        }
    }

    @Test
    public void testFile() throws FormException {
        ResourceResolver resolver = context.resourceResolver();

        context.request().setParameterMap(Collections.singletonMap("file", new ByteArrayInputStream(new byte[0])));

        Map<String, Object> formData = new HashMap<>();
        Resource fieldResource = resolver.getResource("/form/jcr:content/container/form/fields/fieldset/fields/file");
        handler.handleField(context.request(), fieldResource, formData);
        assertNotNull(formData.get("file"));
        assertEquals(ByteArrayInputStream.class, formData.get("file").getClass());
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

        context.request().setParameterMap(Collections.singletonMap("integer", "123"));

        Map<String, Object> formData = new HashMap<>();
        Resource fieldResource = resolver
                .getResource("/form/jcr:content/container/form/fields/fieldset/fields/integer");
        handler.handleField(context.request(), fieldResource, formData);
        assertEquals(123, formData.get("integer"));

        context.request().setParameterMap(Collections.singletonMap("integer", "b"));
        try {
            handler.handleField(context.request(), fieldResource, formData);
            fail();
        } catch (FormException pe) {
            // expected
        }

        fieldResource = resolver
                .getResource("/form/jcr:content/container/form/fields/fieldset/fields/unvalidatedinteger");
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

        Map<String, Object> params = new HashMap<>();
        params.put("textfield", "Hello World");
        params.put("textfield2", "Hello World");
        context.request().setParameterMap(params);

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

        context.request().setParameterMap(Collections.singletonMap("patternfield", "123"));

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

        context.request().setParameterMap(Collections.singletonMap("money", "123"));

        Map<String, Object> formData = new HashMap<>();
        Resource fieldResource = resolver
                .getResource("/form/jcr:content/container/form/fields/fieldset/fields/requiredtextfield");
        handler.handleField(context.request(), fieldResource, formData);
        assertEquals(123, formData.get("money"));
    }
}
