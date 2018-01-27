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

import java.util.List;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

/**
 * A model representing a site configuration.
 */
@Model(adaptables = Resource.class)
public class SiteConfig {

	@Inject
	@Optional
	private List<Parameter> parameters;

	@Inject
	@Optional
	private List<PageTemplate> pageTemplates;

	/**
	 * @return the pageTemplates
	 */
	public List<PageTemplate> getPageTemplates() {
		return pageTemplates;
	}

	/**
	 * @return the parameters
	 */
	public List<Parameter> getParameters() {
		return parameters;
	}

	public String getParameterValue(String key) {
		String value = null;
		if (parameters != null) {
			for (Parameter param : parameters) {
				if (key.equals(param.getKey())) {
					value = param.getValue();
					break;
				}
			}
		}
		return value;
	}

	/**
	 * @param pageTemplates
	 *            the pageTemplates to set
	 */
	public void setPageTemplates(List<PageTemplate> pageTemplates) {
		this.pageTemplates = pageTemplates;
	}

	/**
	 * @param parameters
	 *            the parameters to set
	 */
	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

}
