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
<a href="/">
    <sling:encode value="${properties.home}" mode="HTML" />
</a>&nbsp;&#xbb;&nbsp;
<c:if test="${not empty properties.level}">
    <c:forEach var="parent" items="${sling:getParents(currentPage.resource,properties.level)}">
        <c:if test="${parent.path != page.resource.path}">
            <a href="${sling:encode(parent.path,'HTML_ATTR')}.html">
                <sling:encode value="${parent.valueMap['jcr:content/jcr:title']}" default="${parent.name}" mode="HTML" />
            </a>&nbsp;&#xbb;&nbsp;
        </c:if>
    </c:forEach>
</c:if>