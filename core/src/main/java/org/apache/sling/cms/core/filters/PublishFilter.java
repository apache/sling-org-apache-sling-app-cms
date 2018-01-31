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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.felix.scr.annotations.sling.SlingFilter;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.jcr.resource.JcrResourceConstants;

@SlingFilter(order = Integer.MAX_VALUE)
public class PublishFilter implements Filter {

	public static final String[] PUBLISHABLE_TYPES = new String[] { CMSConstants.NT_FILE, CMSConstants.NT_PAGE,
			JcrResourceConstants.NT_SLING_FOLDER, JcrResourceConstants.NT_SLING_ORDERED_FOLDER };

	public static final String[] VALID_METHODS = new String[] { "GET", "HEAD" };

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
				if (!"true".equals(editEnabled)) {
					Resource publishable = findPublishableParent(slingRequest.getResource());
					if (publishable != null && publishable.getChild(JcrConstants.JCR_CONTENT) != null) {
						if (!(publishable.getChild(JcrConstants.JCR_CONTENT).getValueMap()
								.get(CMSConstants.PN_PUBLISHED, true))) {
							((HttpServletResponse) response).sendError(404);
							return;
						}
					}
				}
			}
		}
		chain.doFilter(request, response);
	}

	private Resource findPublishableParent(Resource resource) {
		String type = resource.getValueMap().get(JcrConstants.JCR_PRIMARYTYPE, String.class);
		if (ArrayUtils.contains(PUBLISHABLE_TYPES, type)) {
			return resource;
		} else if (resource.getParent() != null) {
			return findPublishableParent(resource.getParent());
		}
		return null;
	}

	@Override
	public void destroy() {
	}

}
