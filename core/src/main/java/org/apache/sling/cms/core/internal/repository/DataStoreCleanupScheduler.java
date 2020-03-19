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
package org.apache.sling.cms.core.internal.repository;

import java.util.Optional;

import javax.management.openmbean.CompositeData;

import org.apache.jackrabbit.oak.api.jmx.RepositoryManagementMBean;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.event.jobs.consumer.JobExecutor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

/**
 * Service for running the Jackrabbit OAK Segment Store cleanup on a schedule.
 */
@Component(service = { JobExecutor.class, Runnable.class }, property = { JobExecutor.PROPERTY_TOPICS
        + "=org/apache/sling/cms/repository/DataStoreCleanup" }, configurationPolicy = ConfigurationPolicy.REQUIRE, immediate = true)
@Designate(ocd = DataStoreCleanupConfig.class)
public class DataStoreCleanupScheduler extends AbstractMaintenanceJob {

    private RepositoryManagementMBean repositoryManager;

    @Override
    public String getJobTopic() {
        return "org/apache/sling/cms/repository/DataStoreCleanup";
    }

    @Override
    public String getPrefix() {
        return "DataStore Cleanup";
    }

    @Override
    public Optional<CompositeData> getStatus() {
        return Optional.ofNullable(repositoryManager.getDataStoreGCStatus());
    }

    @Reference
    @Override
    public void setJobManager(final JobManager jobManager) {
        super.jobManager = jobManager;
    }

    @Reference
    public void setRepositoryManager(final RepositoryManagementMBean repositoryManager) {
        this.repositoryManager = repositoryManager;
    }

    @Override
    public Optional<CompositeData> startMaintenance() {
        return Optional.ofNullable(repositoryManager.startDataStoreGC(false));
    }

    @Override
    public Optional<CompositeData> stopMaintenance() {
        // Can't really stop this one
        return Optional.ofNullable(repositoryManager.getDataStoreGCStatus());
    }

}
