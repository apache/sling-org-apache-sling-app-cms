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
package org.apache.sling.cms.core.insights.impl.providers;

import java.text.DecimalFormat;

import org.apache.commons.math.stat.descriptive.moment.StandardDeviation;
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
import org.apache.sling.cms.readability.Text;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = InsightProvider.class, immediate = true)
public class ReadabilityInsightProvider extends BaseInsightProvider {

    public static final String I18N_KEY_READABILITY_DETAIL = "slingcms.readability.detail";
    public static final String I18N_KEY_READABILITY_RESULT_DANGER = "slingcms.readability.danger";
    public static final String I18N_KEY_READABILITY_RESULT_SUCCESS = "slingcms.readability.success";
    public static final String I18N_KEY_READABILITY_RESULT_WARN = "slingcms.readability.warn";
    public static final String I18N_KEY_READABILITY_STATS = "slingcms.readability.stats";

    private static final Logger log = LoggerFactory.getLogger(ReadabilityInsightProvider.class);

    public static final String READABILITY_CA_CONFIG = "readability";

    @Reference
    private ConfigurationResourceResolver caResolver;

    @Reference
    private ReadabilityServiceFactory factory;

    @Reference
    private I18NProvider i18nProvider;

    private void addDetail(Insight insight, double score, String title) {
        insight.getScoreDetails().add(Message.defaultMsg(title + ": " + new DecimalFormat("##0.00").format(score)));
    }

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
            log.debug("Using readability configuration {}", readabilityResource);
            config = readabilityResource.adaptTo(ReadabilitySiteConfig.class);
        }

        I18NDictionary dictionary = i18nProvider.getDictionary(request.getResource().getResourceResolver());
        if (site != null && config != null) {
            executeReadabilityCheck(insight, pageRequest, text, site, config, dictionary);

        } else {
            log.warn("Failed to get readability for resource {} site or config were null",
                    pageRequest.getPage().getResource());
            insight.setScored(false);
            insight.setSucceeded(false);
            insight.setPrimaryMessage(Message.danger(dictionary.get(I18N_KEY_READABILITY_RESULT_DANGER,
                    new Object[] { pageRequest.getPage().getPath() })));
        }

        return insight;
    }

    private void executeReadabilityCheck(Insight insight, PageInsightRequest pageRequest, String text, Site site,
            ReadabilitySiteConfig config, I18NDictionary dictionary) {
        ReadabilityService svc = factory.getReadabilityService(site.getLocale());

        double score = svc.calculateAverageGradeLevel(text);
        String scoreStr = new DecimalFormat("##0.00").format(score);

        insight.setScored(true);

        log.debug("Calculating readability of page {}", pageRequest.getPage());

        if (score > config.getMaxGradeLevel() || score < config.getMinGradeLevel()) {
            log.debug("Retrieved out of bounds readability {} based on range {}-{}", score,
                    config.getMinGradeLevel(), config.getMaxGradeLevel());

            StandardDeviation sd = new StandardDeviation(false);
            double stddev = sd.evaluate(new double[] { config.getMinGradeLevel(), config.getMaxGradeLevel() });
            double dev;
            if (score > config.getMaxGradeLevel()) {
                dev = score - config.getMaxGradeLevel();
            } else {
                dev = config.getMinGradeLevel() - score;
            }
            double calcScore = 1 - (dev / stddev) * .5;
            if (calcScore > 0) {
                insight.setScore(calcScore);
            } else {
                insight.setScore(0.0);
            }
            insight.setPrimaryMessage(Message.warn(dictionary.get(I18N_KEY_READABILITY_RESULT_WARN,
                    new Object[] { config.getMinGradeLevel(), config.getMaxGradeLevel(), scoreStr })));
        } else {
            log.debug("Retrieved in bounds readability {} based on range {}-{}", score, config.getMinGradeLevel(),
                    config.getMaxGradeLevel());
            insight.setScore(1.0);
            insight.setPrimaryMessage(Message.success(dictionary.get(I18N_KEY_READABILITY_RESULT_SUCCESS,
                    new Object[] { config.getMinGradeLevel(), config.getMaxGradeLevel(), scoreStr })));
        }
        Text t = svc.extractSentences(text);

        insight.getScoreDetails().add(Message.defaultMsg(dictionary.get(I18N_KEY_READABILITY_STATS,
                new Object[] { t.getSentences().size(), t.getWordCount(), t.getComplexWordCount() })));
        addDetail(insight, svc.calculateARI(t), "ARI");
        addDetail(insight, svc.calculateColemanLiauIndex(t), "Coleman-Liau Index");
        addDetail(insight, svc.calculateFleschKincaidGradeLevel(t), "Flesch-Kincaid Grade Level");
        addDetail(insight, svc.calculateFleschReadingEase(t), "Flesch-Kincaid Reading Ease");
        addDetail(insight, svc.calculateGunningFog(t), "Gunning Fog");
        addDetail(insight, svc.calculateSMOG(t), "SMOG");
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
        if (request.getType() != InsightRequest.TYPE.PAGE) {
            log.debug("Insight is not of page type");
            return false;
        }
        if (site == null || site.getLocale() == null) {
            log.debug("Did not find site or locale");
            return false;
        }
        if (factory.getReadabilityService(site.getLocale()) == null) {
            log.debug("Unable to get readability service for locale {}", site.getLocale());
            return false;
        }
        return true;
    }
}
