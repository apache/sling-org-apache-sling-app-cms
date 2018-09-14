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
<td class="has-text-centered" data-value="${sling:getRelativeResource(resource,'jcr:content').valueMap.published ? 0 : 1}">
	<c:choose>
		<c:when test="${sling:getRelativeResource(resource,'jcr:content').valueMap.published}">
			<a class="button is-success is-centered  Fetch-Modal" href="/cms/actions/shared/unpublish.html${resource.path}" title="Click to Unpublish" data-title="Unpublish" data-path=".Main-Content form">
				<i class="jam jam-download"></i>
			</a>
		</c:when>
		<c:otherwise>
			<a class="button is-danger Fetch-Modal" href="/cms/actions/shared/publish.html${resource.path}" title="Click to Publish" data-title="Publish" data-path=".Main-Content form">
				<i class="jam jam-upload"></i>
			</a>
		</c:otherwise>
	</c:choose>
</td>