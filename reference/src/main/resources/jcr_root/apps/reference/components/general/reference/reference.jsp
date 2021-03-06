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
<c:if test="${not empty properties.reference}">
    <c:set var="referenceResource" value="${sling:getResource(resourceResolver,properties.reference)}" />
    <c:if test="${referenceResource != null}">
        <c:set var="originalEditEnabled" value="${cmsEditEnabled}"/>
        <c:set var="cmsEditEnabled" value="false" scope="request" />
        <c:choose>
            <c:when test="${not empty properties.overrideType}">
                <sling:include path="${properties.reference}" resourceType="${properties.overrideType}" />
            </c:when>
            <c:otherwise>
                <sling:include resource="${referenceResource}" />
            </c:otherwise>
        </c:choose>
        <c:set var="cmsEditEnabled" value="${originalEditEnabled}" scope="request" />
    </c:if>
</c:if>