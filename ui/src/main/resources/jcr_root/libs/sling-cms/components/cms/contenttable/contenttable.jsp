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
 <a class="Button Fetch-Modal" data-title="Add Page" data-path=".Main-Content form" href="/cms/page/create.html${slingRequest.requestPathInfo.suffix}">+ Page</a>
  | <a class="Button Fetch-Modal" data-title="Add File" data-path=".Main-Content form" href="/cms/file/upload.html${slingRequest.requestPathInfo.suffix}">+ File</a>
  | <a class="Button Fetch-Modal" data-title="Add Folder" data-path=".Main-Content form" href="/cms/folder/create.html${slingRequest.requestPathInfo.suffix}">+ Folder</a>
<sling:getParent resource="${slingRequest.requestPathInfo.suffixResource}" var="site" level="2" />
<ul class="Breadcrumb">
	<li class="Breadcrumb-Item">
		<a href="/cms/site/content.html${site.path}">
			<sling:encode value="${site.valueMap['jcr:title']}" mode="HTML" />
		</a>
	</li>
	<c:if test="${site.path != slingRequest.requestPathInfo.suffix && site.path != slingRequest.requestPathInfo.suffixResource.parent.path}">
		<c:forEach var="parent" items="${sling:getParents(slingRequest.requestPathInfo.suffixResource,3)}">
			<li class="Breadcrumb-Item">
				<a href="/cms/site/content.html${parent.path}">
					<sling:encode value="${parent.valueMap['jcr:content/jcr:title']}" default="${parent.name}" mode="HTML" />
				</a>
			</li>
		</c:forEach>
	</c:if>
	<c:if test="${site.path != slingRequest.requestPathInfo.suffix}">
		<li class="Breadcrumb-Item">
			<sling:encode value="${slingRequest.requestPathInfo.suffixResource.valueMap['jcr:content/jcr:title']}" default="${slingRequest.requestPathInfo.suffix}" mode="HTML" />
		</li>
	</c:if>
</ul>
<table>
	<thead>
		<tr>
			<c:forEach var="column" items="${sling:listChildren(sling:getRelativeResource(resource,'columns'))}">
				<th class="Column-${column.name}">
					<sling:encode value="${column.valueMap.title}" mode="HTML" />
				</th>
			</c:forEach>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="child" items="${sling:listChildren(slingRequest.requestPathInfo.suffixResource)}">
			<sling:getResource var="typeConfig" base="${resource}" path="types/${child.resourceType}" />
			<c:if test="${typeConfig != null}">
				<tr data-resource="${child.path}" data-type="${typeConfig.path}">
					<c:forEach var="column" items="${sling:listChildren(sling:getRelativeResource(typeConfig,'columns'))}">
					<sling:getResource var="colConfig" base="${typeConfig}" path="columns/${column.name}" />
						<c:if test="${colConfig != null}">
							<c:choose>
								<c:when test="${colConfig.valueMap.type == 'Actions'}">
									<td class="Cell-${colConfig.valueMap.type}">
										<c:forEach var="actionConfig" items="${sling:listChildren(colConfig)}">
											<c:choose>
												<c:when test="${actionConfig.valueMap.modal}">
													<a class="Button Fetch-Modal" data-title="${sling:encode(actionConfig.valueMap.title,'HTML_ATTR')}" data-path=".Main-Content form" href="${actionConfig.valueMap.prefix}${child.path}" title="${sling:encode(actionConfig.valueMap.title,'HTML_ATTR')}">
														${actionConfig.valueMap.text}
													</a>
												</c:when>
												<c:otherwise>
													<a class="Button" target="_blank" href="${actionConfig.valueMap.prefix}${child.path}" title="${sling:encode(actionConfig.valueMap.title,'HTML_ATTR')}">
														${actionConfig.valueMap.text}
													</a>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</td>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${colConfig.valueMap.type == 'Static'}">
											<c:set var="colValue" value="${colConfig.valueMap.value}" />
										</c:when>
										<c:when test="${colConfig.valueMap.type == 'String'}">
											<c:set var="colValue" value="${child.valueMap[colConfig.valueMap.property]}" />
										</c:when>
										<c:when test="${colConfig.valueMap.type == 'Date'}">
											<fmt:formatDate var="colValue" type = "both"  dateStyle = "medium" timeStyle = "medium" value = "${child.valueMap[colConfig.valueMap.property].time}" />
										</c:when>
										<c:when test="${colConfig.valueMap.type == 'Name'}">
											<c:set var="colValue" value="${child.name}" />
										</c:when>
									</c:choose>
									<td class="Cell-${colConfig.valueMap.type}" title="${sling:encode(colValue,'HTML_ATTR')}">
										<c:choose>
											<c:when test="${colConfig.valueMap.link}">
												<a href="/cms/site/content.html${child.path}">
													<sling:encode value="${colValue}" mode="HTML" />
												</a>
											</c:when>
											<c:otherwise>
												<sling:encode value="${colValue}" mode="HTML" />
											</c:otherwise>
										</c:choose>
									</td>
								</c:otherwise>
							</c:choose>
						</c:if>
					</c:forEach>
				</tr>
			</c:if>
		</c:forEach> 
	</tbody>
</table>