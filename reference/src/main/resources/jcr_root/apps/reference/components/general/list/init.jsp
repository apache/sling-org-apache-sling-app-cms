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
 <sling:adaptTo adaptable="${resource}" adaptTo="org.apache.sling.cms.PageManager" var="pageMgr" />
<c:set var="query" value="${properties.query}" scope="request" />
<c:set var="limit" value="${properties.limit}" scope="request" />
<c:set var="includePagination" value="${properties.includePagination}" scope="request" />
<c:set var="tag" value="${not empty properties.tag ? properties.tag : 'ul'}" scope="request" />
<c:set var="clazz" value="${not empty properties.class ? properties.class : ''}" scope="request" />
<c:set var="listConfig" value="${sling:adaptTo(resource,'org.apache.sling.cms.ComponentConfiguration').resource}" scope="request" />
<c:if test="${not empty query}">
	<c:set var="list" value="${sling:adaptTo(slingRequest, 'org.apache.sling.cms.reference.models.ItemList')}" scope="request"  />
</c:if>