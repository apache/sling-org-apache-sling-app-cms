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

import java.util.List;
import java.util.Map;

import org.osgi.annotation.versioning.ProviderType;

/**
 * A interface for retrieving sling:Component resources and information about
 * the available sling:Components
 */
@ProviderType
public interface ComponentManager {

    /**
     * Returns all of the components in Sling CMS with a component type specified.
     * 
     * @return a list of the components
     */
    List<Component> getAllComponents();

    /**
     * Returns a Map of the components with a component type specified with the key
     * of the map being the component type and the value being the list of
     * components for that type.
     * 
     * @return the components organized by componentType
     */
    Map<String, List<Component>> getComponentsByType();

    /**
     * Returns a list of all of the registered component types
     * 
     * @return the list of component types
     */
    List<String> getComponentTypes();

}
