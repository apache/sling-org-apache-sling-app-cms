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
package org.apache.sling.cms.core.models;

import java.util.Iterator;

import javax.jcr.query.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.util.Text;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.models.annotations.Model;
import org.osgi.annotation.versioning.ProviderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Model for retrieving the search results
 */
@ProviderType
@Model(adaptables = SlingHttpServletRequest.class)
public class SearchResults {

    private static final Logger log = LoggerFactory.getLogger(SearchResults.class);

    private String type = CMSConstants.NT_PAGE;
    private String path = null;
    private String term = null;
    private SlingHttpServletRequest request;

    public SearchResults(SlingHttpServletRequest request) {
        if (StringUtils.isNotEmpty(request.getParameter("type"))) {
            type = request.getParameter("type");
        }

        if (StringUtils.isNotEmpty(request.getParameter("path"))) {
            path = request.getParameter("path");
        }

        term = Text.escapeIllegalXpathSearchChars(request.getParameter("term")).replace("'", "''");
        this.request = request;
    }

    public Iterator<Resource> getResults() {
        String query = "SELECT * FROM [" + type + "] AS s WHERE CONTAINS(s.*, '" + term + "')";
        if (StringUtils.isNotEmpty(path)) {
            query += " AND ISDESCENDANTNODE([" + path + "])";
        }

        log.debug("Searching for content with {}", query);
        return request.getResourceResolver().findResources(query, Query.JCR_SQL2);
    }
}
