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
package org.apache.sling.cms.core.insights.impl.providers;

import java.io.StringReader;
import java.net.URLEncoder;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.sling.cms.core.insights.impl.BaseInsightProvider;
import org.apache.sling.cms.core.insights.impl.providers.PageSpeedInsightProvider.Config;
import org.apache.sling.cms.i18n.I18NDictionary;
import org.apache.sling.cms.i18n.I18NProvider;
import org.apache.sling.cms.insights.Insight;
import org.apache.sling.cms.insights.InsightProvider;
import org.apache.sling.cms.insights.InsightRequest;
import org.apache.sling.cms.insights.Message;
import org.apache.sling.cms.insights.PageInsightRequest;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = InsightProvider.class, immediate = true)
@Designate(ocd = Config.class)
public class PageSpeedInsightProvider extends BaseInsightProvider {

    @ObjectClassDefinition(name = "%pagespeed.config.name", description = "%pagespeed.config.description", localization = "OSGI-INF/l10n/bundle")
    public @interface Config {
        @AttributeDefinition(name = "%pagespeed.param.enabled.name", description = "%pagespeed.param.enabled.description")
        boolean enabled();

        @AttributeDefinition(name = "%pagespeed.param.apikey.name", description = "%pagespeed.param.apikey.description")
        String apikey();
    }

    @Reference
    private I18NProvider i18nProvider;

    public static final String I18N_KEY_READABILITY_RESULT_DANGER = "slingcms.pagespeed.danger";
    public static final String I18N_KEY_READABILITY_RESULT_WARN = "slingcms.pagespeed.warn";
    public static final String I18N_KEY_READABILITY_RESULT_SUCCESS = "slingcms.pagespeed.success";
    private static final String REQUEST_FORMAT = "https://www.googleapis.com/pagespeedonline/v2/runPagespeed?url=%s&fields=%s&key=%s";
    private static final String PARAMETERS = "id%2CinvalidRules%2CresponseCode%2CruleGroups";
    private static final String PAGESPEED_FORMAT = "https://developers.google.com/speed/pagespeed/insights/?url=%s";
    private static final Logger log = LoggerFactory.getLogger(PageSpeedInsightProvider.class);

    private Config config;

    @Activate
    public void activate(Config config) {
        this.config = config;
    }

    @Override
    protected Insight doEvaluateRequest(InsightRequest request) throws Exception {
        Insight insight = new Insight(this, request);
        PageInsightRequest pageRequest = (PageInsightRequest) request;
        String publishedUrl = pageRequest.getPage().getPublishedUrl();
        String checkUrl = String.format(REQUEST_FORMAT, URLEncoder.encode(publishedUrl, "UTF-8"), PARAMETERS,
                config.apikey());

        HttpGet httpGet = new HttpGet(checkUrl);

        CloseableHttpResponse response = null;
        JsonReader reader = null;
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            I18NDictionary dictionary = i18nProvider.getDictionary(request.getResource().getResourceResolver());

            log.debug("Requesting page speed via: {}", checkUrl);
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            reader = Json.createReader(new StringReader(EntityUtils.toString(entity)));
            JsonObject resp = reader.readObject();

            log.debug("Retrieved response: {}", resp);

            insight.setScored(true);
            double score = resp.getJsonObject("ruleGroups").getJsonObject("SPEED").getJsonNumber("score").doubleValue()
                    / 100.0;
            insight.setScore(score);
            log.debug("Parsed pagespeed score {}", score);

            if (score < 0.65) {
                insight.setPrimaryMessage(Message.danger(dictionary.get(I18N_KEY_READABILITY_RESULT_DANGER)));
            } else if (score < 0.8) {
                insight.setPrimaryMessage(Message.warn(dictionary.get(I18N_KEY_READABILITY_RESULT_WARN)));
            } else {
                insight.setPrimaryMessage(Message.success(dictionary.get(I18N_KEY_READABILITY_RESULT_SUCCESS)));
            }
            insight.setMoreDetailsLink(String.format(PAGESPEED_FORMAT, URLEncoder.encode(publishedUrl, "UTF-8")));

            log.debug("Response parsed successfully");

        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return insight;
    }

    @Override
    public String getId() {
        return "pagespeed";
    }

    @Override
    public String getTitle() {
        return "Page Speed";
    }

    @Override
    public boolean isEnabled(InsightRequest request) {
        if (!config.enabled()) {
            log.debug("Page Speed is not enabled");
            return false;
        }
        if (request.getType() != InsightRequest.TYPE.PAGE) {
            log.debug("Request {} is not a page", request);
            return false;
        }
        if (!((PageInsightRequest) request).getPage().isPublished()) {
            log.debug("The page for {} is not published", request);
            return false;
        }
        return true;
    }

}
