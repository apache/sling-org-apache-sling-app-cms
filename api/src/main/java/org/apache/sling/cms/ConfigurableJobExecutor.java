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
package org.apache.sling.cms;

import org.apache.sling.event.jobs.consumer.JobExecutor;

/**
 * A Configurable Job Executor is a Sling Job which can be manually triggered
 * by Sling CMS users.
 */
public interface ConfigurableJobExecutor extends JobExecutor {

    /**
     * Get the path to the resource to configure a job invocation.
     * 
     * @return the path to the resource to configure a resource invocation
     */
    String getConfigurationPath();

    /**
     * Get the i18n key for this job.
     * 
     * @return the job title i18n key
     */
    String getTitleKey();

    /**
     * Gets the topic of the job. This will be used as the Sling Job Topic for
     * invoking the job.
     * 
     * @return the Sling Job Topic
     */
    String getTopic();
}
