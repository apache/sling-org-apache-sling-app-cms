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
package org.apache.sling.cms.core.models;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.AccessDeniedException;
import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;

import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.cms.core.helpers.SlingCMSTestHelper;
import org.apache.sling.cms.core.internal.ResourceEditorAssociationProvider;
import org.apache.sling.cms.i18n.I18NProvider;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ContentBreadcrumbTest {

    @Rule
    public SlingContext context = new SlingContext();
    private ResourceEditorAssociationProvider associationProvider;

    @Before
    public void init()
            throws AccessDeniedException, UnsupportedRepositoryOperationException, RepositoryException, IOException {
        SlingCMSTestHelper.initContext(context);

        associationProvider = mock(ResourceEditorAssociationProvider.class);
        when(associationProvider.getAssociations()).thenReturn(Collections.emptyList());
    }

    @Test
    public void testValid() throws IllegalAccessException {

        context.requestPathInfo().setSuffix("/content/apache/sling-apache-org");

        Resource resource = mock(Resource.class);
        Map<String, Object> properties = new HashMap<>();
        properties.put("depth", 2);
        properties.put("rootTitle", "Content");
        when(resource.getValueMap()).thenReturn(new ValueMapDecorator(properties));
        context.currentResource(resource);

        I18NProvider provider = SlingCMSTestHelper.getEchoingi18nProvider();

        ContentBreadcrumb breadcrumb = new ContentBreadcrumb(context.request(), provider);

        FieldUtils.writeField(breadcrumb, "associationProvider", associationProvider, true);

        assertEquals("Apache Sling", breadcrumb.getCurrentItem());

        assertEquals(1, breadcrumb.getParents().size());

        assertEquals("/bin/browser.html/content/apache", breadcrumb.getParents().get(0).getLeft());
        assertEquals("Content", breadcrumb.getParents().get(0).getRight());
    }
}
