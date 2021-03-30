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
package org.apache.sling.cms.core.internal.jobs;

import java.io.IOException;
import java.util.Collections;

import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.cms.File;
import org.apache.sling.cms.FileMetadataExtractor;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sling Job Consumer for extracting the metadata from a file
 */
@Component(service = { JobConsumer.class }, property = {
        JobConsumer.PROPERTY_TOPICS + "=" + FileMetadataExtractorConsumer.TOPIC })
public class FileMetadataExtractorConsumer implements JobConsumer {

    public static final Logger log = LoggerFactory.getLogger(FileMetadataExtractorConsumer.class);

    public static final String TOPIC = "org/apache/sling/cms/ExtractMetadata";

    @Reference
    private FileMetadataExtractor extractor;

    @Reference
    private ResourceResolverFactory factory;

    @Override
    public JobResult process(Job job) {
        String path = job.getProperty(SlingConstants.PROPERTY_PATH, String.class);
        try (ResourceResolver serviceResolver = factory.getServiceResourceResolver(
                Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, "sling-cms-metadata"))) {
            log.debug("Processing metadata for {}", path);
            Resource resource = serviceResolver.getResource(path);
            File file = resource.adaptTo(File.class);
            log.debug("Retrieved file {}", file);
            extractor.updateMetadata(file);
            log.debug("Metadata extracted successfully");
            return JobResult.OK;
        } catch (LoginException e) {
            log.error("Exception getting service user", e);
        } catch (IOException e) {
            log.error("Failed to extract metadata from {}", path, e);
        }
        return JobResult.FAILED;
    }

}
