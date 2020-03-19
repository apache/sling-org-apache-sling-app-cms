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

import static org.junit.Assert.assertNull;

import java.util.Optional;

import javax.management.openmbean.CompositeData;

import org.apache.jackrabbit.oak.api.jmx.RepositoryManagementMBean.StatusCode;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.event.jobs.consumer.JobExecutionContext;
import org.apache.sling.event.jobs.consumer.JobExecutionContext.ResultBuilder;
import org.apache.sling.event.jobs.consumer.JobExecutionResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class AbstractMaintenanceJobTest {

    private AbstractMaintenanceJob amj;
    private JobExecutionContext context;
    private ResultBuilder resultBuilder;

    @Before
    public void init() {
        amj = new AbstractMaintenanceJob() {
            @Override
            public String getJobTopic() {
                throw new UnsupportedOperationException();
            }

            @Override
            public String getPrefix() {
                return "Test Job";
            }

            @Override
            public Optional<CompositeData> getStatus() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void setJobManager(JobManager jobManager) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Optional<CompositeData> startMaintenance() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Optional<CompositeData> stopMaintenance() {
                throw new UnsupportedOperationException();
            }
        };

        context = Mockito.mock(JobExecutionContext.class);
        resultBuilder = Mockito.mock(ResultBuilder.class);
        Mockito.when(resultBuilder.message(Mockito.anyString())).thenReturn(resultBuilder);
        Mockito.when(context.result()).thenReturn(resultBuilder);
    }

    @Test
    public void testRunningResult() {
        int id = 1;
        CompositeData data = CompositeDataMock.init().put("code", StatusCode.RUNNING.ordinal())
                .put("message", "Hello World").put("id", id).build();
        JobExecutionResult result = amj.createResult(context, Optional.ofNullable(data), id);
        assertNull(result);
    }

    @Test
    public void testNewIdResult() {
        int id = 1;
        CompositeData data = CompositeDataMock.init().put("code", StatusCode.RUNNING.ordinal())
                .put("message", "Hello World").put("id", 2).build();
        amj.createResult(context, Optional.ofNullable(data), id);
        Mockito.verify(resultBuilder).succeeded();
    }

    @Test
    public void testFailedResult() {
        int id = 1;
        CompositeData data = CompositeDataMock.init().put("code", StatusCode.FAILED.ordinal())
                .put("message", "Hello World").put("id", id).build();
        amj.createResult(context, Optional.ofNullable(data), id);
        Mockito.verify(resultBuilder).failed();

    }

    @Test
    public void testNoneResult() {
        int id = 1;
        CompositeData data = CompositeDataMock.init().put("code", StatusCode.NONE.ordinal())
                .put("message", "Hello World").put("id", id).build();
        amj.createResult(context, Optional.ofNullable(data), id);
        Mockito.verify(resultBuilder).failed();
    }

    @Test
    public void testInvalidCode() {
        int id = 1;
        CompositeData data = CompositeDataMock.init().put("code", 2345677).put("message", "Hello World").put("id", id)
                .build();
        amj.createResult(context, Optional.ofNullable(data), id);
        Mockito.verify(resultBuilder).failed();
    }

    @Test
    public void testSucceededResult() {
        int id = 1;
        CompositeData data = CompositeDataMock.init().put("code", StatusCode.SUCCEEDED.ordinal())
                .put("message", "Hello World").put("id", id).build();
        amj.createResult(context, Optional.ofNullable(data), id);
        Mockito.verify(resultBuilder).succeeded();
    }

}