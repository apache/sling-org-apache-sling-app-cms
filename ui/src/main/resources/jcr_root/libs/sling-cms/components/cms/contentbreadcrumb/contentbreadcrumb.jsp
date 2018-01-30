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
<sling:getParent resource="${slingRequest.requestPathInfo.suffixResource}" var="site" level="${resource.valueMap.depth}" />
<ul class="Breadcrumb">
	<li class="Breadcrumb-Item">
		<a href="${resource.valueMap.prefix}${site.path}">
			<sling:encode value="${site.valueMap['jcr:title']}" mode="HTML" />
		</a>
	</li>
	<c:if test="${site.path != slingRequest.requestPathInfo.suffix && site.path != slingRequest.requestPathInfo.suffixResource.parent.path}">
		<c:forEach var="parent" items="${sling:getParents(slingRequest.requestPathInfo.suffixResource,(resource.valueMap.depth + 1))}">
			<li class="Breadcrumb-Item">
				<a href="${resource.valueMap.prefix}${parent.path}">
					<sling:encode value="${parent.valueMap[resource.valueMap.titleProp]}" default="${parent.name}" mode="HTML" />
				</a>
			</li>
		</c:forEach>
	</c:if>
	<c:if test="${site.path != slingRequest.requestPathInfo.suffix}">
		<li class="Breadcrumb-Item">
			<sling:encode value="${slingRequest.requestPathInfo.suffixResource.valueMap[resource.valueMap.titleProp]}" default="${slingRequest.requestPathInfo.suffix}" mode="HTML" />
		</li>
	</c:if>
</ul>