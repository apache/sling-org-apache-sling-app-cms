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

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.osgi.annotation.versioning.ProviderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ProviderType
@Model(adaptables = SlingHttpServletRequest.class)
public class QueryDebugger {

    private static final String MBEAN_NAME = "org.apache.jackrabbit.oak:name=Oak Query Statistics,type=QueryStat";

    private static final Logger log = LoggerFactory.getLogger(QueryDebugger.class);
    private final String plan;
    private final long duration;
    private final long estimatedSize;
    private final String exception;
    private final List<Resource> results = new ArrayList<>();
    private final String statement;
    private final List<Map<String, Object>> slowQueries = new ArrayList<>();
    private final List<Map<String, Object>> popularQueries = new ArrayList<>();
    private final boolean enabled;

    @Inject
    public QueryDebugger(@Self SlingHttpServletRequest request) {

        Optional<String> statementParam = Optional.ofNullable(request.getParameter("statement"));
        String language = Optional.ofNullable(request.getParameter("language")).orElse(Query.JCR_SQL2);

        int limit = Optional.ofNullable(request.getParameter("sample")).map(s -> Integer.parseInt(s, 10)).orElse(0);

        boolean lenabled = false;
        long lestimate = 0;
        long lduration = -1;
        String lplan = null;
        String lexception = null;
        String lstatement = null;
        try {
            if (statementParam.isPresent()) {

                QueryManager queryManager = request.getResourceResolver().adaptTo(Session.class).getWorkspace()
                        .getQueryManager();
                Query explainQuery = queryManager.createQuery("explain " + statementParam.get(), language);
                Row row = explainQuery.execute().getRows().nextRow();
                lplan = row.getValue("plan").getString();
                lstatement = statementParam.get();

                if (limit > 0) {
                    lenabled = true;
                    long start = System.currentTimeMillis();
                    Query query = queryManager.createQuery(statementParam.get(), language);
                    query.setLimit(limit);
                    QueryResult queryResult = query.execute();
                    RowIterator rowIterator = queryResult.getRows();
                    lestimate = rowIterator.getSize();
                    lduration = System.currentTimeMillis() - start;
                    while (rowIterator.hasNext()) {
                        Optional.ofNullable(rowIterator.nextRow())
                                .map(n -> {
                                    try {
                                        return request.getResourceResolver().getResource(n.getPath());
                                    } catch (RepositoryException e) {
                                        log.warn("Exception getting path from row: {}", n, e);
                                        return null;
                                    }
                                })
                                .ifPresent(results::add);
                    }
                }
            }
        } catch (RepositoryException re) {
            lexception = re.toString();
            log.warn("Failed to debug query: {}", statementParam, re);
        } finally {
            this.plan = lplan;
            this.duration = lduration;
            this.exception = lexception;
            this.statement = lstatement;
            this.estimatedSize = lestimate;
            this.enabled = lenabled;
        }

        try {
            collectMbeanData("PopularQueries", popularQueries);
            collectMbeanData("SlowQueries", slowQueries);
        } catch (MBeanException | MalformedObjectNameException | InstanceNotFoundException | AttributeNotFoundException
                | NullPointerException | ReflectionException e) {
            log.warn("Failed to load mBean data", e);
        }
    }

    private void collectMbeanData(String attributeName, List<Map<String, Object>> target)
            throws MalformedObjectNameException, NullPointerException, InstanceNotFoundException,
            AttributeNotFoundException, ReflectionException, MBeanException {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName mbeanName = ObjectName.getInstance(MBEAN_NAME);
        TabularData data = (TabularData) mBeanServer.getAttribute(mbeanName, attributeName);
        data.values().stream().map(CompositeData.class::cast)
                .forEach(compositeData -> target.add(compositeData.getCompositeType().keySet().stream()
                        .collect(Collectors.toMap(k -> k, compositeData::get))));
    }

    /**
     * @return the execution duration or -1
     */
    public long getDuration() {
        return duration;
    }

    /**
     * @return the estimated size
     */
    public long getEstimatedSize() {
        return estimatedSize;
    }

    /**
     * @return if execution enabled
     */
    public boolean isExecutionEnabled() {
        return enabled;
    }

    /**
     * @return the exception
     */
    public String getException() {
        return exception;
    }

    /**
     * @return the plan
     */
    public String getPlan() {
        return plan;
    }

    /**
     * @return the results
     */
    public List<Resource> getResults() {
        return results;
    }

    /**
     * @return the statement
     */
    public String getStatement() {
        return statement;
    }

    /**
     * @return the slowQueries
     */
    public List<Map<String, Object>> getSlowQueries() {
        return slowQueries;
    }

    /**
     * @return the popularQueries
     */
    public List<Map<String, Object>> getPopularQueries() {
        return popularQueries;
    }

}
