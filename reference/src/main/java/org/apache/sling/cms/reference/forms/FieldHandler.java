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
package org.apache.sling.cms.reference.forms;

import java.util.Map;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;

/**
 * A service interface for registering services to handle a form field.
 */
public interface FieldHandler {

    /**
     * Returns true if the FieldHandler should handle the field resource
     * 
     * @param fieldResource the field resource to handle
     * @return true if the FieldHandler will handle, false otherwise
     */
    boolean handles(Resource fieldResource);

    /**
     * Handle the field being submitted. Uses the configuration from the
     * fieldResource and the data from the request and saves the value into the
     * formData.
     * 
     * @param request       the request for the form submission
     * @param fieldResource the resource from which to get the field configuration
     * @param formData      the Map to which to save the data for the field
     * @throws FormException an exception occurs attempting to handle the field
     *                       including the field not being set or being invalid
     */
    void handleField(SlingHttpServletRequest request, Resource fieldResource, Map<String, Object> formData)
            throws FormException;

    static boolean isRequired(Resource fieldResource) {
        return fieldResource.getValueMap().get("required", false);
    }

    static String getName(Resource fieldResource) {
        return fieldResource.getValueMap().get("name", String.class);
    }
}
