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
import org.osgi.annotation.versioning.ProviderType;

/**
 * A interface for representing a resource that can be edited through the Sling
 * CMS.
 */
@ProviderType
public interface EditableResource {

    /**
     * Get the component associated with this resource
     * 
     * @return the component associated with this resource
     */
    Component getComponent();

    /**
     * Gets the resource backing the component for the specified resource.
     * 
     * @return the component for the specified resource
     */
    Resource getComponentResource();

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
     * Gets the current resource.
     * 
     * @return the current resource
     */
    Resource getResource();
}
