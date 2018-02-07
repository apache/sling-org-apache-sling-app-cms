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

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

/**
 * A model representing a sling:Component
 */
@Model(adaptables = Resource.class)
public class Component {

	@Inject
	@Optional
	@Named("componentType")
	private String[] componentType;

	private Resource resource;

	@Inject
	@Optional
	@Named("jcr:title")
	private String title;

	public Component(Resource resource) {
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
		Component other = (Component) obj;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.getPath().equals(other.resource.getPath()))
			return false;
		return true;
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
	 * @return the componentType
	 */
	public String[] getComponentType() {
		return componentType;
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
		return getComponentEditPath(resource);
	}

	/**
	 * @return the resource
	 */
	public Resource getResource() {
		return resource;
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
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((resource.getPath() == null) ? 0 : resource.getPath().hashCode());
		return result;
	}

	/**
	 * Returns true if the only component type on the component is the specified
	 * type.
	 * 
	 * @param string
	 * @return
	 */
	public boolean isType(String type) {
		boolean isType = false;
		if (this.getComponentType() != null && ArrayUtils.contains(this.getComponentType(), type)) {
			isType = true;
			for (String t : getComponentType()) {
				if (StringUtils.isNotBlank(t) && !type.equals(t)) {
					isType = false;
				}
			}
		}
		return isType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Component [title=" + title + ", resource=" + resource + ", componentType=" + componentType + "]";
	}

}
