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
<c:if test="${not empty properties.itemType}">
	<c:set var="basePath" value="${not empty properties.basePath ? properties.basePath : resource.path} }" />
	<c:set var="limit" value="${not empty properties.limit ? properties.limit : 1000} }" />
	<c:set var="tag" value="${not empty properties.tag ? properties.tag : 'ul'} }" />
	<c:set var="class" value="${not empty properties.class ? properties.class : ''} }" />
	<${tag} class="${class}">
		<c:forEach var="child" items="${sling:listChildren(sling:getResource(resourceResolver,basePath))}" end="${limit}">
			<c:if test="${child.resourceType == 'sling:Page'}">
				<sling:include path="${child.path}" resourceType="${properties.itemType}" />
			</c:if>
		</c:forEach>
	</${tag}>
</c:if>