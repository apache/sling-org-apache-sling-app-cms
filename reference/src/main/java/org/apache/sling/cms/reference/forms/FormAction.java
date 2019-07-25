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

import org.apache.sling.api.resource.Resource;

/**
 * A service interface for registering form actions.
 */
public interface FormAction {

    /**
     * Handle a form submission. The form can be assumed to have been validated.
     * 
     * @param actionResource the configuration to use to configure the form action
     * @param request        the form request to handle
     * @return the result of the action
     * @throws FormException an exception occurs handling the form
     */
    FormActionResult handleForm(Resource actionResource, FormRequest request) throws FormException;

    /**
     * Checks if the the Form Action should handle the specified request and action
     * 
     * @param actionResource the resource to check
     * @return true if this FormAction should handle the configuration, false
     *         otherwise
     */
    boolean handles(Resource actionResource);
}
