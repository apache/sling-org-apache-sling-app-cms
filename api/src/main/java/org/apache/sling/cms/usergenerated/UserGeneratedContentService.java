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
package org.apache.sling.cms.usergenerated;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.osgi.annotation.versioning.ProviderType;

/**
 * Service for creating User Generated Content
 */
@ProviderType
public interface UserGeneratedContentService {

    public enum APPROVE_ACTION {
        MOVE, PUBLISH
    }

    enum CONTENT_TYPE {
        COMMENT, FORUM_POST, REPLY, BLOG_POST, CONTACT_FORM, SIGNUP, MESSAGE, OTHER
    }

    /**
     * Creates a new container for adding user generated content which should be
     * published when approved. The UGC content should be added as a child of this
     * container.
     * 
     * @param request      the request for which this was initiated
     * @param bucketConfig the configuration for the UGC bucket
     * @param preview      the preview of the UGC to be displayed to the approving
     *                     user
     * @param targetPath   the path to which to move the content if this should be
     *                     moved when approved, may be null
     * @return the new UGC Container
     * @throws PersistenceException an exception occurs creating the UGC Container
     */
    Resource createUGCContainer(SlingHttpServletRequest request, UGCBucketConfig bucketConfig, String preview,
            String targetPath) throws PersistenceException;

}
