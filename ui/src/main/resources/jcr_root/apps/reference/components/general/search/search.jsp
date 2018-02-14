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
<sling:adaptTo var="pageMgr" adaptable="${resource}" adaptTo="org.apache.sling.cms.core.models.PageManager" />
<c:set var="searchConfig" value="${pageMgr.page.template.componentConfigs['reference/components/general/search']}" scope="request" />
${searchConfig }
<c:if test="${not empty properties.limit}">
	<div class="search ${searchConfig.valueMap.searchClass}">
		<div class="search__header">
			<fmt:message key="slingcms.search.header">
				<fmt:param value="${sling:encode(param.q,'HTML')}" />
			</fmt:message>
		</div>
		<div class="search__results">
			<c:if test="${not empty param.q}">
				<c:set var="quote" value="'"/>
				<c:set var="escape" value=""/>
				<c:catch>
					<c:set var="query" value="SELECT parent.* FROM [sling:Page] AS parent INNER JOIN [nt:base] AS child ON ISDESCENDANTNODE(child,parent) WHERE ISDESCENDANTNODE(parent, '/content/danklco-com') AND CONTAINS(child.*, '${fn:replace(param.q,quote,escape)}')" scope="request" />
					<sling:findResources var="results" query="${query}" language="JCR-SQL2" />
					<c:choose>
						<c:when test="${not empty param.page}">
							<c:set var="start" value="${param.page * properties.limit}" />
							<c:set var="end" value="${(param.page * properties.limit) + properties.limit}" />
						</c:when>
						<c:otherwise>
							<c:set var="start" value="0" />
							<c:set var="end" value="${properties.limit}" />
						</c:otherwise>
					</c:choose>
					<c:forEach var="result" items="${results}" begin="${start}" end="${end}">
						<c:set var="result" value="${result}" scope="request" />
						<sling:call script="result.jsp" />
					</c:forEach>
				</c:catch>
			</c:if>
		</div>
		<sling:call script="pagination.jsp" />
	</div>
</c:if>