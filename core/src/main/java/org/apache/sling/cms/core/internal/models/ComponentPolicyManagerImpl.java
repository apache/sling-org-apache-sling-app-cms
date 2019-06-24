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
package org.apache.sling.cms.core.internal.models;

import java.util.Optional;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.ComponentPolicy;
import org.apache.sling.cms.ComponentPolicyManager;
import org.apache.sling.cms.Page;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = Resource.class, adapters = ComponentPolicyManager.class)
public class ComponentPolicyManagerImpl implements ComponentPolicyManager {

    private static final Logger log = LoggerFactory.getLogger(ComponentPolicyManagerImpl.class);

    private final ComponentPolicy componentPolicy;

    public ComponentPolicyManagerImpl(Resource resource) {
        componentPolicy = Optional.ofNullable(new PageManagerImpl(resource).getPage()).map(Page::getTemplate).map(t -> {
            log.debug("Loading policies for template: {}", t);
            return t.getComponentPolicies().stream().filter(cp -> cp.applies(resource)).findFirst().orElse(null);
        }).orElse(null);
        log.debug("Loaded component policy: {}", componentPolicy);
    }

    @Override
    public ComponentPolicy getComponentPolicy() {
        return componentPolicy;
    }

}
