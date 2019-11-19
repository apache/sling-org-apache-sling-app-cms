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
                <fmt:message key="slingcms.jobs" />
            </a>
        </li>
        <li class="is-active">
            <a href="#">
                <fmt:message key="${job.properties._titleKey}" />
            </a>
        </li>
    </ul>
</nav>
<h2><fmt:message key="${job.properties._titleKey}" /></h2>
<div class="scroll-container">
    <dl>
        <dt><fmt:message key="slingcms.state" /></dt>
        <dd>${job.jobState}</dd>
        <c:if test="${not empty job.resultMessage}">
            <dt><fmt:message key="slingcms.jobs.resultmessage" /></dt>
            <dd>${sling:encode(job.resultMessage,'HTML')}</dd>
        </c:if>
        <dt><fmt:message key="slingcms.started" /></dt>
        <dd><fmt:formatDate value="${job.created.time}" type="both" dateStyle="long" timeStyle="long" /></dd>
        <c:if test="${job.finishedDate != null}">
            <dt><fmt:message key="slingcms.finished" /></dt>
            <dd>
                <fmt:formatDate value="${job.finishedDate.time}" type="both" dateStyle="long" timeStyle="long" />
            </dd>
        </c:if>
        <c:if test="${job.progressStepCount > 0}">
            <dt><fmt:message key="slingcms.jobs.progress" /></dt>
            <dd>
                ${job.finishedProgressStep} / ${job.progressStepCount}
            </dd>
        </c:if>
        <c:if test="${job.progressLog != null && fn:length(job.progressLog) > 0}">
            <dt><fmt:message key="slingcms.jobs.progresslog" /></dt>
            <dd>
                <ul>
                    <c:forEach var="log" items="${job.progressLog}">
                        <li>${sling:encode(log,'HTML')}</li>
                    </c:forEach>
                </ul>
            </dd>
        </c:if>
        <dt><fmt:message key="slingcms.jobs.properties" /></dt>
        <dd>
            <ul>
                <c:forEach var="el" items="${job.properties}">
                    <c:if test="${not fn:contains(el.key, ':') && not fn:startsWith(el.key, '_') && not fn:startsWith(el.key, 'event.job.')}">
                        <li><strong>${sling:encode(el.key,'HTML')}:</strong>
                        ${sling:encode(el.value,'HTML')}
                    </c:if>
                </c:forEach>
            </ul>
        </dd>
    </dl>
</div>