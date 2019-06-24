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
<sling:adaptTo var="pageMgr" adaptable="${resource}" adaptTo="org.apache.sling.cms.PageManager" />
<c:set var="searchConfig" value="${sling:adaptTo(resource,'org.apache.sling.cms.ComponentConfiguration').properties}" scope="request" />
<c:if test="${not empty properties.limit && not empty param.q}">
    <c:set var="search" value="${sling:adaptTo(slingRequest, 'org.apache.sling.cms.reference.models.Search')}" scope="request"  />
    <div class="search ${searchConfig.searchClass}">
        <div class="search__header">
            <fmt:message key="slingcms.search.header">
                <fmt:param value="${sling:encode(search.term,'HTML')}" />
                <fmt:param value="${search.start + 1}" />
                <fmt:param value="${search.end}" />
                <fmt:param value="${search.count}" />
            </fmt:message>
        </div>
        <div class="search__results">
            <c:forEach var="result" items="${search.results}">
                <c:set var="result" value="${result}" scope="request" />
                <sling:call script="result.jsp" />
            </c:forEach>
        </div>
        <sling:call script="pagination.jsp" />
    </div>
</c:if>${search.finalize}