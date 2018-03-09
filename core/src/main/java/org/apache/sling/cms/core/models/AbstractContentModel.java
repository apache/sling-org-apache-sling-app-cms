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

import java.util.Calendar;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Optional;

/**
 * Abstract class representing the common attributes available in content
 * models.
 */
public abstract class AbstractContentModel {

	@Inject
	@Optional
	@Named("jcr:content")
	private Resource contentResource;

	@Inject
	@Optional
	@Named("jcr:content/jcr:created")
	private Calendar created;

	@Inject
	@Optional
	@Named("jcr:content/jcr:createdBy")
	private String createdBy;

	@Inject
	@Optional
	@Named("jcr:content/jcr:lastModified")
	private Calendar lastModified;

	@Inject
	@Optional
	@Named("jcr:content/jcr:lastModifiedBy")
	private String lastModifiedBy;

	protected Resource resource;

	@Inject
	@Named("jcr:content/jcr:title")
	@Optional
	private String title;

	@Inject
	@Named("jcr:primaryType")
	private String type;

	public Resource getContentResource() {
		return contentResource;
	}

	public Calendar getCreated() {
		return created;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public Calendar getLastModified() {
		return lastModified != null ? lastModified : created;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy != null ? lastModifiedBy : createdBy;
	}

	public String getName() {
		return resource.getName();
	}

	public Resource getParent() {
		return resource.getParent();
	}

	public String getPath() {
		return resource.getPath();
	}

	public ValueMap getProperties() {
		return getContentResource().getValueMap();
	}

	public abstract boolean isPublished();

	public Resource getResource() {
		return resource;
	}

	public String getTitle() {
		if (StringUtils.isNotEmpty(title)) {
			return title;
		} else {
			return resource.getName();
		}
	}

	public String getType() {
		return type;
	}
}
