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
import org.apache.sling.cms.publication.PublicationType;

public interface PublishableResource {

    /**
     * Gets the path of this page after it is published on the site's primary domain
     * with the the html extension
     * 
     * @return the published path of the content
     */
    String getPublishedPath();

    /**
     * Gets the full url (including domain) of this page after it is published on
     * the site's primary domain with the the html extension
     * 
     * @return the published url of the content
     */
    String getPublishedUrl();

    /**
     * Gets the site this file is contained within
     * 
     * @return the site containing the content
     */
    Site getSite();

    /**
     * Retrieves the content Resource of the content
     * 
     * @return the jcr:content child resource of the content
     */
    Resource getContentResource();

    /**
     * Gets the date the content was created
     * 
     * @return the date on which the content was created
     */
    Calendar getCreated();

    /**
     * Gets the name of the user whom created this file
     * 
     * @return the name of the user whom created the content
     */
    String getCreatedBy();

    /**
     * Gets the last time this file was modified
     * 
     * @return the date the content was last modified
     */
    Calendar getLastModified();

    /**
     * Gets the username of the user who last modified the content
     * 
     * @return the name of the user who last modified the content
     */
    String getLastModifiedBy();

    /**
     * The last publication action type
     * 
     * @return the last publication type
     */
    PublicationType getLastPublicationType();

    /**
     * Gets the name of the Sling Resource backing the content
     * 
     * @return the name of the content resource
     */
    String getName();

    /**
     * Gets the parent of the current page. This will generally be another
     * sling:Page or sling:Site
     * 
     * @return the parent of the content, may not be another page
     */
    Resource getParent();

    /**
     * Get the path this page resides within the repository
     * 
     * @return the path of the content
     */
    String getPath();

    /**
     * Gets a ValueMap of the properties of the content resource for this page
     * 
     * @return the properties of the content resource
     */
    ValueMap getProperties();

    /**
     * Returns true if the content is published, false otherwise
     * 
     * @return whether or not the content is published
     */
    boolean isPublished();

    /**
     * Gets the resource backing this file
     * 
     * @return the resource backing the content
     */
    Resource getResource();

    /**
     * Gets the date of the last publication action
     * 
     * @return the date of the publication action
     */
    Calendar getLastPublication();

    /**
     * Get the name of the user who last published the page.
     * 
     * @return the user name or null
     */
    String getLastPublicationBy();

}