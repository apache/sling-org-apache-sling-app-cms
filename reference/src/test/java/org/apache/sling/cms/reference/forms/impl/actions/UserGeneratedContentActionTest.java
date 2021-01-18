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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ModifiableValueMapDecorator;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.cms.NameFilter;
import org.apache.sling.cms.reference.forms.FormException;
import org.apache.sling.cms.reference.forms.FormRequest;
import org.apache.sling.cms.usergenerated.UserGeneratedContentService;
import org.junit.Test;
import org.mockito.Mockito;

public class UserGeneratedContentActionTest {

    @Test
    public void testHandles() {

        NameFilter filter = Mockito.mock(NameFilter.class);
        UserGeneratedContentService ugcService = Mockito.mock(UserGeneratedContentService.class);

        UserGeneratedContentAction ur = new UserGeneratedContentAction(filter, ugcService);

        Resource validResource = Mockito.mock(Resource.class);
        Mockito.when(validResource.getResourceType())
                .thenReturn("reference/components/forms/actions/usergeneratedcontent");
        assertTrue(ur.handles(validResource));

        Resource invalidResource = Mockito.mock(Resource.class);
        Mockito.when(invalidResource.getResourceType())
                .thenReturn("reference/components/forms/actions/someotheraction");
        assertFalse(ur.handles(invalidResource));
    }

    @Test
    public void testActionResolver() throws FormException, PersistenceException {

        NameFilter filter = Mockito.mock(NameFilter.class);
        ResourceResolver resolver = Mockito.mock(ResourceResolver.class);

        Resource container = Mockito.mock(Resource.class);
        Mockito.when(container.getResourceResolver()).thenReturn(resolver);

        UserGeneratedContentService ugcService = Mockito.mock(UserGeneratedContentService.class);
        Mockito.when(ugcService.createUGCContainer(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(container);

        UserGeneratedContentAction ur = new UserGeneratedContentAction(filter, ugcService);

        ValueMap actionData = new ModifiableValueMapDecorator(new HashMap<String, Object>());
        actionData.put("additionalProperties", new String[] { "name1=value1", "name3=vale2", "name4" });
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
        assertTrue(true);

    }
}
