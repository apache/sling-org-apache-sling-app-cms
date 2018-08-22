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
<div id="search-results">
	<div class="Grid">
	<sling:adaptTo adaptable="${slingRequest}" adaptTo="org.apache.sling.cms.core.models.SearchResults" var="results" />
		<c:forEach var="result" items="${results.results}" begin="0" end="100" varStatus="status">
			<div class="Cell Search-Result Small-50">
				<div class="Search-Result__Container">
					<c:choose>
						<c:when test="${not empty result.valueMap['jcr:content/jcr:title']}">
							<c:set var="title" value="${result.valueMap['jcr:content/jcr:title']}" />
						</c:when>
						<c:when test="${not empty result.valueMap['jcr:title']}">
							<c:set var="title" value="${result.valueMap['jcr:title']}" />
						</c:when>
						<c:otherwise>
							<c:set var="title" value="${result.name}" />
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${result.valueMap['jcr:primaryType'] == 'sling:Page'}">
							<c:set var="icon" value="document" />
						</c:when>
						<c:when test="${result.valueMap['jcr:primaryType'] == 'nt:file' || result.valueMap['jcr:primaryType'] == 'sling:File'}">
							<c:set var="icon" value="file" />
						</c:when>
						<c:when test="${result.valueMap['jcr:primaryType'] == 'nt:folder' || result.valueMap['jcr:primaryType'] == 'sling:Folder' || result.valueMap['jcr:primaryType'] == 'sling:OrderedFolder'}">
							<c:set var="icon" value="folder" />
						</c:when>
						<c:otherwise>
							<c:set var="icon" value="sitemap" />
						</c:otherwise>
					</c:choose>
					<h4>
						<span class="jam jam-${icon}"></span>
						${sling:encode(title,'HTML')}
					</h4>
					<small>
						<em>${result.path}</em>
					</small><br/>
					<br />
					<a href="#" class="Button Select-Button" data-path="${result.path}">Select</a>
				</div>
			</div>
		</c:forEach>
	</div>
</div>