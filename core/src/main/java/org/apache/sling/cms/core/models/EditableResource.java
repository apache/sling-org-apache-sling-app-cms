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

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

/**
 * A model for representing a resource that can be edited through the Sling CMS.
 */
@Model(adaptables = Resource.class)
public class EditableResource {

	private final Resource resource;

	public EditableResource(Resource resource) {
		this.resource = resource;
	}

	private Resource getComponentEditPath(Resource component) {
		if (component != null) {
			if (component.getChild("edit") != null) {
				return component.getChild("edit");
			} else {
				component = component.getResourceResolver()
						.getResource(component.getResourceResolver().getParentResourceType(component));
				if (component != null) {
					return getComponentEditPath(component);
				}
			}
		}
		return null;
	}

	/**
	 * Returns the path for the editor for this resource if available
	 * 
	 * @return the editor path or null
	 */
	public String getEditPath() {
		Resource editResource = getEditResource();
		return editResource != null ? editResource.getPath() : null;
	}

	/**
	 * Returns the resource for the editor for this resource if available
	 * 
	 * @return the editor resource or null
	 */
	public Resource getEditResource() {
		Resource component = resource.getResourceResolver().getResource(resource.getResourceType());
		return getComponentEditPath(component);
	}

	/**
	 * Gets the current resource.
	 * 
	 * @return the current resource
	 */
	public Resource getResource() {
		return resource;
	}
}
