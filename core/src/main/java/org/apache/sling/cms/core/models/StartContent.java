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

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.cms.CMSConstants;
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

    private ResourceResolver resolver;
    private String term;

    public StartContent(SlingHttpServletRequest request) {
        this.resolver = request.getResourceResolver();
        term = request.getParameter("q");
    }

    public List<Resource> getRelatedContent() {
        return Stream.concat(get10Related(resolver, CMSConstants.NT_PAGE, term),
                get10Related(resolver, CMSConstants.NT_FILE, term)).sorted((o1, o2) -> {
                    try {
                        return (int) ((o1.getScore() - o2.getScore()) * 100);
                    } catch (RepositoryException e) {
                        log.warn("Exception getting score", e);
                        return 0;
                    }
                }).limit(9).map(row -> {
                    try {
                        return resolver.getResource(row.getPath());
                    } catch (RepositoryException e) {
                        log.warn("Failed to get resource", e);
                        return null;
                    }
                }).filter(Objects::nonNull).collect(Collectors.toList());

    }

    public List<Resource> getRecentContent() {
        return Stream.concat(get10Recent(resolver, CMSConstants.NT_PAGE), get10Recent(resolver, CMSConstants.NT_FILE))
                .sorted((o1,
                        o2) -> o1.getValueMap().get("jcr:content/jcr:lastModified", new Date())
                                .compareTo(o2.getValueMap().get("jcr:content/jcr:lastModified", new Date())) * -1)
                .limit(10).collect(Collectors.toList());
    }

    private Stream<Resource> get10Recent(ResourceResolver resolver, String type) {
        Iterator<Resource> it = resolver.findResources(
                "SELECT * FROM [" + type + "] WHERE [jcr:content/jcr:lastModifiedBy] = '" + resolver.getUserID()
                        + "' AND ISDESCENDANTNODE([/content]) ORDER BY [jcr:content/jcr:lastModified] DESC",
                Query.JCR_SQL2);
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(it, Spliterator.NONNULL), false).limit(10);
    }

    private Stream<Row> get10Related(ResourceResolver resolver, String type, String term) {
        Session session = resolver.adaptTo(Session.class);
        if (session != null) {
            try {
                Query query = session.getWorkspace().getQueryManager()
                        .createQuery("SELECT * FROM [" + type
                                + "] AS s WHERE ISDESCENDANTNODE([/content]) AND CONTAINS(s.*,'"
                                + term.replace("'", "''") + "')", Query.JCR_SQL2);
                QueryResult result = query.execute();
                @SuppressWarnings("unchecked")
                Iterable<Row> iterable = () -> {
                    try {
                        return result.getRows();
                    } catch (RepositoryException e) {
                        log.warn("Failed to get iterator", e);
                    }
                    return null;
                };
                return StreamSupport.stream(iterable.spliterator(), false).limit(10);
            } catch (RepositoryException e) {
                log.warn("Exception searching for related content", e);
            }
        }
        return new ArrayList<Row>().stream();
    }
}
