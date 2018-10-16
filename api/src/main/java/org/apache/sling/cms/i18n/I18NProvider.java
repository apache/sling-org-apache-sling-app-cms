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
package org.apache.sling.cms.i18n;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;

/**
 * Service interface for retrieving an i18n dictionary.
 */
public interface I18NProvider {

    /**
     * Get the appropriate dictionary for the request. Intended to be invoked by
     * methods providing i18n keys to an end user.
     * 
     * @param request the current request
     * @return the i18nDictionary for the request
     */
    I18NDictionary getDictionary(SlingHttpServletRequest request);

    /**
     * Get the appropriate dictionary for the resource resolver. Intended to be
     * invoked by methods providing i18n keys a CMS user / author / admin.
     * 
     * @param resolver the current resource resolver
     * @return the i18nDictionary for the resolver
     */
    I18NDictionary getDictionary(ResourceResolver resolver);
}
