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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.Component;
import org.apache.sling.cms.EditableResource;
import org.apache.sling.cms.core.internal.models.EditableResourceImpl;

/**
 * Filter for injecting the request attributes and markup to enable the Sling
 * CMS editor.
 */
@org.osgi.service.component.annotations.Component(service = { Filter.class }, property = {
        "sling.filter.scope=component" })
public class EditIncludeFilter implements Filter {

    public static final String ENABLED_ATTR_NAME = "cmsEditEnabled";

    @Override
    public void destroy() {
        // Nothing required
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        boolean enabled = "true".equals(request.getAttribute(ENABLED_ATTR_NAME));
        PrintWriter writer = null;
        boolean includeEnd = false;

        if (enabled) {
            writer = response.getWriter();
            includeEnd = writeHeader(request, writer, includeEnd);
        }
        chain.doFilter(request, response);
        if (enabled && writer != null && includeEnd) {
            writer.write("</div>");
        }
    }

    private Iterator<Resource> getSiblings(Resource resource) {
        return Optional.ofNullable(resource.getParent()).map(p -> p.listChildren()).orElse(Collections.emptyIterator());
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Nothing required
    }

    private boolean isFirst(Resource resource) {
        boolean first = false;
        if (resource != null && resource.getParent() != null) {
            Iterator<Resource> children = getSiblings(resource);
            if (!children.hasNext() || children.next().getPath().equals(resource.getPath())) {
                first = true;
            }
        }
        return first;
    }

    private boolean isLast(Resource resource) {
        boolean last = false;
        if (resource != null && resource.getParent() != null) {
            Iterator<Resource> children = getSiblings(resource);
            if (children.hasNext()) {
                while (children.hasNext()) {
                    if (children.next().getPath().equals(resource.getPath()) && !children.hasNext()) {
                        last = true;
                    }
                }
            } else {
                last = true;
            }
        }
        return last;
    }

    private void writeEditorMarkup(Resource resource, PrintWriter writer) {

        boolean exists = resource.getResourceResolver().getResource(resource.getPath()) != null;
        boolean last = isFirst(resource);
        boolean first = isLast(resource);

        EditableResource er = new EditableResourceImpl(resource);
        Component component = er.getComponent();
        String editPath = component.getEditPath();
        String title = StringUtils.isNotEmpty(component.getTitle()) ? component.getTitle()
                : StringUtils.substringAfterLast(resource.getResourceType(), "/");
        writer.write("<div class=\"sling-cms-component\" data-reload=\"" + component.isReloadPage()
                + "\" data-component=\"" + component.getResource().getPath() + "\" data-sling-cms-title=\"" + title
                + "\" data-sling-cms-resource-path=\"" + resource.getPath() + "\" data-sling-cms-resource-type=\""
                + resource.getResourceType() + "\" data-sling-cms-edit=\"" + editPath
                + "\"><div class=\"sling-cms-editor\">");
        writer.write(
                "<div class=\"level has-background-grey\"><div class=\"level-left\"><div class=\"field has-addons\">");

        writer.write("<div class=\"control\"><a href=\"/cms/editor/edit.html" + resource.getPath() + "?editor="
                + editPath + "\" class=\"level-item button action-button\"  title=\"Edit " + title
                + "\"><span class=\"jam jam-pencil-f\"><span class=\"is-vhidden\">Edit " + title
                + "</span></span></a></div>");
        if (!first || !last) {
            writer.write("<div class=\"control\"><a href=\"/cms/editor/reorder.html" + resource.getPath()
                    + "\" class=\"level-item button action-button\" title=\"Reorder " + title
                    + "\"><span class=\"jam jam-arrows-v\"><span class=\"is-vhidden\">Reorder " + title
                    + "</span></span></a></div>");
        }
        if (!resource.getName().equals(JcrConstants.JCR_CONTENT) && exists) {
            writer.write("<div class=\"control\"><a href=\"/cms/editor/delete.html" + resource.getPath()
                    + "\" class=\"level-item button action-button\" title=\"Delete Component\"><span class=\"jam jam-trash\"><span class=\"is-vhidden\">Delete "
                    + title + "</span></span></a></div>");
        }

        writer.write("</div></div>");
        writer.write("<div class=\"level-right\"><div class=\"level-item has-text-light\">" + title + "</div></div>");
        writer.write("</div></div>");
    }

    private boolean writeHeader(ServletRequest request, PrintWriter writer, boolean includeEnd) {
        String editPath = null;
        Resource resource = ((SlingHttpServletRequest) request).getResource();

        EditableResource editableResource = new EditableResourceImpl(resource);
        Component component = editableResource.getComponent();
        if (component != null && !component.isType(CMSConstants.COMPONENT_TYPE_PAGE)) {
            editPath = component.getEditPath();
        }

        if (StringUtils.isNotEmpty(editPath)) {
            includeEnd = true;
            writeEditorMarkup(resource, writer);
        } else if (component != null && !component.isEditable()) {
            includeEnd = true;
            String title = StringUtils.isNotEmpty(component.getTitle()) ? component.getTitle()
                    : StringUtils.substringAfterLast(resource.getResourceType(), "/");
            writer.write("<div class=\"sling-cms-component\" data-reload=\"" + component.isReloadPage()
            + "\" data-component=\"" + component.getResource().getPath() + "\" data-sling-cms-title=\"" + title
            + "\" data-sling-cms-resource-path=\"" + resource.getPath() + "\" data-sling-cms-resource-type=\""
            + resource.getResourceType() + "\" data-sling-cms-edit=\"" + editPath
            + "\">");
        }
        return includeEnd;
    }

}
