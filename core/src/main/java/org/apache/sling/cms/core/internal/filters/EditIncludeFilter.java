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
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.Component;
import org.apache.sling.cms.EditableResource;
import org.apache.sling.cms.core.internal.models.EditableResourceImpl;
import org.apache.sling.cms.i18n.I18NDictionary;
import org.apache.sling.cms.i18n.I18NProvider;
import org.osgi.framework.Bundle;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filter for injecting the request attributes and markup to enable the Sling
 * CMS editor.
 */
@org.osgi.service.component.annotations.Component(service = { Filter.class }, property = {
        "sling.filter.scope=component" })
public class EditIncludeFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(EditIncludeFilter.class);

    public static final String ENABLED_ATTR_NAME = "cmsEditEnabled";

    public static final String WRITE_DROP_TARGET_ATTR_NAME = "writeDropTarget";

    protected static final String ENTRY_BASE = "res/editinclude/";

    private Map<String, String> templates = new HashMap<>();

    private I18NProvider provider;

    @Activate
    public EditIncludeFilter(ComponentContext context, @Reference I18NProvider provider) throws IOException {
        Bundle bundle = context.getBundleContext().getBundle();
        Enumeration<String> entries = bundle.getEntryPaths(ENTRY_BASE);
        while (entries.hasMoreElements()) {
            String en = entries.nextElement();
            log.info("Loaded template: {}", en);
            try (InputStream is = bundle.getEntry(en).openStream()) {
                templates.put(en.replace(ENTRY_BASE, ""),
                        StringUtils.substringAfter(IOUtils.toString(is, StandardCharsets.UTF_8), "-->"));
            }
        }
        this.provider = provider;
    }

    @Override
    public void destroy() {
        // Nothing required
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        boolean enabled = "true".equals(request.getAttribute(ENABLED_ATTR_NAME));
        PrintWriter writer = null;
        if (enabled) {
            boolean includeEnd = false;
            SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
            Resource resource = slingRequest.getResource();
            Boolean container = isContainer(resource);
            boolean writeDropTarget = shouldWriteDropTarget(slingRequest);
            writer = response.getWriter();
            if (writeDropTarget) {
                this.writeDropTarget(resource, writer, "before " + resource.getName());
            }
            includeEnd = writeHeader(slingRequest, writer, includeEnd);
            request.setAttribute(WRITE_DROP_TARGET_ATTR_NAME, container);
            chain.doFilter(request, response);
            request.setAttribute(WRITE_DROP_TARGET_ATTR_NAME, writeDropTarget);
            if (includeEnd) {
                writer.write("</div>");
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private Iterator<Resource> getSiblings(Resource resource) {
        return Optional.ofNullable(resource.getParent()).map(Resource::listChildren)
                .orElse(Collections.emptyIterator());
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Nothing required
    }

    private boolean isContainer(Resource resource) {
        EditableResource er = new EditableResourceImpl(resource);
        if (er.getComponent() != null) {
            return er.getComponent().isContainer();
        } else {
            return false;
        }
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

    private boolean shouldWriteDropTarget(SlingHttpServletRequest request) {
        return request.getAttribute(WRITE_DROP_TARGET_ATTR_NAME) != null
                && request.getAttribute(WRITE_DROP_TARGET_ATTR_NAME) == Boolean.TRUE;
    }

    private void writeDropTarget(Resource resource, PrintWriter writer, String order) {
        Map<String, Object> replacements = new HashMap<>();
        replacements.put("parentPath", resource.getParent().getPath());
        replacements.put("order", order);
        writeTemplate(writer, replacements, "droptarget.html");
    }

    private void writeEditorMarkup(Resource resource, PrintWriter writer, I18NDictionary resourceBundle,
            boolean draggable) {

        boolean exists = resource.getResourceResolver().getResource(resource.getPath()) != null;
        boolean last = isFirst(resource);
        boolean first = isLast(resource);

        EditableResource er = new EditableResourceImpl(resource);
        Component component = er.getComponent();
        String editPath = component.getEditPath();
        String title = StringUtils.isNotEmpty(component.getTitle()) ? component.getTitle()
                : StringUtils.substringAfterLast(resource.getResourceType(), "/");
        title = resourceBundle.get(title);

        Map<String, Object> replacements = new HashMap<>();
        replacements.put("componentPath", component.getResource().getPath());
        replacements.put("draggable", draggable);
        replacements.put("editPath", editPath);
        replacements.put("reload", component.isReloadPage());
        replacements.put("resourceName", component.getResource().getName());
        replacements.put("resourcePath", resource.getPath());
        replacements.put("resourceType", resource.getResourceType());
        replacements.put("title", title);

        writeTemplate(writer, replacements, "start.html");
        writeTemplate(writer, replacements, "edit.html");
        if (!first || !last) {
            writeTemplate(writer, replacements, "reorder.html");
        }
        if (!resource.getName().equals(JcrConstants.JCR_CONTENT) && exists) {
            writeTemplate(writer, replacements, "delete.html");
        }
        writeTemplate(writer, replacements, "end.html");
    }

    private void writeTemplate(PrintWriter writer, Map<String, Object> replacements, String templateName) {
        StringSubstitutor sub = new StringSubstitutor(replacements);
        String template = templates.get(templateName);
        String result = sub.replace(template);
        log.trace("Using: {} and {} to create {}", templateName, replacements, result);
        writer.write(result);
    }

    private boolean writeHeader(SlingHttpServletRequest request, PrintWriter writer, boolean includeEnd) {
        String editPath = null;
        Resource resource = request.getResource();

        EditableResource editableResource = new EditableResourceImpl(resource);
        Component component = editableResource.getComponent();
        if (component != null && !component.isType(CMSConstants.COMPONENT_TYPE_PAGE)) {
            editPath = component.getEditPath();
        }
        if (editPath == null) {
            editPath = "";
        }

        I18NDictionary resourceBundle = provider.getDictionary(request);

        if (StringUtils.isNotEmpty(editPath)) {
            includeEnd = true;
            writeEditorMarkup(resource, writer, resourceBundle, shouldWriteDropTarget(request));
        } else if (component != null && !component.isEditable()) {
            includeEnd = true;
            String title = StringUtils.isNotEmpty(component.getTitle()) ? component.getTitle()
                    : StringUtils.substringAfterLast(resource.getResourceType(), "/");
            title = resourceBundle.get(title);

            Map<String, Object> replacements = new HashMap<>();
            replacements.put("componentPath", component.getResource().getPath());
            replacements.put("editPath", editPath);
            replacements.put("reload", component.isReloadPage());
            replacements.put("resourceName", component.getResource().getName());
            replacements.put("resourcePath", resource.getPath());
            replacements.put("resourceType", resource.getResourceType());
            replacements.put("title", title);
            writeTemplate(writer, replacements, "header.html");
        }

        return includeEnd;
    }

}
