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
<div class="menu">
<a class="menu-label toggle-hidden" data-target="#${fn:replace(properties.title,' ','-')}-nav">${properties.title}</a>
<c:set var="hidden" value="is-hidden" />
<c:forEach var="item" items="${sling:listChildren(sling:getRelativeResource(resource,'links'))}">
    <c:if test="${fn:startsWith(slingRequest.requestURI,item.valueMap.link)}">
        <c:set var="hidden" value="" />
    </c:if>
</c:forEach>
<ul id="${fn:replace(properties.title,' ','-')}-nav" class="menu-list ${hidden}">
    <c:forEach var="item" items="${sling:listChildren(sling:getRelativeResource(resource,'links'))}">
        <li ><a href="${item.valueMap.link}"class="${fn:startsWith(slingRequest.requestURI,item.valueMap.link) ? 'is-active' : ''}">${item.valueMap.text}</a>
        <c:if test="${fn:startsWith(slingRequest.requestURI,item.valueMap.link) }" >
            <sling:include path="bread" resourceType="sling-cms/components/cms/breadcrumbmenu" />
        </c:if>
        </li>
    </c:forEach>
</ul>
</div>