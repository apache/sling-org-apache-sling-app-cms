/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.cms.insights;

/**
 * Interface for all of the Insight Provider services to implement.
 */
public interface InsightProvider {

    /**
     * Evaluate the request and return a response with the details.
     * 
     * @param request the request to evaluate
     * @return the insights gathered
     */
    Insight evaluateRequest(InsightRequest request);

    /**
     * Get the ID for a particular Insight Provider. This should be human readable
     * and URL-safe.
     * 
     * @return the check ID
     */
    String getId();

    /**
     * The title of the Insight Provider
     * 
     * @return the user-displayed title
     */
    String getTitle();

    /**
     * Returns true if the provider is enabled for this request and false otherwise.
     * 
     * @param request the request to evaluate
     * @return true if enabled, false otherwise
     */
    boolean isEnabled(InsightRequest request);
}
