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
package org.apache.sling.cms.reference;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;

/**
 * Service for managing the search user.
 */
public interface SearchService {

	/**
	 * Closes the resource resolver if appropriate
	 * 
	 * @param resolver the resource resolver used in search
	 */
	void closeResolver(ResourceResolver resolver);

	/**
	 * Gets either the service user resource resolver of the request resource
	 * resolver depending if the service user is configured.
	 * 
	 * @param request the request from which to retrieve the request resource
	 *                resolver (if service user not configured)
	 * @return the appropriate resource resolvers
	 */
	public ResourceResolver getResourceResolver(SlingHttpServletRequest request);
}
