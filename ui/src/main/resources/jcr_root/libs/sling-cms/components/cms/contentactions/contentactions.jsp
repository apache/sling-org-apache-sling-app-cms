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
 <c:forEach var="action" items="${sling:listChildren(sling:getRelativeResource(resource,'actions'))}" varStatus="status">
 	<c:if test="${!status.first}"> | </c:if><a class="Button Fetch-Modal" data-title="Add ${action.valueMap.label}" data-path=".Main-Content form" href="${action.valueMap.prefix}${slingRequest.requestPathInfo.suffix}">+ ${action.valueMap.label}</a>
 </c:forEach>