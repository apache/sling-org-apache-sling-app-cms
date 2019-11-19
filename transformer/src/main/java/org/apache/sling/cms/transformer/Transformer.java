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

import org.apache.sling.api.resource.Resource;

/**
 * Transforms a resource using the registered TransformationHandlers to invoke
 * transformations on the file.
 */
public interface Transformer {

    /**
     * Transforms the resource
     * 
     * @param resource       the resource to transform
     * @param transformation the transformation to execute
     * @param format         the format of the stream to return
     * @param out            the OutputStream to which to write the transformed file
     * @throws IOException an exception occurs transforming the resource
     */
    void transform(Resource resource, Transformation transformation, OutputFileFormat format, OutputStream out)
            throws IOException;

}
