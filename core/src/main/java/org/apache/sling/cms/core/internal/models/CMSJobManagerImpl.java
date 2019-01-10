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
package org.apache.sling.cms.core.internal.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.cms.CMSJobManager;
import org.apache.sling.cms.ConfigurableJobExecutor;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.event.jobs.JobManager.QueryType;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of the CMS Job Manager.
 */
@Model(adaptables = SlingHttpServletRequest.class, adapters = CMSJobManager.class)
public class CMSJobManagerImpl implements CMSJobManager {

    private static final Logger log = LoggerFactory.getLogger(CMSJobManagerImpl.class);

    private static final String PN_INITIATOR = "_initiator";
    private static final String PN_JOB_TITLE_KEY = "_titleKey";
    private static final String PN_USER_ID = "_userId";
    private static final String VALUE_SLING_CMS = "SlingCMS";

    @OSGiService
    private JobManager jobManager;

    private SlingHttpServletRequest request;

    public CMSJobManagerImpl(SlingHttpServletRequest request) {
        this.request = request;
    }

    @Override
    public Collection<ConfigurableJobExecutor> getAvailableJobs() {
        BundleContext bundleContext = FrameworkUtil.getBundle(CMSJobManager.class).getBundleContext();
        try {
            return bundleContext.getServiceReferences(ConfigurableJobExecutor.class, null).stream()
                    .map(bundleContext::getService).collect(Collectors.toList());
        } catch (InvalidSyntaxException e) {
            log.warn("Failed to get available jobs", e);
            return new ArrayList<>();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Job> getJobs() {
        Map<String, Object> search = new HashMap<>();
        search.put(PN_USER_ID, request.getResourceResolver().getUserID());
        search.put(PN_INITIATOR, VALUE_SLING_CMS);
        return Stream.concat(jobManager.findJobs(QueryType.ALL, null, 0, search).stream(),
                jobManager.findJobs(QueryType.HISTORY, null, 0, search).stream()).collect(Collectors.toList());
    }

    private String getJobTitleKey(String jobTopic) {
        BundleContext bundleContext = FrameworkUtil.getBundle(CMSJobManager.class).getBundleContext();
        try {
            Optional<String> op = bundleContext
                    .getServiceReferences(ConfigurableJobExecutor.class,
                            "(" + JobConsumer.PROPERTY_TOPICS + "=" + jobTopic + ")")
                    .stream().map(bundleContext::getService).map(ConfigurableJobExecutor::getTitleKey).findFirst();
            return op.orElse(null);
        } catch (InvalidSyntaxException e) {
            log.warn("Failed to get available jobs", e);
        }
        return null;
    }

    @Override
    public Job getSuffixJob() {
        return jobManager
                .getJobById(Optional.ofNullable(request.getRequestPathInfo().getSuffix()).orElse(" ").substring(1));
    }

    @Override
    public Job startJob() {
        Map<String, Object> properties = new HashMap<>();

        request.getRequestParameterMap().forEach((k, v) -> {
            if (v != null && v.length > 0) {
                if (v.length > 1) {
                    properties.put(k, Arrays.stream(v).map(RequestParameter::getString).collect(Collectors.toList())
                            .toArray(new String[v.length]));
                } else {
                    properties.put(k, v[0].getString());
                }
            }
        });
        properties.remove(JobConsumer.PROPERTY_TOPICS);

        String jobTopic = request.getParameter(JobConsumer.PROPERTY_TOPICS);
        String titleKey = getJobTitleKey(jobTopic);
        if (titleKey != null) {
            properties.put(PN_JOB_TITLE_KEY, titleKey);
        }
        properties.put(PN_USER_ID, request.getResourceResolver().getUserID());
        properties.put(PN_INITIATOR, VALUE_SLING_CMS);

        return jobManager.addJob(jobTopic, properties);
    }

}
