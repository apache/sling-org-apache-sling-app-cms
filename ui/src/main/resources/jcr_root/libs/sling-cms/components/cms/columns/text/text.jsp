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
<c:set var="colValue" value="${resource.valueMap[colConfig.valueMap.property]}" />
<td title="${sling:encode(colValue,'HTML_ATTR')}">
	<c:choose>
		<c:when test="${colConfig.valueMap.link}">
			<a href="${colConfig.valueMap.prefix}${resource.path}">
				<sling:encode value="${colValue}" mode="HTML" />
			</a>
		</c:when>
		<c:otherwise>
			<sling:encode value="${colValue}" mode="HTML" />
		</c:otherwise>
	</c:choose>
</td>