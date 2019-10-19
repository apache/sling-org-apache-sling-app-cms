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
package org.apache.sling.cms.core.internal.models;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.Component;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

/**
 * A model representing a sling:Component
 */
@Model(adaptables = Resource.class, adapters = Component.class)
public class ComponentImpl implements Component {

    @Inject
    @Optional
    @Named("componentType")
    private String[] componentType;

    @Inject
    @Optional
    @Default(booleanValues = false)
    private boolean container;

    @Inject
    @Optional
    @Default(booleanValues = true)
    private boolean editable;

    @Inject
    @Optional
    @Default(booleanValues = false)
    private boolean reloadPage;

    private Resource resource;

    @Inject
    @Optional
    @Named("jcr:title")
    private String title;

    public ComponentImpl(Resource resource) {
        this.resource = resource;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Component other = (Component) obj;
        if (resource == null) {
            if (other.getResource() != null) {
                return false;
            }
        } else if (!resource.getPath().equals(other.getResource().getPath())) {
            return false;
        }
        return true;
    }

    private Resource getComponentEditPath(Resource component) {
        if (component != null) {
            if (component.getChild("edit") != null) {
                return component.getChild("edit");
            } else {
                String parentResourceType = component.getResourceResolver().getParentResourceType(component);
                if (StringUtils.isNotBlank(parentResourceType)) {
                    component = component.getResourceResolver().getResource(parentResourceType);
                    if (component != null) {
                        return getComponentEditPath(component);
                    }
                }
            }
        }
        return null;
    }

    /**
     * @return the componentType
     */
    @Override
    public String[] getComponentType() {
        return componentType;
    }

    /**
     * Returns the path for the editor for this resource if available
     * 
     * @return the editor path or null
     */
    @Override
    public String getEditPath() {
        Resource editResource = getEditResource();
        return editResource != null ? editResource.getPath() : null;
    }

    /**
     * Returns the resource for the editor for this resource if available
     * 
     * @return the editor resource or null
     */
    @Override
    public Resource getEditResource() {
        return getComponentEditPath(resource);
    }

    /**
     * @return the resource
     */
    @Override
    public Resource getResource() {
        return resource;
    }

    /**
     * @return the title
     */
    @Override
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
        result = prime * result + resource.getPath().hashCode();
        return result;
    }

    @Override
    public boolean isContainer() {
        return container;
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public boolean isReloadPage() {
        return reloadPage;
    }

    /**
     * Returns true if the only component type on the component is the specified
     * type.
     * 
     * @param type the type of the resource to check
     * @return true if the resource is of the specified type
     */
    @Override
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

    @Override
    public String toString() {
        return "ComponentImpl [componentType=" + Arrays.toString(componentType) + ", editable=" + editable
                + ", container=" + container + ", reloadPage=" + reloadPage + ", resource=" + resource + ", title="
                + title + "]";
    }

}
