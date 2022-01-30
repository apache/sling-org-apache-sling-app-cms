<%-- /*
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
 */ --%>
<%@include file="/libs/sling-cms/global.jsp"%>
<div id="query-debug" class="scroll-container reload-container">
    <sling:adaptTo adaptable="${slingRequest}" adaptTo="org.apache.sling.cms.core.models.QueryDebugger" var="queryDebugger" />
    <br/><hr/><br/>
    <dl>
    <c:if test="${not empty queryDebugger.statement}">
        <dt><fmt:message key="Query Statement" /><dt>
        <dd>${sling:encode(queryDebugger.statement,'HTML')}</dd>
    </c:if>
    <c:if test="${not empty queryDebugger.plan}">
        <dt><fmt:message key="Plan" /><dt>
        <dd>${sling:encode(queryDebugger.plan,'HTML')}</dd>
    </c:if>
    <c:if test="${not empty queryDebugger.exception}">
        <dt><fmt:message key="Exception" /><dt>
        <dd>${sling:encode(queryDebugger.exception,'HTML')}</dd>
    </c:if>
    <br/><hr/><br/>
    <h2><fmt:message key="Popular Queries" /></h2>
    <table class="table">
        <tr>
            <th>
                <fmt:message key="Query Statement" />
            </th>
            <th>
                <fmt:message key="Query Language" />
            </th>
            <th>
                <fmt:message key="Count" />
            </th>
            <th>
                <fmt:message key="Duration" />
            </th>
        </tr>
        <c:forEach var="query" items="${queryDebugger.popularQueries}">
            <tr>
                <td>
                    ${query.statement}
                </td>
                <td>
                    ${query.language}
                </td>
                <td>
                    ${query.occurrenceCount}
                </td>
                <td>
                    ${query.duration}
                </td>
            </tr>
        </c:forEach>
    </table>
    <h2><fmt:message key="Slow Queries" /></h2>
    <table class="table">
        <tr>
            <th>
                <fmt:message key="Query Statement" />
            </th>
            <th>
                <fmt:message key="Query Language" />
            </th>
            <th>
                <fmt:message key="Count" />
            </th>
            <th>
                <fmt:message key="Duration" />
            </th>
        </tr>
        <c:forEach var="query" items="${queryDebugger.slowQueries}">
            <tr>
                <td>
                    ${query.statement}
                </td>
                <td>
                    ${query.language}
                </td>
                <td>
                    ${query.occurrenceCount}
                </td>
                <td>
                    ${query.duration}
                </td>
            </tr>
        </c:forEach>
    </table>
</div>