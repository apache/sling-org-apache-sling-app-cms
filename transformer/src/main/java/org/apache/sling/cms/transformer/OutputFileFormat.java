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

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;

import com.google.common.base.Enums;
import com.google.common.net.MediaType;

/**
 * Enumeration of the valid output formats for the thumbnail generator.
 */
public enum OutputFileFormat {
    GIF(MediaType.GIF.toString()), JPEG(MediaType.JPEG.toString()), PNG(MediaType.PNG.toString());

    /**
     * Loads the output format requested in the specified request suffix.
     * 
     * @param request the current request from which to get the suffix
     * @return the format for the suffix
     */
    public static OutputFileFormat forRequest(SlingHttpServletRequest request) {
        String suffixExtension = StringUtils.substringAfterLast(request.getRequestPathInfo().getSuffix(), ".")
                .toUpperCase();
        return Enums.getIfPresent(OutputFileFormat.class, suffixExtension).or(OutputFileFormat.JPEG);
    }

    private String mimeType;

    private OutputFileFormat(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }
}
