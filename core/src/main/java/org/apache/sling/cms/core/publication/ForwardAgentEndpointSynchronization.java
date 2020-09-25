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
package org.apache.sling.cms.core.publication;

import java.io.IOException;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.sling.cms.publication.INSTANCE_TYPE;
import org.apache.sling.discovery.InstanceDescription;
import org.apache.sling.discovery.TopologyEvent;
import org.apache.sling.discovery.TopologyEventListener;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = TopologyEventListener.class, immediate = true)
@Designate(ocd = ForwardAgentEndpointSynchronizationConfig.class)
public class ForwardAgentEndpointSynchronization implements TopologyEventListener {

    private static final Logger log = LoggerFactory.getLogger(ForwardAgentEndpointSynchronization.class);

    private ForwardAgentEndpointSynchronizationConfig config;

    public static final String ENDPOINT_PROPERTY = "packageImporter.endpoints";

    private ConfigurationAdmin configAdmin;

    @Activate
    public ForwardAgentEndpointSynchronization(@Reference ConfigurationAdmin configAdmin,
            ForwardAgentEndpointSynchronizationConfig config) {
        this.configAdmin = configAdmin;
        this.config = config;
    }

    private void updateInstances(Set<InstanceDescription> instances) {
        log.info("updateInstances");

        String[] endpoints = instances.stream().map(id -> {
            String endpointBase = id.getProperty(InstanceDescription.PROPERTY_ENDPOINTS).split("\\,")[0];
            return endpointBase + id.getProperty(PublicationPropertyProvider.ENDPOINT_PATHS);
        }).collect(Collectors.toList()).toArray(new String[0]);
        if (log.isDebugEnabled()) {
            log.debug("Updating with endpoints: [{}]", Arrays.stream(endpoints).collect(Collectors.joining(",")));
        }
        try {
            log.debug("Updating configurations matching: {}", config.agentTarget());
            Configuration[] configurations = configAdmin.listConfigurations(
                    "(&(service.factoryPid=org.apache.sling.distribution.agent.impl.ForwardDistributionAgentFactory)"
                            + config.agentTarget() + ")");
            if (configurations != null) {
                for (Configuration cfg : configurations) {
                    log.debug("Updating configuration {}", cfg.getPid());
                    Dictionary<String, Object> properties = cfg.getProperties();
                    if (!Arrays.equals(endpoints, (String[]) properties.get(ENDPOINT_PROPERTY))) {
                        properties.put(ENDPOINT_PROPERTY, endpoints);
                        cfg.update(properties);
                        log.debug("Configurations updated!");
                    } else {
                        log.debug("Configurations match, not updating");
                    }
                }
            } else {
                log.warn("No applicable configurations found");
            }
        } catch (IOException | InvalidSyntaxException e) {
            log.error("Failed to update configuration", e);
        }
    }

    @Override
    public void handleTopologyEvent(TopologyEvent event) {
        Set<InstanceDescription> renderers = event.getNewView().findInstances(id -> INSTANCE_TYPE.RENDERER.toString()
                .equals(id.getProperty(PublicationPropertyProvider.INSTANCE_TYPE)));
        updateInstances(renderers);
    }

}
