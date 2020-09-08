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

import java.util.Collections;
import java.util.stream.Stream;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.cms.ConfigurableJobExecutor;
import org.apache.sling.cms.PublishableResource;
import org.apache.sling.cms.ResourceTree;
import org.apache.sling.cms.publication.IsPublishableResourceContainer;
import org.apache.sling.cms.publication.IsPublishableResourceType;
import org.apache.sling.cms.publication.PublicationException;
import org.apache.sling.cms.publication.PublicationManager;
import org.apache.sling.cms.publication.PublicationManagerFactory;
import org.apache.sling.cms.publication.PublicationType;
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
 * A Sling Post Operation to unpublish content
 */
@Component(service = { JobExecutor.class, ConfigurableJobExecutor.class }, property = {
        JobConsumer.PROPERTY_TOPICS + "=" + BulkPublicationJob.TOPIC })
public class BulkPublicationJob extends ConfigurableJobExecutor {

    private static final Logger log = LoggerFactory.getLogger(BulkPublicationJob.class);

    public static final String TOPIC = "cmsjob/org/apache/sling/cms/publication/Bulk";

    @Reference
    private PublicationManagerFactory publicationManagerFactory;

    @Reference
    private ResourceResolverFactory factory;

    @Override
    public JobExecutionResult doProcess(Job job, JobExecutionContext context, ResourceResolver resolver) {
        String[] paths = job.getProperty("paths", String[].class);
        PublicationType type = PublicationType.valueOf(job.getProperty("type", String.class));
        boolean deep = job.getProperty("deep", false);

        PublicationManager publicationManager = publicationManagerFactory.getPublicationManager();

        context.initProgress(paths.length, paths.length * 5000L);

        log.info("Starting bulk publication: paths={}, type={}, deep={}", paths, type, deep);

        for (String path : paths) {
            Stream<PublishableResource> toPublish = null;
            Resource resource = resolver.getResource(path);
            if (deep) {
                toPublish = ResourceTree
                        .stream(resource, new IsPublishableResourceContainer(), new IsPublishableResourceType())
                        .map(rt -> rt.getResource().adaptTo(PublishableResource.class));
            } else {
                toPublish = Collections.singletonList(resource.adaptTo(PublishableResource.class)).stream();
            }
            toPublish.forEach(pr -> {
                try {
                    if (type == PublicationType.ADD) {
                        publicationManager.publish(pr);
                    } else {
                        publicationManager.unpublish(pr);
                    }
                    context.log("{0} complete for {1}", type, pr.getPath());
                } catch (PublicationException e) {
                    context.log("{0} failed for {1}", type, pr.getPath());
                    log.warn("Failed to publish {}", pr, e);
                }
            });

            context.log("Publication complete for path: {0}", path);
            context.incrementProgressCount(1);
        }

        context.log("Publication complete!");
        return context.result().succeeded();
    }

    @Override
    public String getConfigurationPath() {
        return "/mnt/overlay/sling-cms/content/publication/bulk";
    }

    @Override
    public ResourceResolverFactory getResolverFactory() {
        return factory;
    }

    @Override
    public String getTitleKey() {
        return "slingcms.bulkpublication.title";
    }

    @Override
    public String getTopic() {
        return TOPIC;
    }
}