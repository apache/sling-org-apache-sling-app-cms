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

import java.io.IOException;
import java.util.Map;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Service for extracting metadata from a file
 */
@ProviderType
public interface FileMetadataExtractor {

    /**
     * Extract the metadata from the specified file and return the resulting
     * metadata
     * 
     * @param file the file from which to extract the metadata
     * @return the metadata from the file
     * @throws IOException an exception occurs extracting the metadata
     */
    Map<String,Object> extractMetadata(File file) throws IOException;

    /**
     * Extract the metadata from the specified file and persist the results under
     * the jcr:content/metadata node of the file resource
     * 
     * @param file the file to extract the metadata from
     * @throws IOException an exception occurs updating the metadata
     */
    void updateMetadata(File file) throws IOException;

    /**
     * Extract the metadata from the specified file and persist the results under
     * the jcr:content/metadata node of the file resource
     * 
     * @param file the file to extract the metadata from
     * @param persist if true, persist the results, if not leave the changes
     *             unpersisted
     * @throws IOException an exception occurs updating the metadata
     */
    void updateMetadata(File file, boolean persist) throws IOException;
}
