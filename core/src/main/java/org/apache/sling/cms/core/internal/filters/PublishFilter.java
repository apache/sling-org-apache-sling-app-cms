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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.cms.core.CMSConstants;
import org.apache.sling.cms.core.CMSUtils;
import org.osgi.service.component.annotations.Component;

/**
 * Denies requests to sling:Page and sling:File resources and children which are
 * not set to publish=true
 */
@Component(service = { Filter.class }, property = { "sling.filter.scope=request",
		"service.ranking=" + Integer.MAX_VALUE })
public class PublishFilter implements Filter {

	private static final String[] VALID_METHODS = new String[] { "GET", "HEAD" };

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (request instanceof SlingHttpServletRequest) {
			SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
			if (ArrayUtils.contains(VALID_METHODS, slingRequest.getMethod())) {
				Object editEnabled = slingRequest.getAttribute(CMSConstants.ATTR_EDIT_ENABLED);
				if (!"true".equals(editEnabled) && !CMSUtils.isPublished(slingRequest.getResource())) {
					((HttpServletResponse) response).sendError(404);
					return;
				}
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

}
