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

import org.osgi.annotation.versioning.ProviderType;

/**
 * An interface representing a sling:Page resource. Adaptable from a sling:Page
 * Resource.
 */
@ProviderType
public interface Page extends PublishableResource {

    /**
     * Gets the keywords for the page
     * 
     * @return the page keywords in a string representation
     */
    String[] getKeywords();

    /**
     * Gets the sling:Template this page was configured with
     * 
     * @return the template for the page
     */
    PageTemplate getTemplate();

    /**
     * Gets the path to the template this page was configured with.
     * 
     * @return the path to the template
     */
    String getTemplatePath();

    /**
     * Gets the title of this page, will fall back to the name of the page
     * 
     * @return the title of the page or the name of the page if the title is not
     *         specified
     */
    String getTitle();
}
