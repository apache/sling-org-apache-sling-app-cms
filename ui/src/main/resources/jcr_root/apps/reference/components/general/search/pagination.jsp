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
<c:if test="${not empty param.q && not empty query}">
	<nav>
		<ul class="${searchConfig.valueMap.paginationClass}">
			<c:choose>
				<c:when test="${not empty param.page && param.page != '0'}">
					<li class="${searchConfig.valueMap.pageItemClass} disabled">
						<span class="${searchConfig.valueMap.pageLinkClass}">
							&lt;
						</span>
					</li>
				</c:when>
				<c:otherwise>
					<li class="${searchConfig.valueMap.pageItemClass} disabled">
						<a class="${searchConfig.valueMap.pageLinkClass}" href="?q=${sling:encode(param.q,'HTML_ATTR')}">&lt;</a>
					</li>
				</c:otherwise>
			</c:choose>
			<c:set var="hasMode" value="false" />
			<c:forEach var="item" items="${sling:findResources(resourceResolver,query,'JCR-SQL2')}" step="${properties.limit}" varStatus="status">
				<li class="${searchConfig.valueMap.pageItemClass} ">
					<a href="?q=${sling:encode(param.q,'HTML_ATTR')}&page=${status.index}" class="${searchConfig.valueMap.pageLinkClass}">
						${status.index + 1}
					</a>
				</li>
				<c:if test="${status.last && param.page lt status.index}">
					<c:set var="hasMode" value="true" />
				</c:if>
			</c:forEach>
			<c:choose>
				<c:when test="${hasMode == 'false'}">
					<li class="${searchConfig.valueMap.pageItemClass} disabled">
						<span class="${searchConfig.valueMap.pageLinkClass}">
							&gt;
						</span>
					</li>
				</c:when>
				<c:otherwise>
					<li class="${searchConfig.valueMap.pageItemClass} disabled">
						<a class="${searchConfig.valueMap.pageLinkClass}" href="?q=${sling:encode(param.q,'HTML_ATTR')}&page=${param.page + 1}">&gt;</a>
					</li>
				</c:otherwise>
			</c:choose>
		</ul>
	</nav>
</c:if>