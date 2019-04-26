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
import java.util.Iterator;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang.StringUtils;
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
    public void init(FilterConfig filterConfig) throws ServletException {
        // Nothing required
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        boolean enabled = "true".equals(request.getAttribute(ENABLED_ATTR_NAME));
        PrintWriter writer = null;
        boolean includeEnd = false;

        if (enabled) {

            String editPath = null;
            Resource resource = ((SlingHttpServletRequest) request).getResource();

            EditableResource editableResource = new EditableResourceImpl(resource);
            Component component = editableResource.getComponent();
            if (component != null && !component.isType(CMSConstants.COMPONENT_TYPE_PAGE)) {
                editPath = component.getEditPath();
            }

            writer = response.getWriter();

            if (StringUtils.isNotEmpty(editPath)) {
                includeEnd = true;
                writeEditorMarkup(resource, writer);
            } else if (component != null && !component.isEditable()) {
                includeEnd = true;
                EditableResource er = resource.adaptTo(EditableResource.class);
                if (er != null) {
                    component = er.getComponent();
                }
                writer = response.getWriter();
                writer.write("<div class=\"sling-cms-component\" data-sling-cms-title=\""
                        + (component != null ? component.getTitle() : "") + "\" data-sling-cms-resource-path=\""
                        + resource.getPath() + "\" data-sling-cms-resource-type=\"" + resource.getResourceType()
                        + "\">");
            }
        }
        chain.doFilter(request, response);
        if (enabled && writer != null && includeEnd) {
            writer.write("</div>");
        }
    }

    private void writeEditorMarkup(Resource resource, PrintWriter writer) {

        boolean last = false;
        boolean first = false;
        if (resource != null && resource.getParent() != null) {
            Iterator<Resource> children = resource.getParent().listChildren();
            if (!children.hasNext() || children.next().getPath().equals(resource.getPath())) {
                first = true;
            }
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
        boolean exists = resource.getResourceResolver().getResource(resource.getPath()) != null;

        Component component = null;
        EditableResource er = new EditableResourceImpl(resource);
        String editPath = "";
        if (er != null) {
            component = er.getComponent();
            editPath = component.getEditPath();
        }
        writer.write("<div class=\"sling-cms-component\" data-component=\""+component+"\" data-sling-cms-title=\""
                + (component != null ? component.getTitle() : "") + "\" data-sling-cms-resource-path=\""
                + resource.getPath() + "\" data-sling-cms-resource-type=\"" + resource.getResourceType()
                + "\" data-sling-cms-edit=\"" + editPath + "\"><div class=\"sling-cms-editor\">");
        writer.write(
                "<div class=\"level has-background-grey\"><div class=\"level-left\"><div class=\"field has-addons\">");

        writer.write(
                "<div class=\"control\"><button class=\"level-item button\" data-sling-cms-action=\"edit\" data-sling-cms-path=\""
                        + resource.getPath() + "\" data-sling-cms-edit=\"" + editPath
                        + "\" title=\"Edit Component\"><span class=\"jam jam-pencil-f\"></span></button></div>");
        if (!first || !last) {
            writer.write(
                    "<div class=\"control\"><button class=\"level-item button\" data-sling-cms-action=\"reorder\" data-sling-cms-path=\""
                            + resource.getPath()
                            + "\" title=\"Reorder Component\"><span class=\"jam jam-arrows-v\"></span></button></div>");
        }
        if (!resource.getName().equals(JcrConstants.JCR_CONTENT) && exists) {
            writer.write(
                    "<div class=\"control\"><button class=\"level-item button\" data-sling-cms-action=\"delete\" data-sling-cms-path=\""
                            + resource.getPath()
                            + "\" title=\"Delete Component\"><span class=\"jam jam-trash\"></span></button></div>");
        }

        writer.write("</div></div>");
        if (component != null) {
            writer.write("<div class=\"level-right\"><div class=\"level-item has-text-light\">" + component.getTitle()
                    + "</div></div>");
        }
        writer.write("</div></div>");
    }

    @Override
    public void destroy() {
        // Nothing required
    }

}
