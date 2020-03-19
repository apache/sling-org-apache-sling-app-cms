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

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import javax.management.openmbean.CompositeData;

import org.apache.jackrabbit.oak.api.jmx.RepositoryManagementMBean.StatusCode;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.event.jobs.consumer.JobExecutionContext;
import org.apache.sling.event.jobs.consumer.JobExecutionContext.ResultBuilder;
import org.apache.sling.event.jobs.consumer.JobExecutionResult;
import org.apache.sling.event.jobs.consumer.JobExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for running the Jackrabbit OAK Segment Store cleanup on a schedule.
 */
public abstract class AbstractMaintenanceJob implements Runnable, JobExecutor {

    private static final Logger log = LoggerFactory.getLogger(AbstractMaintenanceJob.class);

    protected JobManager jobManager;

    protected final JobExecutionResult createResult(JobExecutionContext context, Optional<CompositeData> data,
            Integer startId) {
        String message = data.map(d -> ((String) d.get("message"))).orElse(null);
        StatusCode code = data.map(d -> ((Integer) d.get("code"))).map(c -> Arrays.stream(StatusCode.values())
                .filter(sc -> sc.ordinal() == c).findFirst().orElse(StatusCode.NONE)).orElse(StatusCode.NONE);
        log.trace("Loaded status code: {}", code);
        Integer id = data.map(d -> ((Integer) d.get("id"))).orElse(null);
        boolean success = false;
        StringBuilder sb = new StringBuilder(getPrefix());
        if (!data.isPresent() || code == null) {
            log.trace("No result...");
            sb.append("No result.");
        } else if (startId != null && (id == null || id.intValue() != startId.intValue())) {
            log.trace("ID does not match original ID, assuming successful...");
            sb.append(StatusCode.SUCCEEDED.name);
            success = true;
        } else if (code == StatusCode.INITIATED || code == StatusCode.SUCCEEDED) {
            log.trace("Successful result: {}...", code.name);
            sb.append(code.name);
            success = true;
        } else if (code == StatusCode.UNAVAILABLE || code == StatusCode.NONE || code == StatusCode.FAILED) {
            log.trace("Failed result: {}...", code.name);
            sb.append(code.name);
        } else {
            return null;
        }
        if (message != null) {
            sb.append(" ");
            sb.append(message);
        }
        ResultBuilder rb = context.result().message(sb.toString());
        return success ? rb.succeeded() : rb.failed();
    }

    public abstract String getJobTopic();

    public abstract String getPrefix();

    public abstract Optional<CompositeData> getStatus();

    public JobExecutionResult process(Job job, JobExecutionContext context) {
        log.info("Starting {}", getPrefix());
        Optional<CompositeData> data = startMaintenance();
        Integer id = data.map(d -> ((Integer) d.get("id"))).orElse(null);
        JobExecutionResult result = null;
        while (result == null) {
            data = getStatus();
            result = createResult(context, data, id);
            if (result == null) {
                if (context.isStopped()) {
                    log.info(
                            "Canceling {}. The task was either stopped by the user or the Maintenance Window reached its end",
                            getPrefix());
                    stopMaintenance();
                    return context.result().message(String.format("%sStopped by user.", getPrefix())).failed();
                }
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else {
                log.debug("Retrieved result: {}", result);
            }
        }
        return result;
    }

    @Override
    public void run() {
        log.trace("Kicking off job: {}", getJobTopic());
        jobManager.addJob(getJobTopic(), Collections.emptyMap());
    }

    public abstract void setJobManager(JobManager jobManager);

    public abstract Optional<CompositeData> startMaintenance();

    public abstract Optional<CompositeData> stopMaintenance();

}
