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
import java.io.OutputStream;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.cms.File;

/**
 * Transforms a Sling File into thumbnails using the registered
 * TransformationHandlers to invoke Thumbnails transformations on the file.
 */
public interface FileThumbnailTransformer {

    /**
     * Gets the transformation handler for the specified command
     * 
     * @param command the command string to use to look up the transformation
     *                handler.
     * @return the TransformationHandler from the command string or null if none
     *         found
     */
    TransformationHandler getTransformationHandler(String command);

    /**
     * Transforms the file into a thumbnail using the specified commands.
     * 
     * @param commands the commands to execute
     * @param format   the format of the file to return
     * @param out      the Outputstream to write the thumbnail to
     * @throws IOException an exception occurs transforming the file
     */
    void transformFile(File file, String[] commands, OutputFileFormat format, OutputStream out) throws IOException;

    /**
     * Transforms the file from the resource path into a thumbnail using the
     * specified commands from the suffix.
     * 
     * @param request the request to parse the file and commands from
     * @param out     the Outputstream to write the thumbnail to
     * @throws IOException an exception occurs transforming the file
     */
    void transformFile(SlingHttpServletRequest request, OutputStream out) throws IOException;

}
