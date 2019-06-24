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

import java.util.Collections;
import java.util.Optional;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.cms.ComponentConfiguration;
import org.apache.sling.cms.ComponentPolicyManager;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = Resource.class, adapters = ComponentConfiguration.class)
public class ComponentConfigurationImpl implements ComponentConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ComponentConfigurationImpl.class);

    private Optional<Resource> configRsrc;

    public ComponentConfigurationImpl(Resource resource) {
        configRsrc = Optional.ofNullable(resource.adaptTo(ComponentPolicyManager.class))
                .map(ComponentPolicyManager::getComponentPolicy)
                .map(p -> p.getComponentConfigs().get(resource.getResourceType()));
        log.debug("Loaded configuration resource: {}", configRsrc);
    }

    @Override
    public ValueMap getProperties() {
        return configRsrc.map(Resource::getValueMap).orElse(new ValueMapDecorator(Collections.emptyMap()));
    }

    @Override
    public Resource getResource() {
        return configRsrc.orElse(null);
    }

}
