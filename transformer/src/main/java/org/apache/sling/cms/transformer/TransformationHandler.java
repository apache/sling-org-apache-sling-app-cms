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

import net.coobird.thumbnailator.Thumbnails.Builder;

/*
 * Transformation handlers handle the transformation of thumbnails using the Thumbnails library.
 * Each transformation handler implements a named transformation command which will be parsed out 
 * of the suffix of the transformation request by splitting the suffix by slashes and checking the 
 * "applies" method of each TransformationHandler, in order. 
 */
public interface TransformationHandler {

    /**
     * Returns true if the transformation handler should execute for the specified
     * command.
     * 
     * @param command the command to check
     * @return true if the handler will handle this, false otherwise
     */
    boolean applies(String command);

    /**
     * Handles the transformation of the thumbnail using the command values from the
     * suffix segment.
     * 
     * @param builder the Thumbnails builder to use / update
     * @param cmd     the command to parse to retrieve the configuration values for
     *                the transformation
     * @throws IOException an exception occurs transforming the thumbnail
     */
    void handle(Builder<? extends InputStream> builder, String cmd) throws IOException;

}
