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
package org.apache.sling.cms.core.internal.listeners;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.observation.ExternalResourceChangeListener;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.apache.sling.cms.File;
import org.apache.sling.cms.FileMetadataExtractor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Resource Change Listener which extracts the metadata from sling:Files when
 * they are uploaded.
 */
@Component(service = { FileMetadataExtractorListener.class, ResourceChangeListener.class,
        ExternalResourceChangeListener.class }, property = { ResourceChangeListener.CHANGES + "=ADDED",
                ResourceChangeListener.PATHS + "=/content",
                ResourceChangeListener.PATHS + "=/static" }, immediate = true)
public class FileMetadataExtractorListener implements ResourceChangeListener, ExternalResourceChangeListener {

    @Reference
    private FileMetadataExtractor extractor;

    @Reference
    private ResourceResolverFactory factory;

    private static final Logger log = LoggerFactory.getLogger(FileMetadataExtractorListener.class);

    @Override
    public void onChange(List<ResourceChange> changes) {
        Map<String, Object> serviceParams = new HashMap<>();
        serviceParams.put(ResourceResolverFactory.SUBSERVICE, "sling-cms-metadata");
        ResourceResolver serviceResolver = null;
        try {
            serviceResolver = factory.getServiceResourceResolver(serviceParams);
            for (ResourceChange rc : changes) {
                handleChange(serviceResolver, rc);
            }
        } catch (LoginException e) {
            log.error("Exception getting service user", e);
        } finally {
            if (serviceResolver != null) {
                serviceResolver.close();
            }
        }

    }

    private void handleChange(ResourceResolver serviceResolver, ResourceChange rc) {
        File changed = Optional.ofNullable(serviceResolver.getResource(rc.getPath())).map(r -> r.adaptTo(File.class))
                .orElse(null);
        try {
            if (changed != null) {
                extractor.updateMetadata(changed);
            } else {
                log.warn("Failed to get File from: {}", rc.getPath());
            }
        } catch (IOException t) {
            log.warn("Failed to extract metadata due to exception", t);
        }
    }

}
