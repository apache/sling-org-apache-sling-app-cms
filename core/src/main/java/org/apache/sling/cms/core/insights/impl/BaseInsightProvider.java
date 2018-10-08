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

import org.apache.sling.cms.insights.Insight;
import org.apache.sling.cms.insights.InsightProvider;
import org.apache.sling.cms.insights.InsightRequest;
import org.apache.sling.cms.insights.Message;
import org.apache.sling.cms.insights.Message.STYLE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple base for the Insight Providers
 */
public abstract class BaseInsightProvider implements InsightProvider {

    private static final Logger log = LoggerFactory.getLogger(BaseInsightProvider.class);

    /**
     * Method for the extending classes to implement, this can safely throw
     * exceptions and this will trigger a failure result to be returned.
     * 
     * @param request the request to evaluate
     * @return the result of evaluation
     * @throws Exception any exception
     */
    protected abstract Insight doEvaluateRequest(InsightRequest request) throws Exception;

    @Override
    public Insight evaluateRequest(InsightRequest request) {
        log.trace("evaluatePage");
        Insight insight = null;
        try {
            insight = this.doEvaluateRequest(request);
        } catch (Exception e) {
            log.error("Unable to perform check " + getTitle() + " for request " + request, e);
            insight = new Insight(this, request);
            insight.getScoreDetails().add(new Message(
                    "Unable to perform check " + getTitle() + " due to unexpected exception", STYLE.DANGER));
            insight.setSucceeded(false);
            insight.setScored(false);

        }
        return insight;
    }
}
