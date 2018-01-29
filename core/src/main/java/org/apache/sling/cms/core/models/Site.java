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

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

/**
 * A model for representing a site.
 */
@Model(adaptables = Resource.class)
public class Site {

	public static final String PN_CONFIG = CMSConstants.NAMESPACE + ":config";

	private static Resource findSiteResource(Resource resource) {
		if (CMSConstants.NT_SITE.equals(resource.getValueMap().get(JcrConstants.JCR_PRIMARYTYPE, String.class))) {
			return resource;
		} else if (resource.getParent() != null) {
			return findSiteResource(resource.getParent());
		}
		return null;
	}

	public static Site getSite(Resource resource) {
		Site site = null;
		Resource siteResource = Site.findSiteResource(resource);
		if (siteResource != null) {
			site = siteResource.adaptTo(Site.class);
		}
		return site;
	}

	@Inject
	@Named(CMSConstants.PN_DESCRIPTION)
	@Optional
	private String description;

	@Inject
	@Named(CMSConstants.PN_TITLE)
	private String title;

	@Inject
	@Named(PN_CONFIG)
	private String config;

	private Resource resource;

	public Site(Resource resource) {
		this.resource = resource;
	}

	public String getDescription() {
		return description;
	}

	public String getPath() {
		return resource.getPath();
	}

	public Resource getResource() {
		return resource;
	}

	public String getTitle() {
		return title;
	}

	public String getSiteConfigPath() {
		return config;
	}

	public SiteConfig getSiteConfig() {
		Resource scr = resource.getResourceResolver().getResource(getSiteConfigPath());
		if (scr != null) {
			return scr.adaptTo(SiteConfig.class);
		}
		return null;
	}

}
