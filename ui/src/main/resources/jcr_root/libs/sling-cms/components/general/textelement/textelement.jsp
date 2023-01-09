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
<c:choose>
    <c:when test="${properties.i18n}">
        <fmt:message key="${properties.text}" var="text" />
    </c:when>
    <c:otherwise>
        <c:set var="text" value="${properties.text}"/>
    </c:otherwise>
</c:choose>
<c:choose>
    <c:when test="${not empty properties.href}">
        <${sling:encode(properties.level,'HTML')}>
            <a href="${sling:encode(properties.href,'HTML_ATTR')}">
                <sling:encode value="${text}" mode="HTML" />
            </a>
        </${sling:encode(properties.level,'HTML')}>
    </c:when>
    <c:otherwise>
        <${sling:encode(properties.level,'HTML')}><sling:encode value="${text}" mode="HTML" /></${sling:encode(properties.level,'HTML')}>
    </c:otherwise>
</c:choose>
