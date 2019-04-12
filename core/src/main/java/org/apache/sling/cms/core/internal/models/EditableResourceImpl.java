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

import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.Component;
import org.apache.sling.cms.EditableResource;
import org.apache.sling.models.annotations.Model;

/**
 * A model for representing a resource that can be edited through the Sling CMS.
 */
@Model(adaptables = Resource.class, adapters = EditableResource.class)
public class EditableResourceImpl implements EditableResource {

    private final Resource resource;

    public EditableResourceImpl(Resource resource) {
        this.resource = resource;
    }

    @Override
    public Component getComponent() {
        if (getComponentResource() != null) {
            return getComponentResource().adaptTo(Component.class);
        }
        return null;
    }

    /**
     * Gets the component for the specified resource.
     * 
     * @return the component for the specified resource
     */
    @Override
    public Resource getComponentResource() {
        String resourceType = resource.getResourceType();
        return resource.getResourceResolver().getResource(resourceType);
    }

    /**
     * Returns the path for the editor for this resource if available
     * 
     * @return the editor path or null
     */
    @Override
    public String getEditPath() {
        if (getComponent() != null) {
            return getComponent().getEditPath();
        }
        return null;
    }

    /**
     * Returns the resource for the editor for this resource if available
     * 
     * @return the editor resource or null
     */
    @Override
    public Resource getEditResource() {
        if (getComponent() != null) {
            return getComponent().getEditResource();
        }
        return null;
    }

    /**
     * Gets the current resource.
     * 
     * @return the current resource
     */
    @Override
    public Resource getResource() {
        return resource;
    }
}
