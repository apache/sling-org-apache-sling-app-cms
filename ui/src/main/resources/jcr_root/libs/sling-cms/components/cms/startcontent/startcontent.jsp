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
<br/><hr/><br/>
<sling:adaptTo adaptable="${slingRequest}" adaptTo="org.apache.sling.cms.core.models.StartContent" var="startContent" />
<div class="columns">
    <div class="column">
        <nav class="panel" aria-label="<fmt:message key="slingcms.recentcontent" />">
            <p class="panel-heading">
                <fmt:message key="slingcms.recentcontent" />
            </p>
            <c:forEach var="item" items="${startContent.recentContent}">
                <a class="panel-block" title="${item.path}" href="/cms/site/content.html${item.parent.path}?resource=${item.path}">
                    <span class="panel-icon">
                        <c:choose>
                            <c:when test="${item.resourceType == 'sling:Page'}">
                                <i class="jam jam-document" aria-hidden="true"></i>
                            </c:when>
                            <c:otherwise>
                                <i class="jam jam-file" aria-hidden="true"></i>
                            </c:otherwise>
                        </c:choose>
                    </span>
                    <c:choose>
                        <c:when test="${item.resourceType == 'sling:Page'}">
                            <sling:encode value="${item.valueMap['jcr:content/jcr:title']}" default="${item.name}" mode="HTML" />
                        </c:when>
                        <c:otherwise>
                            ${item.name}
                        </c:otherwise>
                    </c:choose>&nbsp;&mdash;&nbsp;
                    <small><fmt:formatDate value="${item.valueMap['jcr:content/jcr:lastModified'].time}" type="both" dateStyle="short" timeStyle="short" /></small>
                </a>
            </c:forEach>
        </nav>
    </div>
    <div class="column">
        <nav class="panel">
            <p class="panel-heading">
                <fmt:message key="slingcms.findcontent" />
            </p>
            <div class="panel-block">
                <form method="get" class="get-form" data-target=".search-result-container" data-load="div" action="${resource.path}.search.html">
                    <p class="control has-icons-left">
                        <label class="is-vhidden" for="search-term">Search</label>
                        <input class="input is-small" type="text" name="q" id="search-term" />
                        <span class="icon is-small is-left">
                            <i class="jam jam-search" aria-hidden="true"></i>
                        </span>
                    </p>
                </form>
            </div>
            <div class="search-result-container"></div>
        </nav>
    </div>
</div>