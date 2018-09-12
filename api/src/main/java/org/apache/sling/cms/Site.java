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

import java.util.Locale;

import org.apache.sling.api.resource.Resource;
import org.osgi.annotation.versioning.ProviderType;

/**
 * A inteface representing a sling:Site. Adaptable from a sling:Site Resource.
 */
@ProviderType
public interface Site {

    /**
     * Gets the description of the site.
     * 
     * @return the description of the site
     */
    String getDescription();

    /**
     * Gets the locale for the sites
     * 
     * @return the locale for the site
     */
    Locale getLocale();

    /**
     * Gets the locale as it is stored in the Sling repository
     * 
     * @return the string form of the site locale
     */
    String getLocaleString();

    /**
     * Gets the path of the site
     * 
     * @return the path of the site
     */
    String getPath();

    /**
     * Gets the resource backing the site
     * 
     * @return the resource at the site path
     */
    Resource getResource();

    /**
     * Gets the title of the site
     * 
     * @return the title of the site
     */
    String getTitle();

    /**
     * Gets the "primary" URL for the site as configured
     * 
     * @return the primary url for the site
     */
    String getUrl();

}
