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

import javax.management.openmbean.CompositeData;

import org.apache.jackrabbit.oak.api.jmx.RepositoryManagementMBean;
import org.apache.jackrabbit.oak.api.jmx.RepositoryManagementMBean.StatusCode;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.event.jobs.consumer.JobExecutionContext;
import org.apache.sling.event.jobs.consumer.JobExecutionContext.ResultBuilder;
import org.apache.sling.event.jobs.consumer.JobExecutionResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class RevisionCleanupSchedulerTest {

    private JobExecutionContext context;
    private ResultBuilder resultBuilder;

    @Before
    public void init() {
        context = Mockito.mock(JobExecutionContext.class);
        resultBuilder = Mockito.mock(ResultBuilder.class);
        Mockito.when(resultBuilder.message(Mockito.anyString())).thenReturn(resultBuilder);
        Mockito.when(resultBuilder.succeeded()).thenReturn(Mockito.mock(JobExecutionResult.class));
        Mockito.when(resultBuilder.failed()).thenReturn(Mockito.mock(JobExecutionResult.class));
        Mockito.when(context.result()).thenReturn(resultBuilder);
    }

    @Test
    public void testRunnable() {
        final RevisionCleanupScheduler dscs = new RevisionCleanupScheduler();
        final JobManager jobManager = Mockito.mock(JobManager.class);
        dscs.setJobManager(jobManager);

        Mockito.when(jobManager.addJob(Mockito.eq(dscs.getJobTopic()), Mockito.anyMap())).then((answer) -> {
            dscs.process(Mockito.mock(Job.class), context);
            return null;
        });

        Integer id = 1;
        final RepositoryManagementMBean repositoryManager = Mockito.mock(RepositoryManagementMBean.class);
        CompositeData startingCd = CompositeDataMock.init().put("id", id).build();
        Mockito.when(repositoryManager.startDataStoreGC(false)).thenReturn(startingCd);
        CompositeData doneCd = CompositeDataMock.init().put("id", id).put("code", StatusCode.SUCCEEDED.ordinal())
                .build();
        Mockito.when(repositoryManager.getRevisionGCStatus()).thenReturn(doneCd);
        dscs.setRepositoryManager(repositoryManager);

        dscs.run();

        Mockito.verify(repositoryManager).startRevisionGC();
        Mockito.verify(resultBuilder).succeeded();
    }

}