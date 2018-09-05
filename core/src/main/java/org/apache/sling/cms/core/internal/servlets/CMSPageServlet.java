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
package org.apache.sling.cms.core.internal.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet which includes the content of the page when the page is accessed.
 */
@Component(service = { Servlet.class }, property = { "sling.servlet.resourceTypes=sling:Page",
		"sling.servlet.methods=" + HttpConstants.METHOD_TRACE, "sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.methods=" + HttpConstants.METHOD_OPTIONS, "sling.servlet.methods=" + HttpConstants.METHOD_HEAD })
public class CMSPageServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = -410942682163323725L;
	private static final Logger log = LoggerFactory.getLogger(CMSPageServlet.class);

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		Resource contentResource = request.getResourceResolver().getResource(request.getResource(),
				JcrConstants.JCR_CONTENT);
		if (contentResource == null) {
			log.error("No content for page {}", request.getResource());
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "no content");
			return;
		}
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(contentResource);
		if (requestDispatcher != null) {
			requestDispatcher.include(request, response);
		} else {
			log.error("Failed to get request dispatcher for content of {}", request.getResource());
			throw new ServletException("No Content");
		}
	}
}
