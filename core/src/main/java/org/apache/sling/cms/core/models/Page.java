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

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.CMSUtils;
import org.apache.sling.models.annotations.Model;

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
	private Boolean published;

	public Page(Resource resource) {
		this.resource = resource;
	}

	public String getAvailableComponents() {
		Resource config = resource.adaptTo(SiteManager.class).getSite().getConfig();
		List<String> types = new ArrayList<String>();
		if (config != null && config.getChild("content/pagetypes") != null) {
			for (Resource pageType : config.getChild("content/pagetypes").getChildren()) {
				if (getContentResource().getResourceType()
						.equals(pageType.getValueMap().get("resourceType", String.class))) {
					for (Resource type : pageType.getChild("availabletypes").getChildren()) {
						types.add(type.getValueMap().get("resourceType", String.class));
					}
				}
			}
		}
		return StringUtils.join(types, ",");
	}

	public Boolean getPublished() {
		return published;
	}
}
