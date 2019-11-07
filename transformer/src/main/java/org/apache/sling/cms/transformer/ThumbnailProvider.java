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
package org.apache.sling.cms.transformer;

import java.io.IOException;
import java.io.InputStream;

import org.apache.sling.api.resource.Resource;

/**
 * Service for retrieving a thumbnail for the specified Resource.
 */
public interface ThumbnailProvider {

    /**
     * Returns true if the ThumbnailProvider applies for the specified resource.
     * 
     * @param resource the resource to check
     * @return true if this ThumbnailProvider will create a thumbnail for this
     *         resource, false otherwise
     */
    boolean applies(Resource resource);

    /**
     * Get the thumbnail from the specified resource.
     * 
     * @param resource the resource from which to retrieve the thumbnail
     * @return the thumbnail
     * @throws IOException an exception occurs retrieving the thumbnail
     */
    InputStream getThumbnail(Resource resource) throws IOException;

}