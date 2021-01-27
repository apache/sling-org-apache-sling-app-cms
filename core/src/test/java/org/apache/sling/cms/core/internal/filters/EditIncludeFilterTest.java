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
package org.apache.sling.cms.core.internal.filters;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import javax.jcr.AccessDeniedException;
import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import org.apache.sling.cms.core.helpers.SlingCMSTestHelper;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

public class EditIncludeFilterTest {

    @Rule
    public SlingContext context = new SlingContext();
    private EditIncludeFilter includeFilter;

    @Before
    public void init()
            throws AccessDeniedException, UnsupportedRepositoryOperationException, RepositoryException, IOException {
        SlingCMSTestHelper.initAuthContext(context);

        includeFilter = new EditIncludeFilter();

        Bundle bundle = Mockito.mock(Bundle.class);
        Mockito.when(bundle.getEntryPaths(Mockito.anyString()))
                .thenReturn(Collections.enumeration(Arrays.asList(EditIncludeFilter.ENTRY_BASE + "delete.html",
                        EditIncludeFilter.ENTRY_BASE + "droptarget.html", EditIncludeFilter.ENTRY_BASE + "edit.html",
                        EditIncludeFilter.ENTRY_BASE + "end.html", EditIncludeFilter.ENTRY_BASE + "header.html",
                        EditIncludeFilter.ENTRY_BASE + "reorder.html", EditIncludeFilter.ENTRY_BASE + "start.html")));
        Mockito.when(bundle.getEntry(Mockito.anyString())).thenAnswer((in) -> {
            String entry = in.getArgument(0, String.class);
            return getClass().getClassLoader().getResource(entry);
        });

        BundleContext bundleContext = Mockito.mock(BundleContext.class);
        Mockito.when(bundleContext.getBundle()).thenReturn(bundle);

        ComponentContext context = Mockito.mock(ComponentContext.class);
        Mockito.when(context.getBundleContext()).thenReturn(bundleContext);
        includeFilter.activate(context);
    }

    @Test
    public void testPage() throws IOException, ServletException {

        context.currentResource("/content/apache/sling-apache-org/index");
        context.requestPathInfo().setExtension("html");

        context.request().setAttribute(EditIncludeFilter.ENABLED_ATTR_NAME, "true");

        includeFilter.doFilter(context.request(), context.response(), Mockito.mock(FilterChain.class));

        assertEquals("", context.response().getOutputAsString());
    }

    @Test
    public void testDisabled() throws IOException, ServletException {

        context.currentResource("/content/apache/sling-apache-org/index/jcr:content/menu/richtext");
        context.requestPathInfo().setExtension("html");

        context.request().setAttribute(EditIncludeFilter.ENABLED_ATTR_NAME, "false");

        includeFilter.doFilter(context.request(), context.response(), Mockito.mock(FilterChain.class));

        assertEquals("", context.response().getOutputAsString());
    }

    @Test
    public void testComponent() throws IOException, ServletException {

        context.currentResource("/content/apache/sling-apache-org/index/jcr:content/menu/richtext");
        context.requestPathInfo().setExtension("html");

        context.request().setAttribute(EditIncludeFilter.ENABLED_ATTR_NAME, "true");

        includeFilter.doFilter(context.request(), context.response(), Mockito.mock(FilterChain.class));

        assertEquals(
                "<div class=\"sling-cms-component\" data-reload=\"false\" data-component=\"/libs/sling-cms/components/general/richtext\" data-sling-cms-title=\"Rich Text Editor\" data-sling-cms-resource-path=\"/content/apache/sling-apache-org/index/jcr:content/menu/richtext\" data-sling-cms-resource-type=\"sling-cms/components/general/richtext\" data-sling-cms-edit=\"/libs/sling-cms/components/general/richtext/edit\" data-sling-cms-resource-name=\"richtext\">\n    <div class=\"sling-cms-editor\" draggable=\"false\">\n        <div class=\"level has-background-light\">\n            <div class=\"level-left\">\n                <div class=\"field has-addons\"><div class=\"control\">\n    <a href=\"/cms/editor/edit.html/content/apache/sling-apache-org/index/jcr:content/menu/richtext?editor=/libs/sling-cms/components/general/richtext/edit\" class=\"level-item button is-small has-text-black-ter action-button\"  title=\"Edit Rich Text Editor\">\n        <span class=\"icon\">\n            <span class=\"jam jam-pencil-f\">\n                <span class=\"is-sr-only\">Edit Rich Text Editor</span>\n            </span>\n        </span>\n    </a>\n</div><div class=\"control\">\n    <a href=\"/cms/editor/delete.html/content/apache/sling-apache-org/index/jcr:content/menu/richtext\" class=\"level-item button is-small has-text-black-ter action-button\" title=\"Delete Component\">\n        <span class=\"icon\">\n            <span class=\"jam jam-trash\">\n                <span class=\"is-sr-only\">Delete Rich Text Editor</span>\n            </span>\n        </span>\n    </a>\n</div>            </div>  \n        </div>\n        <div class=\"level-right\">\n            <div class=\"level-item has-text-black-ter\">Rich Text Editor</div>\n        </div>\n    </div>\n</div></div>",
                context.response().getOutputAsString());
    }

    @Test
    public void testContainer() throws IOException, ServletException {

        context.currentResource("/content/apache/sling-apache-org/index/jcr:content/container");
        context.requestPathInfo().setExtension("html");

        context.request().setAttribute(EditIncludeFilter.ENABLED_ATTR_NAME, "true");
        context.request().setAttribute(EditIncludeFilter.WRITE_DROP_TARGET_ATTR_NAME, Boolean.TRUE);

        includeFilter.doFilter(context.request(), context.response(), Mockito.mock(FilterChain.class));

        assertEquals(
                "<div class=\"sling-cms-droptarget\" data-path=\"/content/apache/sling-apache-org/index/jcr:content\" data-order=\"before container\"></div><div class=\"sling-cms-component\" data-reload=\"false\" data-component=\"/libs/sling-cms/components/general/container\" data-sling-cms-title=\"Container\" data-sling-cms-resource-path=\"/content/apache/sling-apache-org/index/jcr:content/container\" data-sling-cms-resource-type=\"sling-cms/components/general/container\" data-sling-cms-edit=\"\"></div>",
                context.response().getOutputAsString());
    }

}