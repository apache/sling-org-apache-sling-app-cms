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

import java.util.Map;

import org.apache.sling.api.resource.Resource;

/**
 * A class to represent a component policy, e.g. the definition of what
 * components are available for a particular container and their shared
 * configurations.
 */
public interface ComponentPolicy {

    /**
     * Returns true if the ComponentPolicy applies to the specified resource
     * 
     * @param resource the resource to check
     * @return true if this policy should be enabled for the resource, false if not
     */
    boolean applies(Resource resource);

    /**
     * Gets the list of Component Types which will be available when using this
     * component policy
     * 
     * @return the available ComponentTypes
     */
    String[] getAvailableComponentTypes();

    /**
     * Gets the Component Configurations when using this component policy
     * 
     * @return the componentConfigs
     */
    Map<String, Resource> getComponentConfigs();

}
