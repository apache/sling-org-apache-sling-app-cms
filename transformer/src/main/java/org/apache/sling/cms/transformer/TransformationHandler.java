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

import net.coobird.thumbnailator.Thumbnails.Builder;

/*
 * Transformation handlers handle the transformation of files using the Thumbnails library.
 * Each transformation handler implements a transformation command using the specifed configuration.
 */
@SuppressWarnings("squid:S1214") // I don't like this rule...
public interface TransformationHandler {

    public static final String HANDLER_RESOURCE_TYPE = "handler.resourceType";

    /**
     * Get the resource type associated with this handler
     * 
     * @return the handler resource type
     */
    String getResourceType();

    /**
     * Handles the transformation of the file using the command values from the
     * suffix segment.
     * 
     * @param builder the Thumbnails builder to use / update
     * @param config  the configuration values for the transformation
     * @throws IOException an exception occurs transforming the file
     */
    void handle(Builder<? extends InputStream> builder, Resource config) throws IOException;

}
