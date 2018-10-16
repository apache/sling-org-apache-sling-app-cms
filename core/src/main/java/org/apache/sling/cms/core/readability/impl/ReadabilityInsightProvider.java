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
package org.apache.sling.cms.core.readability.impl;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.resource.ConfigurationResourceResolver;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.Site;
import org.apache.sling.cms.SiteManager;
import org.apache.sling.cms.core.insights.impl.BaseInsightProvider;
import org.apache.sling.cms.core.internal.models.ReadabilitySiteConfig;
import org.apache.sling.cms.i18n.I18NDictionary;
import org.apache.sling.cms.i18n.I18NProvider;
import org.apache.sling.cms.insights.Insight;
import org.apache.sling.cms.insights.InsightProvider;
import org.apache.sling.cms.insights.InsightRequest;
import org.apache.sling.cms.insights.Message;
import org.apache.sling.cms.insights.PageInsightRequest;
import org.apache.sling.cms.readability.ReadabilityService;
import org.apache.sling.cms.readability.ReadabilityServiceFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = InsightProvider.class)
public class ReadabilityInsightProvider extends BaseInsightProvider {

    public static final String I18N_KEY_READABILITY_RESULT_DANGER = "sling.cms.readability.danger";
    public static final String I18N_KEY_READABILITY_RESULT_SUCCESS = "sling.cms.readability.success";
    public static final String I18N_KEY_READABILITY_RESULT_WARN = "sling.cms.readability.warn";

    public static final String READABILITY_CA_CONFIG = "readability";

    @Reference
    private ConfigurationResourceResolver caResolver;

    @Reference
    private ReadabilityServiceFactory factory;

    @Reference
    private I18NProvider i18nProvider;

    /**
     * Method for the extending classes to implement, this can safely throw
     * exceptions and this will trigger a failure result to be returned.
     * 
     * @param request the request to evaluate
     * @return the result of evaluation
     * @throws Exception any exception
     */
    @Override
    protected Insight doEvaluateRequest(InsightRequest request) throws Exception {
        Insight insight = new Insight(this, request);

        PageInsightRequest pageRequest = (PageInsightRequest) request;
        String text = pageRequest.getPageBodyElement().text();
        Site site = null;
        SiteManager smgr = request.getResource().adaptTo(SiteManager.class);
        if (smgr != null) {
            site = smgr.getSite();
        }

        Resource readabilityResource = caResolver.getResource(pageRequest.getPage().getResource(),
                CMSConstants.INSIGHTS_CA_CONFIG_BUCKET, READABILITY_CA_CONFIG);
        ReadabilitySiteConfig config = null;
        if (readabilityResource != null) {
            config = readabilityResource.adaptTo(ReadabilitySiteConfig.class);
        }

        I18NDictionary dictionary = i18nProvider.getDictionary(request.getResource().getResourceResolver());
        if (site != null && config != null) {
            ReadabilityService svc = factory.getReadabilityService(site.getLocale());

            double score = svc.calculateAverageGradeLevel(text);

            insight.setScored(true);

            if (score > config.maxGradeLevel() || score < config.minGradeLevel()) {
                insight.setScore(0.5);
                insight.addMessage(Message.success(dictionary.get(I18N_KEY_READABILITY_RESULT_WARN,
                        new Object[] { config.minGradeLevel(), config.maxGradeLevel(), score })));
            } else {
                insight.setScore(1.0);
                insight.addMessage(Message.success(dictionary.get(I18N_KEY_READABILITY_RESULT_SUCCESS,
                        new Object[] { config.minGradeLevel(), config.maxGradeLevel(), score })));
            }
        } else {
            insight.setScored(false);
            insight.setSucceeded(false);
            insight.addMessage(Message.danger(dictionary.get(I18N_KEY_READABILITY_RESULT_DANGER,
                    new Object[] { pageRequest.getPage().getPath() })));
        }

        return insight;
    }

    @Override
    public String getId() {
        return READABILITY_CA_CONFIG;
    }

    @Override
    public String getTitle() {
        return "Readability";
    }

    @Override
    public boolean isEnabled(InsightRequest request) {
        Site site = null;
        SiteManager smgr = request.getResource().adaptTo(SiteManager.class);
        if (smgr != null) {
            site = smgr.getSite();
        }
        return request.getType() == InsightRequest.TYPE.PAGE && site != null && site.getLocale() != null
                && factory.getReadabilityService(site.getLocale()) != null;
    }
}
