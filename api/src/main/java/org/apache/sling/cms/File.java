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
 * An interface representing a sling:File resource. Adaptable from a sling:File
 * Resource.
 */
@ProviderType
public interface File {

    /**
     * Gets the path of this file after it is published on the site's primary domain
     * with the the html extension
     * 
     * @return the published path of the file
     */
    String getPublishedPath();

    /**
     * Gets the full url (including domain) of this file after it is published on
     * the site's primary domain
     * 
     * @return the published url of the file
     */
    String getPublishedUrl();

    /**
     * Gets the site this file is contained within
     * 
     * @return the site containing the file
     */
    Site getSite();

    /**
     * Retrieves the content Resource of the file
     * 
     * @return the jcr:content child resource of the file
     */
    Resource getContentResource();

    /**
     * Gets the date the file was created
     * 
     * @return the date on which the file was created
     */
    Calendar getCreated();

    /**
     * Gets the name of the user whom created this file
     * 
     * @return the name of the user whom created the file
     */
    String getCreatedBy();

    /**
     * Gets the last time this file was modified
     * 
     * @return the date the file was last modified
     */
    Calendar getLastModified();

    /**
     * Gets the username of the user who last modified the file
     * 
     * @return the name of the user who last modified the file
     */
    String getLastModifiedBy();

    /**
     * Retrieves the metadata extracted from the file.
     * 
     * @return the metadata extracted from the file
     */
    ValueMap getMetadata();

    /**
     * Gets the name of the Sling Resource backing the file
     * 
     * @return the name of the file resource
     */
    String getName();

    /**
     * Gets the parent of the current file
     * 
     * @return the parent of the file
     */
    Resource getParent();

    /**
     * Get the path this file resides within the repository
     * 
     * @return the path of the file
     */
    String getPath();

    /**
     * Gets a ValueMap of the properties of the content resource for this page
     * 
     * @return the properties of the content resource
     */
    ValueMap getProperties();

    /**
     * Returns true if the file is published, false otherwise
     * 
     * @return whether or not the file is published
     */
    boolean isPublished();

    /**
     * Gets the resource backing this file
     * 
     * @return the resource backing the file
     */
    Resource getResource();

    /**
     * Gets the content type of this file
     * 
     * @return the content type of the file
     */
    String getContentType();
}
