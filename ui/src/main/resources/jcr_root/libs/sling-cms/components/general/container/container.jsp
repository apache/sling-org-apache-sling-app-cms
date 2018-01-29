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
 <c:choose>
	<c:when test="${not empty requestScope.availableTypes}">
		<c:set var="availableTypes" value="${requestScope.availableTypes}" />
	</c:when>
	<c:when test="${empty requestScope.availableTypes}">
		<sling:adaptTo var="pageMgr" adaptable="${resource}" adaptTo="org.apache.sling.cms.core.models.PageManager" />
		<c:set var="availableTypes" value="${fn:join(pageMgr.page.template.availableComponentTypes,',')}" />
	</c:when>
</c:choose>
<c:forEach var="child" items="${sling:listChildren(resource)}">
	<sling:include resource="${child}" />
</c:forEach>
<c:if test="${cmsEditEnabled == 'true'}">
	<div class="Sling-CMS__edit-bar">
		<button class="Sling-CMS__edit-button" data-sling-cms-action="add" data-sling-cms-path="${resource.path}" data-sling-cms-available-types="${availableTypes}" title="Add">
			&#43;
		</button>
	</div>
</c:if>