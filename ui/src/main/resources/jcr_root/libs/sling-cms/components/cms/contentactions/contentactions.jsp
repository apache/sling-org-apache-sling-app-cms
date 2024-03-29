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
<nav class="level">
    <div class="level-left">
        <div class="level-item">
            <div class="buttons has-addons">
                <c:forEach var="action" items="${sling:listChildren(sling:getRelativeResource(resource,'actions'))}" varStatus="status">
                    <a class="button Fetch-Modal" data-title="Add ${action.valueMap.label}" data-path=".Main-Content form" href="${action.valueMap.prefix}${sling:encode(slingRequest.requestPathInfo.suffix,'HTML_ATTR')}${action.valueMap.suffix}">
                        + <fmt:message key="${action.valueMap.label}" var="label" />${sling:encode(label,'HTML_ATTR')}
                    </a>
                </c:forEach>
            </div>
        </div>
        <div class="level-item">
            <div class="buttons has-addons actions-target">
            </div>
        </div>
    </div>
    <c:if test="${properties.includeSwitcher}">
        <div class="level-right">
            <div class="level-item">
                <div class="field">
                    <div class="control has-icons-left">
                        <sling:adaptTo adaptable="${resourceResolver}" adaptTo="org.apache.sling.cms.AuthorizableWrapper" var="auth" />
                        <sling:getResource path="${auth.authorizable.path}/profile" var="profile" />
                        <c:set var="pagePath" value="${sling:adaptTo(resource,'org.apache.sling.cms.PageManager').page.path}" />
                        <form method="get" action="" class="layout-switch is-inline">
                            <div class="select">
                                <select aria-label="Select View">
                                    <c:choose>
                                        <c:when test="${slingRequest.requestPathInfo.selectorString == 'table' || (profile.valueMap.defaultLayout == 'table' && slingRequest.requestPathInfo.selectorString != 'grid')}">
                                            <option value="/cms${sling:encode(fn:substring(pagePath,30,fn:length(pagePath)),'HTML_ATTR')}.grid.html${sling:encode(slingRequest.requestPathInfo.suffix,'HTML_ATTR')}"><fmt:message key="Grid" /></option>
                                            <option selected value="/cms${sling:encode(fn:substring(pagePath,30,fn:length(pagePath)),'HTML_ATTR')}.table.html$${sling:encode(slingRequest.requestPathInfo.suffix,'HTML_ATTR')}"><fmt:message key="Table" /></option>
                                        </c:when>
                                        <c:otherwise>
                                            <option selected value="/cms${sling:encode(fn:substring(pagePath,30,fn:length(pagePath)),'HTML_ATTR')}.grid.html${sling:encode(slingRequest.requestPathInfo.suffix,'HTML_ATTR')}"><fmt:message key="Grid" /></option>
                                            <option value="/cms${sling:encode(fn:substring(pagePath,30,fn:length(pagePath)),'HTML_ATTR')}.table.html${sling:encode(slingRequest.requestPathInfo.suffix,'HTML_ATTR')}"><fmt:message key="Table" /></option>
                                        </c:otherwise>
                                    </c:choose>
                                </select>
                            </div>
                            <div class="icon is-small is-left">
                                <c:choose>
                                    <c:when test="${slingRequest.requestPathInfo.selectorString == 'table' || (profile.valueMap.defaultLayout == 'table' && slingRequest.requestPathInfo.selectorString != 'grid')}">
                                        <i class="jam jam-unordered-list"></i>
                                    </c:when>
                                    <c:otherwise>
                                        <i class="jam jam-grid"></i>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
</nav>