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
package org.apache.sling.cms;

import org.apache.sling.api.resource.Resource;
import org.jetbrains.annotations.NotNull;
import org.osgi.annotation.versioning.ProviderType;

/**
 * A interface to represent a sling:Component
 */
@ProviderType
public interface Component {

    /**
     * Returns the type of the component. A component can belong to multiple types
     * 
     * @return the component type
     */
    @NotNull
    String[] getComponentType();

    /**
     * Returns the path for the editor for this resource if available
     * 
     * @return the editor path or null
     */
    String getEditPath();

    /**
     * Returns the resource for the editor for this resource if available
     * 
     * @return the editor resource or null
     */
    Resource getEditResource();

    /**
     * The underlying Sling Resource
     * 
     * @return the resource
     */
    Resource getResource();

    /**
     * Get the title of the component
     * 
     * @return the title
     */
    String getTitle();

    /**
     * True if the component is a container, false otherwise.
     * 
     * @return the container flag
     */
    boolean isContainer();

    /**
     * True if the component is editable, false otherwise.
     * 
     * @return the editable flag
     */
    boolean isEditable();

    /**
     * Returns true if the the property reloadPage is set to true.
     * 
     * @return true if page should be reloaded when the component is saved
     */
    boolean isReloadPage();

    /**
     * Returns true if the only component type on the component is the specified
     * type.
     * 
     * @param type the type of the resource to check
     * @return true if the resource is of the specified type
     */
    boolean isType(String type);

}
