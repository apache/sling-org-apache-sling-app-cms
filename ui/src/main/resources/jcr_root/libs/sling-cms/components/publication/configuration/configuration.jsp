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
<c:if test="${not empty config[properties.key]}">
    <dt>
        <fmt:message key="${properties.title}" var="title" />
        <sling:encode value="${title}" mode="HTML" />
    </dt>
    <dd>
        <c:choose>
            <c:when test="${not empty properties.objectClass}">
                <a href="/system/console/services?filter=%28%26%28objectClass%3D${properties.objectClass}%29%28name%3D${sling:encode(config[properties.key],'HTML_ATTR')}%29%29">
                    <sling:encode value="${config[properties.key]}" mode="HTML" />
                </a>
            </c:when>
            <c:otherwise>
                <sling:encode value="${config[properties.key]}" mode="HTML" />
            </c:otherwise>
        </c:choose>
    </dd>
</c:if>