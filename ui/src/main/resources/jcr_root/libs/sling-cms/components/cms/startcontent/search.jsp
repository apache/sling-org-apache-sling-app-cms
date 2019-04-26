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
<sling:adaptTo adaptable="${slingRequest}" adaptTo="org.apache.sling.cms.core.models.StartContent" var="startContent" />
<div>
    <c:set var="results" value="${false}" />
    <c:forEach var="item" items="${startContent.relatedContent}">
        <c:set var="results" value="${true}" />
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
            </c:choose>
        </a>
    </c:forEach>
    <c:if test="${!results}">
        <span class="panel-block">
            <fmt:message key="slingcms.noresults" />
        </span>
    </c:if>
</div>