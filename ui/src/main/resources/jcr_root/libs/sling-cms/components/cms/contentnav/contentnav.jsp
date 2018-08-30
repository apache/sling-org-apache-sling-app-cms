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
<sling:findResources var="content" query="${properties.query}" language="JCR-SQL2" />
<aside class="menu">
<a class="menu-label toggle-hidden" data-target="#nav-${fn:replace(properties.title,' ','-')}">${properties.title}</a>
<ul class="menu-list ${fn:startsWith(slingRequest.requestURI, properties.prefix) ? '' : 'is-hidden'}" id="nav-${fn:replace(properties.title,' ','-')}">
    <c:forEach var="item" items="${content}">
        <c:set var="prefixPath" value="${item.path}/" />
        <li class="${(fn:startsWith(slingRequest.requestPathInfo.suffix, prefixPath) || slingRequest.requestPathInfo.suffix == item.path) ? 'is-active' : ''}">
            <a href="${properties.itemPrefix}${item.path}" title="View ${item.valueMap['jcr:title']}">
                <c:choose>
                    <c:when test="${sling:getRelativeResource(item,'jcr:content') != null}">
                        <sling:encode value="${sling:getRelativeResource(item,'jcr:content').valueMap['jcr:title']}" mode="HTML" />
                    </c:when>
                    <c:when test="${not empty item.valueMap['jcr:title']}">
                        <sling:encode value="${item.valueMap['jcr:title']}" mode="HTML" />
                    </c:when>
                    <c:otherwise>
                        <sling:encode value="${item.name}" mode="HTML" />
                    </c:otherwise>
                </c:choose>
            </a>
        </li>
    </c:forEach>
    <c:choose>
        <c:when test="${not empty properties.createTitle}">
            <c:set var="createTitle" value="${properties.createTitle}" />
        </c:when>
        <c:otherwise>
            <c:set var="createTitle" value="${properties.title}" />
        </c:otherwise>      
    </c:choose>
    <li><a href="${properties.createPath}" class="Fetch-Modal" title="Create a new ${createTitle}"  data-title="Create ${createTitle}" data-path=".Main-Content form"><span class="icon is-small"><i class="jam jam-plus"></i></span> ${createTitle}</a></li>
</ul>
</aside>