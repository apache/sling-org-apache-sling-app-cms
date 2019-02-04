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
<c:if test="${slingRequest.requestPathInfo.suffix != null}">
    <sling:getResource path="${slingRequest.requestPathInfo.suffix}" var="editedResource" />
    <c:set var="editProperties" value="${sling:adaptTo(editedResource,'org.apache.sling.api.resource.ValueMap')}" scope="request"/>
</c:if>
<c:choose>
    <c:when test="${properties.required}">
        <c:set var="required" value="required='required'" scope="request" />
    </c:when>
    <c:otherwise>
        <c:set var="required" value="" scope="request" />
    </c:otherwise>
</c:choose>
<c:choose>
    <c:when test="${properties.disabled}">
        <c:set var="disabled" value="disabled='disabled'" scope="request" />
    </c:when>
    <c:otherwise>
        <c:set var="disabled" value="" scope="request" />
    </c:otherwise>
</c:choose>
<c:choose>
    <c:when test="${properties.skipload}">
        <c:set var="value" value="" scope="request" />
    </c:when>
    <c:when test="${empty editProperties[properties.name] && properties.defaultValue}">
        <c:set var="value" value="${properties.defaultValue}" scope="request" />
    </c:when>
    <c:otherwise>
        <c:set var="value" value="${editProperties[properties.name]}" scope="request" />
    </c:otherwise>
</c:choose>
<div class="field">
    <c:if test="${not empty properties.label}">
        <label class="label" for="${properties.name}">
            <sling:encode value="${properties.label}" mode="HTML" />
            <c:if test="${properties.required}"><span class="has-text-danger">*</span></c:if>
        </label>
    </c:if>
    <div class="control">
    <sling:call script="field.jsp" />
    </div>
</div>