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

/**
 * Package with all of the core models used to support the Sling reference CMS
 */
package org.apache.sling.cms.core.models;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.jcr.query.Query;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.osgi.annotation.versioning.ProviderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Model for retrieving the most start page content
 */
@ProviderType
@Model(adaptables = SlingHttpServletRequest.class)
public class StartContent {

    private static final Logger log = LoggerFactory.getLogger(StartContent.class);

    private final ResourceResolver resolver;
    private final String term;

    public StartContent(SlingHttpServletRequest request) {
        this.resolver = request.getResourceResolver();
        term = request.getParameter("q");
    }

    public List<Resource> getRelatedContent() {
        return getTenResults(
                "SELECT * FROM [nt:hierarchyNode] AS s WHERE ISDESCENDANTNODE([/content]) AND CONTAINS(s.*,'"
                        + escape(term.replaceAll("[\\Q+-&|!(){}[]^\"~*?:\\/\\E]", "")) + "')");

    }

    public List<Resource> getRecentDrafts() {
        return Stream.concat(getTenResults("SELECT * FROM [sling:Page] WHERE [jcr:content/jcr:lastModifiedBy] = '"
                + escape(resolver.getUserID())
                + "' AND ISDESCENDANTNODE([/content]) AND [jcr:content/sling:published] = false ORDER BY [jcr:content/jcr:lastModified] DESC")
                .stream(),
                getTenResults("SELECT * FROM [sling:Page] WHERE [jcr:content/jcr:lastModifiedBy] = '"
                        + escape(resolver.getUserID())
                        + "' AND ISDESCENDANTNODE([/content]) AND [jcr:content/sling:published] IS NULL ORDER BY [jcr:content/jcr:lastModified] DESC")
                        .stream())
                .sorted((r1, r2) -> r2.getValueMap().get("jcr:content/jcr:lastModified", Calendar.class)
                        .compareTo(r1.getValueMap().get("jcr:content/jcr:lastModified", Calendar.class))

                ).limit(10).collect(Collectors.toList());
    }

    public List<Resource> getRecentContent() {
        return getTenResults(
                "SELECT * FROM [nt:hierarchyNode] WHERE [jcr:content/jcr:lastModifiedBy] = '"
                        + escape(resolver.getUserID())
                        + "' AND ISDESCENDANTNODE([/content]) ORDER BY [jcr:content/jcr:lastModified] DESC");
    }

    private String escape(String str) {
        return str.replace("'", "''");
    }

    private List<Resource> getTenResults(String query) {
        log.debug("Executing query: {}", query);
        Iterator<Resource> it = resolver.findResources(query,
                Query.JCR_SQL2);
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(it, Spliterator.NONNULL), false).limit(10)
                .collect(Collectors.toList());
    }

}
