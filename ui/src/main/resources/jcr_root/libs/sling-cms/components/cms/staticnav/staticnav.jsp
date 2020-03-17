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
<nav class="menu">
    <a class="menu-label toggle-hidden" data-target="#${fn:replace(properties.title,' ','-')}-nav">
        <sling:encode value="${properties.title}" mode="HTML" />
    </a>
    <c:set var="hidden" value="is-hidden" />
    <c:forEach var="item" items="${sling:listChildren(sling:getRelativeResource(resource,'links'))}">
        <c:if test="${fn:startsWith(slingRequest.requestURI,item.valueMap.link)}">
            <c:set var="hidden" value="" />
        </c:if>
        <c:forEach var="alternative" items="${item.valueMap.alternatives}">
            <c:if test="${fn:startsWith(slingRequest.requestURI,alternative)}">
                <c:set var="hidden" value="" />
            </c:if>
        </c:forEach>
    </c:forEach>
    <sling:adaptTo var="currentUser" adaptable="${slingRequest.resourceResolver}" adaptTo="org.apache.sling.cms.AuthorizableWrapper" />
    <ul id="${fn:replace(properties.title,' ','-')}-nav" class="menu-list ${hidden}">
        <c:forEach var="item" items="${sling:listChildren(sling:getRelativeResource(resource,'links'))}">
            <c:set var="selected" value="" />
            <c:if test="${fn:startsWith(slingRequest.requestURI,item.valueMap.link)}">
                <c:set var="selected" value="is-selected" />
            </c:if>
            <c:forEach var="alternative" items="${item.valueMap.alternatives}">
                <c:if test="${fn:startsWith(slingRequest.requestURI,alternative)}">
                    <c:set var="selected" value="is-selected" />
                </c:if>
            </c:forEach>
            <c:set var="enabled" value="${currentUser.administrator || empty item.valueMap.enabledGroups}" />
            <c:if test="${not empty item.valueMap.enabledGroups && !currentUser.administrator}">
                <c:set var="enabled" value="${false}" />
                <c:forEach var="group" items="${item.valueMap.enabledGroups}">
                    <c:forEach var="userGroup" items="${currentUser.groupNames}">
                        <c:if test="${group == userGroup}">
                            <c:set var="enabled" value="${true}" />
                        </c:if>
                    </c:forEach>
                </c:forEach>
            </c:if>
            <c:if test="${enabled}">
                <li class="${selected}"><a href="${item.valueMap.link}">${item.valueMap.text}</a></li>
            </c:if>
        </c:forEach>
    </ul>
</nav>
