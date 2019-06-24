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
<nav>
    <ul class="${listConfig.valueMap.paginationClass}">
        <c:choose>
            <c:when test="${list.first == true}">
                <li class="${listConfig.valueMap.pageItemClass} disabled">
                    <span class="${listConfig.valueMap.pageLinkClass}">
                        &lt;
                    </span>
                </li>
            </c:when>
            <c:otherwise>
                <li class="${listConfig.valueMap.pageItemClass}">
                    <a class="${listConfig.valueMap.pageLinkClass}" href="?page=${list.currentPage - 1}">&lt;</a>
                </li>
            </c:otherwise>
        </c:choose>
        <c:forEach var="page" items="${list.pages}">
            <li class="${listConfig.valueMap.pageItemClass} ${page == list.currentPage ? 'active' : ''}">
                <a href="?page=${page}" class="${listConfig.valueMap.pageLinkClass}">
                    ${page}
                </a>
            </li>
        </c:forEach>
        <c:choose>
            <c:when test="${list.last}">
                <li class="${listConfig.valueMap.pageItemClass} disabled">
                    <span class="${listConfig.valueMap.pageLinkClass}">
                        &gt;
                    </span>
                </li>
            </c:when>
            <c:otherwise>
                <li class="${listConfig.valueMap.pageItemClass}">
                    <a class="${listConfig.valueMap.pageLinkClass}" href="?page=${list.currentPage + 1}">&gt;</a>
                </li>
            </c:otherwise>
        </c:choose>
    </ul>
</nav>