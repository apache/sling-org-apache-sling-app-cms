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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.cms.Page;
import org.apache.sling.cms.insights.PageInsightRequest;
import org.apache.sling.engine.SlingRequestProcessor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the PageInsightRequest interface
 */
public class PageInsightRequestImpl implements PageInsightRequest {

    private static final Logger log = LoggerFactory.getLogger(PageInsightRequestImpl.class);

    private Map<String, String> markupCache = new HashMap<>();

    private final Page page;

    private final SlingRequestProcessor requestProcessor;

    private final ResourceResolver resourceResolver;

    public PageInsightRequestImpl(Page page, SlingRequestProcessor requestProcessor) {
        this.page = page;
        this.requestProcessor = requestProcessor;
        this.resourceResolver = page.getResource().getResourceResolver();
    }

    private String getContents(String url) {
        log.trace("getLocalPageHTML");
        if (!markupCache.containsKey(url)) {
            String requestPath = page.getPath() + ".html";
            log.debug("Loading local page HTML from {}", requestPath);
            HttpServletRequest req = new FakeRequest("GET", requestPath);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            HttpServletResponse resp;
            try {
                resp = new FakeResponse(out);
                requestProcessor.processRequest(req, resp, resourceResolver);
            } catch (ServletException | IOException | NoSuchAlgorithmException e) {
                log.warn("Exception retrieving page contents for {}", url, e);
            }
            markupCache.put(url, out.toString());
        }
        return markupCache.get(url);
    }

    @Override
    public Page getPage() {
        return this.page;
    }

    @Override
    public Element getPageBodyElement() throws IOException {
        return Jsoup.parseBodyFragment(getPageBodyHtml()).body();
    }

    @Override
    public String getPageBodyHtml() throws IOException {
        return getContents(page.getPath() + "/jcr:content/container.html");
    }

    @Override
    public Document getPageDocument() throws IOException {
        return Jsoup.parse(getPageHtml());
    }

    @Override
    public String getPageHtml() throws IOException {
        return getContents(page.getPath() + ".html");
    }

    @Override
    public Resource getResource() {
        return page.getResource();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PageInsightRequestImpl [markupCache=" + markupCache + ", page=" + page + ", requestProcessor="
                + requestProcessor + ", resourceResolver=" + resourceResolver + "]";
    }

}
