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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.cms.ComponentPolicy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = Resource.class, adapters = ComponentPolicy.class)
public class ComponentPolicyImpl implements ComponentPolicy {

    private static final Logger log = LoggerFactory.getLogger(ComponentPolicyImpl.class);

    @ValueMapValue
    private String pathPattern;

    @ValueMapValue
    private String policyPath;

    private Optional<Resource> policyResource;

    private final ResourceResolver resolver;

    public ComponentPolicyImpl(Resource resource) {
        resolver = resource.getResourceResolver();

        log.debug("Initialized policy {} with pattern {} and path {}", resolver, pathPattern, policyPath);
    }

    @Override
    public boolean applies(Resource resource) {
        return Optional.ofNullable(new PageManagerImpl(resource).getPage())
                .map(p -> StringUtils.removeStart(resource.getPath(), p.getPath())).map(p -> p.matches(pathPattern))
                .orElse(false);
    }

    @Override
    public String[] getAvailableComponentTypes() {
        return policyResource.map(pr -> pr.getValueMap().get("availableComponentTypes", new String[0]))
                .orElse(new String[0]);
    }

    @Override
    public Map<String, Resource> getComponentConfigs() {
        Map<String, Resource> configs = new HashMap<>();
        Resource container = policyResource.map(pr -> pr.getChild("componentConfigurations")).orElse(null);
        if (container != null) {
            container.getChildren().forEach(c -> configs.put(c.getValueMap().get("type", String.class), c));
        }
        log.debug("Loaded configurations for components: {}", configs.keySet());

        return configs;
    }

    @PostConstruct
    public void init() {
        policyResource = Optional.ofNullable(resolver.getResource(policyPath));
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ComponentPolicyImpl [pathPattern=" + pathPattern + ", policyPath=" + policyPath + ", resolver="
                + resolver + ", policyResource=" + policyResource + "]";
    }

}
