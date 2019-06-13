/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.cms.core.insights.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.sling.cms.File;
import org.apache.sling.cms.Page;
import org.apache.sling.cms.insights.Insight;
import org.apache.sling.cms.insights.InsightFactory;
import org.apache.sling.cms.insights.InsightProvider;
import org.apache.sling.cms.insights.InsightRequest;
import org.apache.sling.engine.SlingRequestProcessor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * Implementation of the InsightFactory service interface
 */
@Component(immediate = true, service = { InsightFactory.class })
public class InsightFactoryImpl implements InsightFactory {

    @Reference(policyOption = ReferencePolicyOption.GREEDY)
    private List<InsightProvider> providers;

    @Reference
    private SlingRequestProcessor requestProcessor;

    @Override
    public List<Insight> getInsights(File file) {
        return getInsights(new FileInsightRequestImpl(file));
    }

    private List<Insight> getInsights(InsightRequest request) {
        List<Insight> insights = providers.stream().filter(ip -> ip.isEnabled(request))
                .map(ip -> ip.evaluateRequest(request)).collect(Collectors.toList());
        Collections.sort(insights, (o1, o2) -> o1.getProvider().getTitle().compareTo(o2.getProvider().getTitle()));
        return insights;
    }

    @Override
    public List<Insight> getInsights(Page page) {
        return getInsights(new PageInsightRequestImpl(page, requestProcessor));
    }

    public List<InsightProvider> getProviders() {
        return providers;
    }

}
