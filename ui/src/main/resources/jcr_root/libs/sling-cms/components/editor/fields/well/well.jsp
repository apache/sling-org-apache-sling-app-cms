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
<div class="card field">
    <c:choose>
        <c:when test="${properties.collapse}">
            <header class="card-header toggle-hidden" data-target="#${resource.name}">
                <p class="card-header-title">
                    <sling:encode value="${properties.title}" mode="HTML" />
                </p>
                <a href="#" class="card-header-icon" aria-label="Expand">
                    <span class="icon">
                        <i class="jam jam-chevron-down" aria-hidden="true"></i>
                    </span>
                </a>
            </header>
            <div class="card-content is-hidden" id="${resource.name}">
                <sling:include path="content" resourceType="sling-cms/components/general/container" />
            </div>
        </c:when>
        <c:otherwise>
            <header class="card-header">
                <p class="card-header-title">
                    <sling:encode value="${properties.title}" mode="HTML" />
                </p>
            </header>
            <div class="card-content">
                <sling:include path="content" resourceType="sling-cms/components/general/container" />
            </div>
        </c:otherwise>
    </c:choose>
</div>
