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

import java.util.Locale;

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
	@Named(PN_CONFIG)
	private String config;

	@Inject
	@Named(CMSConstants.PN_DESCRIPTION)
	@Optional
	private String description;

	@Inject
	@Named(CMSConstants.PN_LANGUAGE)
	private String locale;

	private Resource resource;

	@Inject
	@Named(CMSConstants.PN_TITLE)
	private String title;

	public Site(Resource resource) {
		this.resource = resource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Site other = (Site) obj;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.getPath().equals(other.resource.getPath()))
			return false;
		return true;
	}

	public String getDescription() {
		return description;
	}

	public String getLocaleString() {
		return locale;
	}

	public Locale getLocale() {
		String[] segments = locale.split("_");
		if (segments.length == 3) {
			return new Locale(segments[0], segments[1], segments[2]);
		} else if (segments.length == 2) {
			return new Locale(segments[0], segments[1]);
		}
		return new Locale(segments[0]);
	}

	public String getPath() {
		return resource.getPath();
	}

	public Resource getResource() {
		return resource;
	}

	public SiteConfig getSiteConfig() {
		Resource scr = resource.getResourceResolver().getResource(getSiteConfigPath());
		if (scr != null) {
			return scr.adaptTo(SiteConfig.class);
		}
		return null;
	}

	public String getSiteConfigPath() {
		return config;
	}

	public String getTitle() {
		return title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((resource.getPath() == null) ? 0 : resource.getPath().hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Site [config=" + config + ", description=" + description + ", locale=" + locale + ", resource="
				+ resource + ", title=" + title + "]";
	}

}
