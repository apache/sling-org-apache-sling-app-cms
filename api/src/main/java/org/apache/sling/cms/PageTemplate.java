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

import org.apache.sling.api.resource.Resource;
import org.osgi.annotation.versioning.ProviderType;

/**
 * An interface for representing a page template. Adaptable from a
 * sling:Template Resource.
 */
@ProviderType
public interface PageTemplate {

    /**
     * Gets the paths under which pages for this template can be created.
     * 
     * @return the allowedPaths
     */
    String[] getAllowedPaths();

    /**
     * Gets the list of Component Policies for pages created with this template
     * 
     * @return the component policies
     */
    List<ComponentPolicy> getComponentPolicies();

    /**
     * Gets the Resource backing this template
     * 
     * @return the resource
     */
    Resource getResource();

    /**
     * Gets the Handlebars template to use for creating the content of the page
     * 
     * @return the template
     */
    String getTemplate();

    /**
     * Gets the title of the template
     * 
     * @return the title
     */
    String getTitle();

}
