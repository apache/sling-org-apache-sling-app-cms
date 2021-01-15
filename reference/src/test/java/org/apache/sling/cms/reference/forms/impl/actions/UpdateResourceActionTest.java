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
package org.apache.sling.cms.reference.forms.impl.actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;

import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ModifiableValueMapDecorator;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.cms.reference.forms.FormException;
import org.apache.sling.cms.reference.forms.FormRequest;
import org.junit.Test;
import org.mockito.Mockito;

public class UpdateResourceActionTest {

    @Test
    public void testHandles() {

        UpdateResourceAction ur = new UpdateResourceAction(null);

        Resource validResource = Mockito.mock(Resource.class);
        Mockito.when(validResource.getResourceType()).thenReturn("reference/components/forms/actions/updateresource");
        assertTrue(ur.handles(validResource));

        Resource invalidResource = Mockito.mock(Resource.class);
        Mockito.when(invalidResource.getResourceType())
                .thenReturn("reference/components/forms/actions/someotheraction");
        assertFalse(ur.handles(invalidResource));
    }

    @Test
    public void testActionResolver() throws FormException {

        UpdateResourceAction ur = new UpdateResourceAction(null);

        ModifiableValueMap contentData = new ModifiableValueMapDecorator(new HashMap<String, Object>());
        contentData.put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
        Resource contentResource = Mockito.mock(Resource.class);
        Mockito.when(contentResource.getValueMap()).thenReturn(contentData);
        Mockito.when(contentResource.adaptTo(Mockito.any())).thenReturn(contentData);

        ResourceResolver resolver = Mockito.mock(ResourceResolver.class);
        Mockito.when(resolver.getResource(Mockito.eq("/content/test"))).thenReturn(contentResource);

        ValueMap actionData = new ModifiableValueMapDecorator(new HashMap<String, Object>());
        actionData.put("allowedProperties", new String[] { "name1", "name3", "name4" });
        actionData.put("path", "/content/test");

        Resource actionResource = Mockito.mock(Resource.class);
        Mockito.when(actionResource.getValueMap()).thenReturn(actionData);
        Mockito.when(actionResource.getResourceResolver()).thenReturn(resolver);

        FormRequest formRequest = Mockito.mock(FormRequest.class);
        ValueMap formData = new ValueMapDecorator(new HashMap<>());
        formData.put("name2", "value2");
        formData.put("name3", "value3");
        Mockito.when(formRequest.getFormData()).thenReturn(formData);

        ur.handleForm(actionResource, formRequest);

        assertEquals(2, contentResource.getValueMap().keySet().size());
        assertEquals("value3", contentResource.getValueMap().get("name3"));
        assertFalse(contentResource.getValueMap().containsKey("name2"));

    }

    @Test
    public void testInvalidServiceUser() throws FormException, LoginException {

        LoginException le = new LoginException();
        ResourceResolverFactory factory = Mockito.mock(ResourceResolverFactory.class);
        Mockito.when(factory.getServiceResourceResolver(Mockito.anyMap())).thenThrow(le);

        UpdateResourceAction ur = new UpdateResourceAction(factory);

        ValueMap actionData = new ModifiableValueMapDecorator(new HashMap<String, Object>());
        actionData.put("allowedProperties", new String[] { "name1", "name3", "name4" });
        actionData.put("path", "/content/test");
        actionData.put("serviceUser", "bob");

        Resource actionResource = Mockito.mock(Resource.class);
        Mockito.when(actionResource.getValueMap()).thenReturn(actionData);

        FormRequest formRequest = Mockito.mock(FormRequest.class);
        ValueMap formData = new ValueMapDecorator(new HashMap<>());
        formData.put("name2", "value2");
        formData.put("name3", "value3");
        Mockito.when(formRequest.getFormData()).thenReturn(formData);

        try {
            ur.handleForm(actionResource, formRequest);
            fail();
        } catch (FormException fe) {
            assertEquals(le, fe.getCause());
        }
    }

    @Test
    public void testServiceUser() throws FormException, LoginException {

        ModifiableValueMap contentData = new ModifiableValueMapDecorator(new HashMap<String, Object>());
        contentData.put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
        Resource contentResource = Mockito.mock(Resource.class);
        Mockito.when(contentResource.getValueMap()).thenReturn(contentData);
        Mockito.when(contentResource.adaptTo(Mockito.any())).thenReturn(contentData);

        ResourceResolver resolver = Mockito.mock(ResourceResolver.class);
        Mockito.when(resolver.getResource(Mockito.eq("/content/test"))).thenReturn(contentResource);

        ResourceResolverFactory factory = Mockito.mock(ResourceResolverFactory.class);
        Mockito.when(factory.getServiceResourceResolver(Mockito.anyMap())).thenReturn(resolver);

        UpdateResourceAction ur = new UpdateResourceAction(factory);

        ValueMap actionData = new ModifiableValueMapDecorator(new HashMap<String, Object>());
        actionData.put("allowedProperties", new String[] { "name1", "name3", "name4" });
        actionData.put("path", "/content/test");
        actionData.put("serviceUser", "bob");

        Resource actionResource = Mockito.mock(Resource.class);
        Mockito.when(actionResource.getValueMap()).thenReturn(actionData);

        FormRequest formRequest = Mockito.mock(FormRequest.class);
        ValueMap formData = new ValueMapDecorator(new HashMap<>());
        formData.put("name2", "value2");
        formData.put("name3", "value3");
        Mockito.when(formRequest.getFormData()).thenReturn(formData);

        ur.handleForm(actionResource, formRequest);

        assertEquals(2, contentResource.getValueMap().keySet().size());
        assertEquals("value3", contentResource.getValueMap().get("name3"));
        assertFalse(contentResource.getValueMap().containsKey("name2"));

    }
}
