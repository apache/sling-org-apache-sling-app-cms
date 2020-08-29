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
package org.apache.sling.cms.publication;

import org.apache.sling.cms.PublishableResource;
import org.jetbrains.annotations.NotNull;

/**
 * Publication Manager is used to get the publication status of content and get
 * the instances' publication mode.
 * 
 * Adaptable from a ResourceResolver.
 * 
 * @see org.apache.sling.api.resource.ResourceResolver
 */
public interface PublicationManager {

    /**
     * Publishes the resource.
     * 
     * @param resource the resource to publish
     * @throws PublicationException an exception occurs publishing the resource
     */
    void publish(@NotNull PublishableResource resource) throws PublicationException;

    /**
     * Un-publishes the resource.
     * 
     * @param resource the resource to publish
     * @throws PublicationException an exception occurs publishing the resource
     */
    void unpublish(@NotNull PublishableResource resource) throws PublicationException;

    /**
     * The publication mode for the instance
     * 
     * @return the publication mode
     */
    @NotNull
    PUBLICATION_MODE getPublicationMode();

}
