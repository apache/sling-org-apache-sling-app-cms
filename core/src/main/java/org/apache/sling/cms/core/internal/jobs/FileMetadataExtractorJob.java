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

import java.util.ArrayList;
import java.util.List;

import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.ConfigurableJobExecutor;
import org.apache.sling.cms.File;
import org.apache.sling.cms.core.internal.listeners.FileMetadataExtractor;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.apache.sling.event.jobs.consumer.JobExecutionContext;
import org.apache.sling.event.jobs.consumer.JobExecutionResult;
import org.apache.sling.event.jobs.consumer.JobExecutor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the Configurable Job Executor interface for the file
 * metadata extractor
 */
@Component(service = { JobExecutor.class, ConfigurableJobExecutor.class }, property = {
        JobConsumer.PROPERTY_TOPICS + "=" + FileMetadataExtractorJob.TOPIC })
public class FileMetadataExtractorJob implements ConfigurableJobExecutor {

    public static final Logger log = LoggerFactory.getLogger(FileMetadataExtractorJob.class);

    public static final String TOPIC = "org/apache/sling/cms/file/ExtractMetadata";

    public static final String PN_RECURSIVE = "recursive";

    @Reference
    private FileMetadataExtractor extractor;

    @Reference
    private ResourceResolverFactory factory;

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.sling.cms.ConfigurableJobExecutor#getConfigurationPath()
     */
    @Override
    public String getConfigurationPath() {
        return "/mnt/overlay/sling-cms/content/jobs/filemetadataextractor";
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.sling.cms.ConfigurableJobExecutor#getTopic()
     */
    @Override
    public String getTopic() {
        return TOPIC;
    }

    @Override
    public JobExecutionResult process(Job job, JobExecutionContext context) {
        String path = job.getProperty(SlingConstants.PROPERTY_PATH, "");

        ResourceResolver resolver = null;

        try {
            resolver = factory.getServiceResourceResolver(null);

            Resource root = resolver.getResource(path);
            if (root != null) {
                List<File> files = new ArrayList<>();
                if (CMSConstants.NT_FILE.equals(root.getResourceType())) {
                    files.add(root.adaptTo(File.class));
                } else {
                    collectFiles(root, files);
                }
                context.log("Found {} files to extract metadata", files.size());

                context.initProgress(files.size(), -1);

                int processed = 1;
                for (File file : files) {
                    extractor.extractMetadata(file);
                    context.incrementProgressCount(processed++);
                    context.log("Extracted metadata for {}", file.getPath());
                }

                return context.result().message("Metadata Extracted").succeeded();
            } else {
                return context.result().message("No file found at " + path).failed();
            }
        } catch (LoginException e) {
            log.warn("Unable to get service user", e);
            return context.result().message("Unable to get service user").failed();
        } finally {
            if (resolver != null) {
                resolver.close();
            }
        }
    }

    private void collectFiles(Resource root, List<File> files) {
        for (Resource child : root.getChildren()) {
            if (CMSConstants.NT_FILE.equals(child.getResourceType())) {
                files.add(child.adaptTo(File.class));
            } else {
                collectFiles(child, files);
            }
        }
    }

    @Override
    public String getTitleKey() {
        return "slingcms.filemetadata.title";
    }

}
