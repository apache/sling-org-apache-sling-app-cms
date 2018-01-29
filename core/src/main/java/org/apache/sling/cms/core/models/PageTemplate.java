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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

/**
 * A simple model for representing a page template
 */
@Model(adaptables = Resource.class)
public class PageTemplate {

	@Inject
	@Optional
	private String[] allowedPaths;

	@Inject
	@Optional
	private String[] availableComponentTypes;

	@Inject
	private List<Resource> componentConfigurations;
	
	@Inject
	private List<Resource> fields;

	private Resource resource;

	@Inject
	@Optional
	private String template;

	@Inject
	@Named(CMSConstants.PN_TITLE)
	private String title;

	public PageTemplate(Resource resource) {
		this.resource = resource;
	}

	/**
	 * @return the allowedPaths
	 */
	public String[] getAllowedPaths() {
		return allowedPaths;
	}

	/**
	 * @return the availableComponentTypes
	 */
	public String[] getAvailableComponentTypes() {
		return availableComponentTypes;
	}
	
	

	/**
	 * @return the componentConfigs
	 */
	public Map<String,Resource> getComponentConfigs() {
		Map<String,Resource> configs = new HashMap<String,Resource>();
		for(Resource cfg : componentConfigurations){
			configs.put(cfg.getValueMap().get("type", String.class), cfg);
		}
		return configs;
	}

	/**
	 * @return the fields
	 */
	public List<Resource> getFields() {
		return fields;
	}

	/**
	 * @return the resource
	 */
	public Resource getResource() {
		return resource;
	}

	/**
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PageTemplate [allowedPaths=" + Arrays.toString(allowedPaths) + ", availableComponentTypes="
				+ Arrays.toString(availableComponentTypes) + ", fields=" + fields + ", resource=" + resource
				+ ", template=" + template + ", title=" + title + "]";
	}

}
