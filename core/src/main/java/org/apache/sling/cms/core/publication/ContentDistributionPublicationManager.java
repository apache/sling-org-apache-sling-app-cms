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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.cms.PublishableResource;
import org.apache.sling.cms.publication.PUBLICATION_MODE;
import org.apache.sling.cms.publication.PublicationException;
import org.apache.sling.distribution.DistributionRequest;
import org.apache.sling.distribution.DistributionRequestType;
import org.apache.sling.distribution.DistributionResponse;
import org.apache.sling.distribution.Distributor;
import org.apache.sling.distribution.SimpleDistributionRequest;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the PublicationManager interface using Sling Content
 * Distribution.
 */
public class ContentDistributionPublicationManager extends StandalonePublicationManager {

    private static final Logger log = LoggerFactory.getLogger(ContentDistributionPublicationManager.class);

    private final Distributor distributor;
    private final String[] agents;

    public ContentDistributionPublicationManager(Distributor distributor, String[] agents, EventAdmin eventAdmin) {
        super(eventAdmin);
        this.distributor = distributor;
        this.agents = agents;

    }

    @Override
    public void publish(PublishableResource resource) throws PublicationException {
        log.info("Publishing: {}", resource.getPath());
        DistributionRequest request = new SimpleDistributionRequest(DistributionRequestType.ADD,
                new String[] { resource.getPath(), resource.getContentResource().getPath() },
                Collections.singleton(resource.getContentResource().getPath()));
        List<DistributionResponse> failedResponses = this
                .distributeRequest(resource.getResource().getResourceResolver(), request);
        if (!failedResponses.isEmpty()) {
            throw new PublicationException("Failed to publish: " + collectFailures(failedResponses));
        } else {
            log.debug("Content Distribution successful, updating publication information");
            super.publish(resource);
        }
    }

    private String collectFailures(List<DistributionResponse> failedResponses) {
        return failedResponses.stream().map(r -> r.getState() + ": " + r.getMessage())
                .collect(Collectors.joining(",", "[", "]"));
    }

    private List<DistributionResponse> distributeRequest(ResourceResolver resolver, DistributionRequest request) {
        return Arrays.stream(agents).map(a -> {
            log.info("Sending to agent: {}", a);
            DistributionResponse response = distributor.distribute(a, resolver, request);
            log.debug("Retrieved response [{}]: {}", response.getState(), response.getMessage());
            return response;
        }).filter(res -> !res.isSuccessful()).collect(Collectors.toList());
    }

    @Override
    public void unpublish(PublishableResource resource) throws PublicationException {
        log.info("Unpublish: {}", resource.getPath());
        DistributionRequest request = new SimpleDistributionRequest(DistributionRequestType.DELETE,
                new String[] { resource.getPath(), resource.getContentResource().getPath() },
                Collections.singleton(resource.getContentResource().getPath()));
        List<DistributionResponse> failedResponses = this
                .distributeRequest(resource.getResource().getResourceResolver(), request);
        if (!failedResponses.isEmpty()) {
            throw new PublicationException("Failed to unpublish: " + collectFailures(failedResponses));
        } else {
            super.unpublish(resource);
        }
    }

    @Override
    public PUBLICATION_MODE getPublicationMode() {
        return PUBLICATION_MODE.CONTENT_DISTRIBUTION;
    }

}