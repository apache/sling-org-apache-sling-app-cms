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
package org.apache.sling.cms.core.filters;

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
import org.apache.sling.cms.core.models.Component;
import org.apache.sling.cms.core.models.EditableResource;
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

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		Resource resource = ((SlingHttpServletRequest) request).getResource();
		boolean enabled = "true".equals(request.getAttribute(ENABLED_ATTR_NAME));
		String editPath = getEditPath(resource);
		PrintWriter writer = null;

		if (enabled && StringUtils.isNotEmpty(editPath)) {
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
			writer = response.getWriter();
			Component component = resource.adaptTo(EditableResource.class).getComponent();
			writer.write("<div class=\"Sling-CMS__component\" data-sling-cms-title=\""
					+ (component != null ? component.getTitle() : "") + "\" data-sling-cms-resource-path=\""
					+ resource.getPath() + "\" data-sling-cms-resource-type=\"" + resource.getResourceType()
					+ "\" data-sling-cms-edit=\"" + editPath + "\">");
			writer.write("<div class=\"Sling-CMS__edit-bar\">");
			writer.write(
					"<button class=\"Sling-CMS__edit-button\" data-sling-cms-action=\"edit\" data-sling-cms-path=\""
							+ resource.getPath() + "\" data-sling-cms-edit=\"" + editPath
							+ "\" title=\"Edit\">&#x270f;</button>");
			if (!first || !last) {
				writer.write(
						"<button class=\"Sling-CMS__edit-button\" data-sling-cms-action=\"reorder\" data-sling-cms-path=\""
								+ resource.getPath() + "\" title=\"Reorder\">&#8597;</button>");
			}
			if (!resource.getName().equals(JcrConstants.JCR_CONTENT) && exists) {
				writer.write(
						"<button class=\"Sling-CMS__edit-button\" data-sling-cms-action=\"delete\" data-sling-cms-path=\""
								+ resource.getPath() + "\" title=\"Delete\">&times;</button>");
			}

			if (component != null) {
				writer.write("<span class=\"Sling-CMS__component-title\">" + component.getTitle() + "</span>");
			}
			writer.write("</div>");
		}
		chain.doFilter(request, response);
		if (enabled && StringUtils.isNotEmpty(editPath)) {
			writer.write("</div>");
		}
	}

	private String getEditPath(Resource resource) {
		log.trace("getEditPage resource={}", resource);
		String editPath = null;
		if (resource != null) {
			EditableResource editResource = new EditableResource(resource);
			if (editResource != null) {
				Component component = editResource.getComponent();
				if (component != null && !component.isType(CMSConstants.COMPONENT_TYPE_PAGE)) {
					editPath = component.getEditPath();
				}
			}
		}
		return editPath;
	}

	@Override
	public void destroy() {
	}

}
