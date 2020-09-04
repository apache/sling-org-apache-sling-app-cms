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
<c:set var="importer" value="${slingRequest.requestPathInfo.suffixResource}" />
<c:set var="importerCfg" value="${importer.valueMap}" />
<nav class="breadcrumb" aria-label="breadcrumbs">
    <ul>
        <li>
            <a href="/cms/publication/home.html">
                Publication
            </a>
        </li>
        <li>
            <a href="/cms/publication/importers.html/libs/sling/distribution/settings/importers">
                Importers
            </a>
        </li>
        <li class="is-active">
            <a href="#">
                <sling:encode value="${importerCfg.name}" mode="HTML" />
            </a>
        </li>
    </ul>
</nav>
<h1 class="title">
    <sling:encode value="${importerCfg.name}" mode="HTML" />
</h1>
<div class="scroll-container">
    <div class="columns">
        <div class="column is-6">
            <article class="message is-light">
                <div class="message-header">
                    <p>Configuration</p>
                </div>
                <div class="message-body">
                    <dl>
                        <sling:getResource base="${resource}" path="configuration" var="configuration" />
                        <c:set var="config" value="${importerCfg}" scope="request" />
                        <c:forEach var="configRsrc" items="${sling:listChildren(configuration)}">
                            <sling:include resource="${configRsrc}" />
                        </c:forEach>
                    </dl>
                    <a class="button" href="/system/console/configMgr/${importerCfg['service.pid']}">Edit</a>
                </div>
            </article>
        </div>
    </div>
</div>


