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
<c:set var="agent" value="${slingRequest.requestPathInfo.suffixResource}" />
<c:set var="agentCfg" value="${agent.valueMap}" />
<nav class="breadcrumb" aria-label="breadcrumbs">
    <ul>
        <li>
            <a href="/cms/publication/home.html">
                <fmt:message key="Publication" />
            </a>
        </li>
        <li>
            <a href="/cms/publication/agents.html/libs/sling/distribution/settings/agents">
                <fmt:message key="Agents" />
            </a>
        </li>
        <li class="is-active">
            <a href="#">
                <sling:encode value="${agentCfg.title}" mode="HTML" />
            </a>
        </li>
    </ul>
</nav>
<h1 class="title">
    <sling:encode value="${agentCfg.title}" mode="HTML" />
</h1>
<h2 class="subtitle">
    <sling:encode value="${agent.name}" mode="HTML" />
</h2>
<div class="scroll-container">
    <c:if test="${not empty agentCfg.details}">
        <p>
            <sling:encode value="${agent.name}" mode="HTML" />
        </p>
    </c:if>
    <div class="columns">
        <div class="column is-6">
            <article class="message is-light">
                <div class="message-header">
                    <p><fmt:message key="Configuration" /></p>
                </div>
                <div class="message-body">
                    <dl>
                        <sling:getResource base="${resource}" path="configuration" var="configuration" />
                        <c:set var="config" value="${agentCfg}" scope="request" />
                        <c:forEach var="configRsrc" items="${sling:listChildren(configuration)}">
                            <sling:include resource="${configRsrc}" />
                        </c:forEach>
                    </dl>
                    <div class="level">
                        <div class="level-left">
                            <div class="level-item">
                                <a class="button" href="/system/console/configMgr/${agentCfg['service.pid']}"><fmt:message key="Edit" /></a>
                            </div>
                            <div class="level-item">
                                <form method="post" action="/libs/sling/distribution/services/agents/${agent.name}" class="mb-0" target="_blank">
                                    <input type="hidden" name="action" value="TEST" />
                                    <button class="button" type="submit"><fmt:message key="Test" /></button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </article>
        </div>
        <div class="column is-6">
            <c:forEach var="url" items="${agentCfg['packageImporter.endpoints']}" varStatus="status">
                <article class="message is-light">
                    <sling:getResource var="queue" path="/libs/sling/distribution/services/agents/${agent.name}/queues/endpoint${status.index}" />
                    <div class="message-header">
                        <p><fmt:message key="Queue" /> #${status.index + 1}</p>
                    </div>
                    <div class="message-body">
                        <dl>
                            <dt><fmt:message key="Endpoint URL" /></dt>
                            <dd>
                                <sling:encode value="${url}" mode="HTML" />
                            </dd>
                            <dt><fmt:message key="Count" /></dt>
                            <dd>
                                <sling:encode value="${queue.valueMap.itemsCount}" mode="HTML" />
                            </dd>
                            <dt><fmt:message key="State" /></dt>
                            <dd>
                                <sling:encode value="${queue.valueMap.state}" mode="HTML" />
                            </dd>
                        </dl>
                    </div>
                </article>
            </c:forEach>
        </div>
    </div>
    
    <article class="message is-light">
        <div class="message-header">
            <p><fmt:message key="Logs" /></p>
        </div>
        <div class="message-body">
            <figure class="image is-16by9">
                <iframe class="has-ratio" src="/libs/sling/distribution/services/agents/${agent.name}/log.txt"></iframe>
            </figure>
        </div>
    </article>

</div>


