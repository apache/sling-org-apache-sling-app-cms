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

import org.apache.sling.api.resource.Resource;

/**
 * Service interface for providing values to form fields. Classes implementing
 * this interface should make values available to the form fields via the Form
 * Data in the Form Request.
 */
public interface FormValueProvider {

    boolean handles(Resource valueProviderResource);

    /**
     * Populates the form values for a request.
     * 
     * @param valueProviderResource the resource for configuring this provider
     * @param formData              the map of data for the form
     */
    void loadValues(Resource valueProviderResource, Map<String, Object> formData);
}
