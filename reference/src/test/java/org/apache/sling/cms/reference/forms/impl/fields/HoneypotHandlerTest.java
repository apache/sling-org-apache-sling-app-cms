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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.cms.reference.forms.FormException;
import org.apache.sling.cms.reference.forms.impl.SlingContextHelper;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class HoneypotHandlerTest {

    @Rule
    public final SlingContext context = new SlingContext();
    private HoneypotHandler handler;
    private ResourceResolver resolver;

    @Before
    public void init() {
        SlingContextHelper.initContext(context);

        resolver = context.resourceResolver();
        handler = new HoneypotHandler();
    }

    @Test
    public void testHandles() {

        assertFalse(handler.handles(resolver.getResource("/form/jcr:content/container/form/fields/fieldset/fields")));

        assertTrue(handler
                .handles(resolver.getResource("/form/jcr:content/container/form/fields/fieldset/fields/honeypot")));
    }

    @Test
    public void testValid() throws FormException {
        ResourceResolver resolver = context.resourceResolver();

        context.request().setParameterMap(Collections.singletonMap("someothervalue", "something else"));

        Map<String, Object> formData = new HashMap<>();
        Resource fieldResource = resolver
                .getResource("/form/jcr:content/container/form/fields/fieldset/fields/honeypot");

        handler.handleField(context.request(), fieldResource, formData);

        assertTrue(formData.isEmpty());
    }

    @Test
    public void testInValid() throws FormException {
        ResourceResolver resolver = context.resourceResolver();

        context.request().setParameterMap(Collections.singletonMap("honeypot", "a value"));

        Map<String, Object> formData = new HashMap<>();
        Resource fieldResource = resolver
                .getResource("/form/jcr:content/container/form/fields/fieldset/fields/honeypot");

        try {

            handler.handleField(context.request(), fieldResource, formData);
            fail();
        } catch (FormException e) {
            // expected
        }

        assertTrue(formData.isEmpty());
    }

}
