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

import java.util.Calendar;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.annotation.versioning.ProviderType;

/**
 * An interface representing a sling:Page resource. Adaptable from a sling:Page
 * Resource.
 */
@ProviderType
public interface Page {

    /**
     * Gets the keywords for the page
     * 
     * @return the page keywords in a string representation
     */
    String[] getKeywords();

    /**
     * Gets the path of this page after it is published on the site's primary domain
     * with the the html extension
     * 
     * @return the published path of the page
     */
    String getPublishedPath();

    /**
     * Gets the full url (including domain) of this page after it is published on
     * the site's primary domain with the the html extension
     * 
     * @return the published url of the page
     */
    String getPublishedUrl();

    /**
     * Gets the site this page is contained within
     * 
     * @return the site containing the page
     */
    Site getSite();

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
     * Retrieves the content Resource of the page
     * 
     * @return the jcr:content child resource of the page
     */
    Resource getContentResource();

    /**
     * Gets the date the page was created
     * 
     * @return the date on which the page was created
     */
    Calendar getCreated();

    /**
     * Gets the name of the user whom created this page
     * 
     * @return the name of the user whom created the page
     */
    String getCreatedBy();

    /**
     * Gets the last time this page was modified
     * 
     * @return the date the page was last modified
     */
    Calendar getLastModified();

    /**
     * Gets the username of the user who last modified the page
     * 
     * @return the name of the user who last modified the page
     */
    String getLastModifiedBy();

    /**
     * Gets the name of the Sling Resource backing the page
     * 
     * @return the name of the page resource
     */
    String getName();

    /**
     * Gets the parent of the current page. This will generally be another
     * sling:Page or sling:Site
     * 
     * @return the parent of the page, may not be another page
     */
    Resource getParent();

    /**
     * Get the path this page resides within the repository
     * 
     * @return the path of the page
     */
    String getPath();

    /**
     * Gets a ValueMap of the properties of the content resource for this page
     * 
     * @return the properties of the content resource
     */
    ValueMap getProperties();

    /**
     * Returns true if the page is published, false otherwise
     * 
     * @return whether or not the page is published
     */
    boolean isPublished();

    /**
     * Gets the resource backing this pages
     * 
     * @return the resource backing the page
     */
    Resource getResource();

    /**
     * Gets the title of this page, will fall back to the name of the page
     * 
     * @return the title of the page or the name of the page if the title is not
     *         specified
     */
    String getTitle();
}
