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

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sling Model for retrieving an error handler based on the specified Sling
 * Request. Checks if the specified resource is contained within a Sling site
 * and if so, will display the error page found at
 * [site-root]/errors/[error-code] or [site-root]/errors/default
 */
@Model(adaptables = SlingHttpServletRequest.class)
public class ErrorHandler {

	private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

	@RequestAttribute
	@Named(SlingConstants.ERROR_STATUS)
	@Optional
	@Default(intValues = 500)
	private Integer errorCode;

	private SlingHttpServletRequest slingRequest;

	private Resource handler;

	public ErrorHandler(SlingHttpServletRequest slingRequest) {
		this.slingRequest = slingRequest;
	}

	@PostConstruct
	public void init() {

		Resource resource = slingRequest.getResource();
		ResourceResolver resolver = slingRequest.getResourceResolver();

		log.debug("Calculating error handling scripts for resource {} and error code {}", resource, errorCode);

		if (slingRequest.getAttribute(SlingConstants.ERROR_EXCEPTION) != null) {
			log.warn("Handing exception of type " + errorCode,
					slingRequest.getAttribute(SlingConstants.ERROR_EXCEPTION));
		}
		SiteManager siteMgr = resource.adaptTo(SiteManager.class);
		if (siteMgr != null && siteMgr.getSite() != null) {
			Site site = siteMgr.getSite();
			log.debug("Checking for error pages in the site {}", site.getPath());

			handler = site.getResource().getChild("errors/" + errorCode.toString());
			if (handler == null) {
				handler = site.getResource().getChild("errors/default");
			}

			if (handler != null) {
				log.debug("Using error handler {}", handler);
			} else {
				log.debug("No error page defined for site {}", site.getPath());
			}
		}

		if (handler == null) {
			log.debug("Using Sling CMS default error pages");
			handler = resolver.getResource("/libs/sling-cms/content/errorhandling/" + errorCode.toString());
			if (handler == null) {
				handler = resolver.getResource("/libs/sling-cms/content/errorhandling/default");
			}
			log.debug("Using Sling CMS error handler {}", handler);
		}
	}

	public Resource getHandler() {
		return handler;
	}
}
