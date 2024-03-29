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
<sling:adaptTo var="jobMgr" adaptable="${slingRequest}" adaptTo="org.apache.sling.cms.CMSJobManager" />
<c:set var="job" value="${jobMgr.suffixJob}" />
<nav class="breadcrumb" aria-label="breadcrumbs">
    <ul>
        <li>
            <a href="/cms/jobs/list.html">
                <fmt:message key="Jobs" />
            </a>
        </li>
        <li class="is-active">
            <a href="#">
                <fmt:message key="${job.properties._titleKey}" var="title" />
                <sling:encode value="title" mode="HTML" />
            </a>
        </li>
    </ul>
</nav>
<h2><fmt:message key="${job.properties._titleKey}" /></h2>
<div class="scroll-container">
    <dl>
        <dt><fmt:message key="State" /></dt>
        <dd>${sling:encode(job.jobState,'HTML')}</dd>
        <c:if test="${not empty job.resultMessage}">
            <dt><fmt:message key="Result Message" /></dt>
            <dd>${sling:encode(job.resultMessage,'HTML')}</dd>
        </c:if>
        <dt><fmt:message key="Started" /></dt>
        <dd><fmt:formatDate value="${job.created.time}" type="both" dateStyle="long" timeStyle="long" /></dd>
        <c:if test="${job.finishedDate != null}">
            <dt><fmt:message key="Finished" /></dt>
            <dd>
                <fmt:formatDate value="${job.finishedDate.time}" type="both" dateStyle="long" timeStyle="long" />
            </dd>
        </c:if>
        <c:if test="${job.progressStepCount > 0}">
            <dt><fmt:message key="Progress" /></dt>
            <dd>
                ${sling:encode(job.finishedProgressStep,'HTML')} / ${sling:encode(job.progressStepCount,'HTML')}
            </dd>
        </c:if>
        <c:if test="${job.progressLog != null && fn:length(job.progressLog) > 0}">
            <dt><fmt:message key="Progress Log" /></dt>
            <dd>
                <ul>
                    <c:forEach var="log" items="${job.progressLog}">
                        <li>${sling:encode(log,'HTML')}</li>
                    </c:forEach>
                </ul>
            </dd>
        </c:if>
        <dt><fmt:message key="Properties" /></dt>
        <dd>
            <ul>
                <c:forEach var="el" items="${job.properties}">
                    <c:if test="${not fn:contains(el.key, ':') && not fn:startsWith(el.key, '_') && not fn:startsWith(el.key, 'event.job.')}">
                        <li>
                            <strong>
                                <fmt:message key="${el.key}" var="label" />
                                ${sling:encode(label,'HTML')}:
                            </strong>
                            ${sling:encode(el.value,'HTML')}
                        </li>
                    </c:if>
                </c:forEach>
            </ul>
        </dd>
    </dl>
</div>