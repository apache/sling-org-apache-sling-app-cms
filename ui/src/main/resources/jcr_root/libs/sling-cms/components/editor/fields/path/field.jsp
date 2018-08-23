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
	<c:when test="${properties.hidesearch != true}">
		<div class="Grid">
			<div class="Cell Mobile-80">
				<input class="Field-Path" type="text" name="${properties.name}" value="${editProperties[properties.name]}" ${required} ${disabled} data-type="${properties.type}" data-base="${properties.basePath}" autocomplete="off" />
			</div>
			<div class="Cell Mobile-20">
				<a href="/cms/shared/search.html" class="Button Fetch-Modal Search-Button" data-title="Search" data-path=".Main-Content > .Grid > .Cell > *">
					<span class="jam jam-search"></span>
				</a>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<input class="Field-Path" type="text" name="${properties.name}" value="${editProperties[properties.name]}" ${required} ${disabled} data-type="${properties.type}" data-base="${properties.basePath}" autocomplete="off" />
	</c:otherwise>
</c:choose>