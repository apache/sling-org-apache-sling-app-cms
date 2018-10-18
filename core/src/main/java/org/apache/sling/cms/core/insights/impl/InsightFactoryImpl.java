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

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.sling.cms.File;
import org.apache.sling.cms.Page;
import org.apache.sling.cms.insights.Insight;
import org.apache.sling.cms.insights.InsightFactory;
import org.apache.sling.cms.insights.InsightProvider;
import org.apache.sling.cms.insights.InsightRequest;
import org.apache.sling.engine.SlingRequestProcessor;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the InsightFactory service interface
 */
@Component(immediate = true, service = { InsightFactory.class, ServiceListener.class })
public class InsightFactoryImpl implements InsightFactory, ServiceListener {

    private static final Logger log = LoggerFactory.getLogger(InsightFactoryImpl.class);
    private static final Map<String, InsightProvider> insightProviders = new HashMap<>();

    @Reference
    private SlingRequestProcessor requestProcessor;

    @Activate
    public void activate(ComponentContext context) throws InvalidSyntaxException {
        log.info("activate");
        insightProviders.clear();
        String filter = "(" + Constants.OBJECTCLASS + "=" + InsightProvider.class.getName() + ")";
        context.getBundleContext().addServiceListener(this, filter);
        @SuppressWarnings("unchecked")
        ServiceReference<InsightProvider>[] serviceReferences = (ServiceReference<InsightProvider>[]) context
                .getBundleContext().getServiceReferences(InsightProvider.class.getName(), null);
        if (serviceReferences != null) {
            Stream.of(serviceReferences).map(sr -> context.getBundleContext().getService(sr))
                    .forEach(provider -> insightProviders.put(provider.getId(), provider));
        }
        log.info("Check Service Listener registered successfully!");
    }

    @Override
    public void serviceChanged(ServiceEvent event) {
        @SuppressWarnings("unchecked")
        ServiceReference<InsightProvider> reference = (ServiceReference<InsightProvider>) event.getServiceReference();
        InsightProvider provider = (InsightProvider) event.getServiceReference().getBundle().getBundleContext()
                .getService(reference);
        synchronized (this) {
            log.info("Unregistering {}", provider.getId());
            insightProviders.remove(provider.getId());
            if (event.getType() == ServiceEvent.MODIFIED || event.getType() == ServiceEvent.REGISTERED) {
                log.info("Registering {}", provider.getId());
                insightProviders.put(provider.getId(), provider);
            }
        }
    }

    @Override
    public List<Insight> getInsights(File file) {
        return getInsights(new FileInsightRequestImpl(file));
    }

    @Override
    public List<Insight> getInsights(Page page) {
        return getInsights(new PageInsightRequestImpl(page, requestProcessor));
    }

    public Collection<InsightProvider> getProviders() {
        return insightProviders.values();
    }

    private List<Insight> getInsights(InsightRequest request) {
        List<Insight> insights = insightProviders.values().stream().filter(ip -> ip.isEnabled(request))
                .map(ip -> ip.evaluateRequest(request)).collect(Collectors.toList());
        Collections.sort(insights, new Comparator<Insight>() {
            @Override
            public int compare(Insight o1, Insight o2) {
                return o1.getProvider().getTitle().compareTo(o2.getProvider().getTitle());
            }
        });
        return insights;
    }

}
