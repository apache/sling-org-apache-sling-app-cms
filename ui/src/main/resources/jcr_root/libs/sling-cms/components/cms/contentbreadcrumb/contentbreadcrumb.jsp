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
<sling:adaptTo var="breadcrumb" adaptable="${slingRequest}" adaptTo="org.apache.sling.cms.core.models.ContentBreadcrumb" />
<nav class="breadcrumb" aria-label="breadcrumbs">
    <ul>
        <c:forEach var="parent" items="${breadcrumb.parents}">
            <li>
                <a href="${parent.left}">
                    <sling:encode value="${parent.right}" mode="HTML" />
                </a>
            </li>
        </c:forEach>
        <li class="is-active">
            <a href="#">
                <sling:encode value="${breadcrumb.currentItem}" mode="HTML" />
            </a>
        </li>
    </ul>
    <c:if test="${!properties.hideSearch}">
        <form method="get" class="contentnav-search">
            <p class="control has-icons-left">
                <label class="is-vhidden" for="search-term">Search</label>
                <input class="input is-small" type="text" name="search" id="search-term">
                <span class="icon is-small is-left">
                    <i class="jam jam-search" aria-hidden="true"></i>
                </span>
            </p>
        </form>
    </c:if>
</nav>
