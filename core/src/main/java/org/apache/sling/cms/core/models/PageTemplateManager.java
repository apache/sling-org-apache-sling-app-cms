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

import java.util.ArrayList;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

/**
 * A model for retrieving the available templates to create a page under the
 * specified resource.
 */
@Model(adaptables = Resource.class)
public class PageTemplateManager {

	private SiteConfig siteConfig;
	private Resource resource;

	public PageTemplateManager(Resource resource) {
		Site site = resource.adaptTo(SiteManager.class).getSite();
		this.siteConfig = site.getSiteConfig();
		this.resource = resource;
	}

	public List<PageTemplate> getAvailableTemplates() {
		String path = resource.getPath();
		List<PageTemplate> availableTemplates = new ArrayList<PageTemplate>();
		if (siteConfig != null && siteConfig.getPageTemplates() != null) {
			for (PageTemplate template : siteConfig.getPageTemplates()) {
				for (String allowedPath : template.getAllowedPaths()) {
					if (path.matches(allowedPath)) {
						availableTemplates.add(template);
						break;
					}
				}
			}
		}
		return availableTemplates;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PageTemplateManager [siteConfig=" + siteConfig + ", resource=" + resource + "]";
	}
}
