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
<br/><br/>
<sling:adaptTo adaptable="${slingRequest}" adaptTo="org.apache.sling.cms.core.models.StartContent" var="startContent" />
<div class="columns">
    <div class="column">
        <fmt:message key="Recent Content" var="recentContentMessage" />
        <nav class="panel has-background-white" aria-label="${recentContentMessage}">
            <p class="panel-heading">
                ${recentContentMessage}
            </p>
            <c:forEach var="item" items="${startContent.recentContent}">
                <a class="panel-block" title="${sling:encode(item.path,'HTML_ATTR')}" href="/cms/site/content.html${sling:encode(item.parent.path,'HTML_ATTR')}?resource=${sling:encode(item.path,'HTML_ATTR')}">
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
                            <sling:encode value="${item.name}" mode="HTML" />
                        </c:otherwise>
                    </c:choose>&nbsp;&mdash;&nbsp;
                    <small><fmt:formatDate value="${item.valueMap['jcr:content/jcr:lastModified'].time}" type="both" dateStyle="short" timeStyle="short" /></small>
                </a>
            </c:forEach>
        </nav>
        <fmt:message key="Recent Drafts" var="recentDraftsMessage" />
        <nav class="panel has-background-white" aria-label="${recentDraftsMessage}">
            <p class="panel-heading">
                ${recentDraftsMessage}
            </p>
            <c:forEach var="item" items="${startContent.recentDrafts}">
                <a class="panel-block" title="${sling:encode(item.path,'HTML_ATTR')}" href="/cms/site/content.html${sling:encode(item.parent.path,'HTML_ATTR')}?resource=${sling:encode(item.path,'HTML_ATTR')}">
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
                            <sling:encode value="${item.name}" mode="HTML" />
                        </c:otherwise>
                    </c:choose>&nbsp;&mdash;&nbsp;
                    <small><fmt:formatDate value="${item.valueMap['jcr:content/jcr:lastModified'].time}" type="both" dateStyle="short" timeStyle="short" /></small>
                </a>
            </c:forEach>
        </nav>
    </div>
    <div class="column">
        <nav class="panel has-background-white">
            <p class="panel-heading">
                <fmt:message key="Find Content" />
            </p>
            <div class="panel-block">
                <form method="get" class="get-form" data-target=".search-result-container" data-load="div" action="${sling:encode(resource.path,'HTML_ATTR')}.search.html">
                    <p class="control has-icons-left">
                        <label class="is-sr-only" for="search-term"><fmt:message key="Search" /></label>
                        <input class="input is-small" type="text" name="q" id="search-term" />
                        <span class="icon is-small is-left">
                            <i class="jam jam-search" aria-hidden="true"></i>
                        </span>
                        <input class="is-right is-sr-only" type="submit" value="Search" id="search-submit" />
                    </p>
                </form>
            </div>
            <div class="search-result-container"></div>
        </nav>
    </div>
</div>