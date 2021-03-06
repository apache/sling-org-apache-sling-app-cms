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

import java.util.Collections;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.File;
import org.apache.sling.cms.Page;
import org.apache.sling.cms.insights.Insight;
import org.apache.sling.cms.insights.InsightFactory;
import org.apache.sling.cms.insights.InsightsModel;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the Insights Model
 */
@Model(adaptables = Resource.class, adapters = InsightsModel.class)
public class InsightsModelImpl implements InsightsModel {

    private static final Logger log = LoggerFactory.getLogger(InsightsModelImpl.class);

    @OSGiService
    private InsightFactory insightsFactory;

    private Resource resource;

    public InsightsModelImpl(Resource resource) {
        this.resource = resource;
    }

    @Override
    public List<Insight> getInsights() {
        if (CMSConstants.NT_FILE.equals(resource.getResourceType())) {
            log.debug("Gathering file insights for resource {}", resource);
            return insightsFactory.getInsights(resource.adaptTo(File.class));
        } else if (CMSConstants.NT_PAGE.equals(resource.getResourceType())) {
            log.debug("Gathering page insights for resource {}", resource);
            return insightsFactory.getInsights(resource.adaptTo(Page.class));
        } else {
            log.debug("Insights not available for resource {}", resource);
            return Collections.emptyList();
        }
    }

}
