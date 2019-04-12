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

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobExecutionContext;
import org.apache.sling.event.jobs.consumer.JobExecutionResult;
import org.apache.sling.event.jobs.consumer.JobExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Configurable Job Executor is a Sling Job which can be manually triggered by
 * Sling CMS users.
 */
public abstract class ConfigurableJobExecutor implements JobExecutor {

    private static final Logger log = LoggerFactory.getLogger(ConfigurableJobExecutor.class);

    private static final String USER_ID_KEY = "_userId";

    /**
     * A method for Configurable Job Executors to extend.
     * 
     * @param job      the job configuration
     * @param context  the job context
     * @param resolver an impersonated resource resolver for the user
     * @return the result of executing the job
     */
    public abstract JobExecutionResult doProcess(Job job, JobExecutionContext context, ResourceResolver resolver);

    /**
     * Get the path to the resource to configure a job invocation.
     * 
     * @return the path to the resource to configure a resource invocation
     */
    public abstract String getConfigurationPath();

    /**
     * A method to get the resource resolver factory, should be injected as a
     * service.
     * 
     * @return the resource resolver factory
     */
    public abstract ResourceResolverFactory getResolverFactory();

    /**
     * Get the i18n key for this job.
     * 
     * @return the job title i18n key
     */
    public abstract String getTitleKey();

    /**
     * Gets the topic of the job. This will be used as the Sling Job Topic for
     * invoking the job.
     * 
     * @return the Sling Job Topic
     */
    public abstract String getTopic();

    @Override
    @SuppressWarnings("deprecation")
    public final JobExecutionResult process(Job job, JobExecutionContext context) {
        ResourceResolver resolver = null;
        try {
            Map<String, Object> authenticationInfo = new HashMap<>();
            authenticationInfo.put(ResourceResolverFactory.USER_IMPERSONATION, job.getProperty(USER_ID_KEY));
            resolver = getResolverFactory().getAdministrativeResourceResolver(authenticationInfo);
            return doProcess(job, context, resolver);
        } catch (LoginException e) {
            log.warn("Failed to login", e);
            return context.result().failed();
        } finally {
            if (resolver != null) {
                resolver.close();
            }
        }
    }
}
