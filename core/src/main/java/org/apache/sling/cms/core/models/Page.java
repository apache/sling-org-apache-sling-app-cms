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

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.CMSUtils;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

/**
 * A model representing a page.
 */
@Model(adaptables = Resource.class)
public class Page extends AbstractContentModel {

	public static final Page getContainingPage(Resource resource) {
		Resource pageRsrc = CMSUtils.findParentResourceofType(resource, CMSConstants.NT_PAGE);
		Page page = null;
		if (pageRsrc != null) {
			page = pageRsrc.adaptTo(Page.class);
		}
		return page;
	}

	@Inject
	@Named("jcr:content/published")
	@Default(booleanValues = false)
	private boolean published;

	@Inject
	@Optional
	@Named(JcrConstants.JCR_CONTENT + "/" + CMSConstants.PN_TAXONOMY)
	private String[] taxonomy;

	@Inject
	@Optional
	@Named("jcr:content/sling:template")
	private String template;

	public Page(Resource resource) {
		this.resource = resource;
	}

	public String[] getKeywords() {
		List<String> keywords = new ArrayList<String>();
		if (taxonomy != null) {
			for (String item : taxonomy) {
				Resource resource = this.resource.getResourceResolver().getResource(item);
				if (resource != null) {
					keywords.add(resource.getValueMap().get(CMSConstants.PN_TITLE, String.class));
				}
			}
		}
		return keywords.toArray(new String[keywords.size()]);
	}

	public boolean isPublished() {
		return published;
	}

	public String getPublishedPath() {
		Site site = resource.adaptTo(SiteManager.class).getSite();
		if (site != null) {
			return resource.getPath().replace(site.getPath(), "") + ".html";
		} else {
			return resource.getPath() + ".html";
		}
	}

	public String getPublishedUrl() {
		Site site = resource.adaptTo(SiteManager.class).getSite();
		if (site != null) {
			return site.getUrl() + getPublishedPath();
		} else {
			return resource.getPath();
		}
	}

	public PageTemplate getTemplate() {
		Resource templateResource = this.resource.getResourceResolver().getResource(template);
		if (templateResource != null) {
			return templateResource.adaptTo(PageTemplate.class);
		} else {
			return null;
		}
	}

	public String getTemplatePath() {
		return this.template;
	}
}
