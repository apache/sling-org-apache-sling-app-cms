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
package org.apache.sling.cms.insights;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple POJO Model for holding an insight provider's results.
 */
public class Insight {

    private String moreDetailsLink;
    private Message primaryMessage;
    private InsightProvider provider;
    private InsightRequest request;
    private double score;
    private boolean scored = false;
    private List<Message> scoreDetails = new ArrayList<>();
    private boolean skip = false;

    private boolean succeeded = true;

    public Insight(InsightProvider provider, InsightRequest request) {
        this.request = request;
        this.provider = provider;
    }

    public void addMessage(Message message) {
        scoreDetails.add(message);
    }

    /**
     * @return the moreDetailsLink
     */
    public String getMoreDetailsLink() {
        return moreDetailsLink;
    }

    public Message getPrimaryMessage() {
        return primaryMessage;
    }

    /**
     * @return the provider
     */
    public InsightProvider getProvider() {
        return provider;
    }

    /**
     * @return the request
     */
    public InsightRequest getRequest() {
        return request;
    }

    /**
     * @return the score
     */
    public double getScore() {
        return score;
    }

    /**
     * @return the scoreDetails
     */
    public List<Message> getScoreDetails() {
        return scoreDetails;
    }

    public boolean isScored() {
        return scored;
    }

    /**
     * @return the skip
     */
    public boolean isSkip() {
        return skip;
    }

    /**
     * @return the succeeded
     */
    public boolean isSucceeded() {
        return succeeded;
    }

    /**
     * @param moreDetailsLink the moreDetailsLink to set
     */
    public void setMoreDetailsLink(String moreDetailsLink) {
        this.moreDetailsLink = moreDetailsLink;
    }

    public void setPrimaryMessage(Message primaryMessage) {
        this.primaryMessage = primaryMessage;
    }

    /**
     * @param provider the provider to set
     */
    public void setProvider(InsightProvider provider) {
        this.provider = provider;
    }

    /**
     * @param request the request to set
     */
    public void setRequest(InsightRequest request) {
        this.request = request;
    }

    /**
     * @param score the score to set
     */
    public void setScore(double score) {
        this.score = score;
    }

    public void setScored(boolean scored) {
        this.scored = scored;
    }

    /**
     * @param scoreDetails the scoreDetails to set
     */
    public void setScoreDetails(List<Message> scoreDetails) {
        this.scoreDetails = scoreDetails;
    }

    /**
     * @param skip the skip to set
     */
    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    /**
     * @param succeeded the succeeded to set
     */
    public void setSucceeded(boolean succeeded) {
        this.succeeded = succeeded;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Insight [primaryMessage=" + primaryMessage + ", provider=" + provider + ", request=" + request
                + ", score=" + score + ", scored=" + scored + ", scoreDetails=" + scoreDetails + ", skip=" + skip
                + ", moreDetailsLink=" + moreDetailsLink + ", succeeded=" + succeeded + "]";
    }

}
