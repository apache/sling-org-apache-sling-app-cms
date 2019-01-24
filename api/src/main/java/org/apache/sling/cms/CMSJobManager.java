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

import java.util.Collection;

import org.apache.sling.event.jobs.Job;

/**
 * An abstraction layer for starting and finding jobs started by the Sling CMS.
 * Can be adapted from a SlingHttpServletRequest
 */
public interface CMSJobManager {

    /**
     * Gets all of the jobs which can be executed.
     * 
     * @return the jobs which can be executed
     */
    Collection<ConfigurableJobExecutor> getAvailableJobs();

    /**
     * Find all of the jobs started by this user.
     * 
     * @return the jobs started by the user
     */
    Collection<Job> getJobs();

    /**
     * Gets the job by id as passed in the Sling suffix.
     * 
     * @return the job
     */
    Job getSuffixJob();

    /**
     * Starts a new job based on the request.
     * 
     * @return the job
     */
    Job startJob();

    /**
     * Deletes the specified job.
     * 
     * @param id the id of the job to delete
     */
    void deleteJob(String id);

}
