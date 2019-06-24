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
<c:set var="pageParam" value="${not empty param.q ? param.q : '1'}" />
<nav>
    <ul class="${searchConfig.paginationClass}">
        <c:choose>
            <c:when test="${search.first == true}">
                <li class="${searchConfig.pageItemClass} disabled">
                    <span class="${searchConfig.pageLinkClass}">
                        &lt;
                    </span>
                </li>
            </c:when>
            <c:otherwise>
                <li class="${searchConfig.pageItemClass}">
                    <a class="${searchConfig.pageLinkClass}" href="?q=${sling:encode(search.term,'HTML_ATTR')}">&lt;</a>
                </li>
            </c:otherwise>
        </c:choose>
        <c:forEach var="page" items="${search.pages}">
            <li class="${searchConfig.pageItemClass}${page == pageParam ? ' active' : ''}">
                <a href="?q=${sling:encode(search.term,'HTML_ATTR')}&page=${page}" class="${searchConfig.pageLinkClass}${page == param.page ? ' active' : ''}">
                    ${page}
                </a>
            </li>
        </c:forEach>
        <c:choose>
            <c:when test="${search.last}">
                <li class="${searchConfig.pageItemClass} disabled">
                    <span class="${searchConfig.pageLinkClass}">
                        &gt;
                    </span>
                </li>
            </c:when>
            <c:otherwise>
                <li class="${searchConfig.pageItemClass}">
                    <a class="${searchConfig.pageLinkClass}" href="?q=${sling:encode(search.term,'HTML_ATTR')}&page=${search.currentPage + 1}">&gt;</a>
                </li>
            </c:otherwise>
        </c:choose>
    </ul>
</nav>