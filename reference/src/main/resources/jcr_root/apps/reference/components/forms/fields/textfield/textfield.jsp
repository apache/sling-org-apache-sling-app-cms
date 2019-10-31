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
<div class="${formConfig.fieldGroupClass} ${properties.addClasses}">
    <c:if test="${not empty properties.label}">
        <label for="${properties.name}">
            <sling:encode value="${properties.label}" mode="HTML" />
            <c:if test="${properties.required}">
                <span class="${formConfig.fieldRequiredClass}">
                    *
                </span>
            </c:if>
        </label>
    </c:if>
    <c:set var="placeholderStr" value="placeholder='${properties.placeholder}'" />
    <c:set var="patternStr" value="pattern='${properties.pattern}'" />
    <c:choose>
        <c:when test="${not empty formData[properties.name]}">
            <c:set var="fieldValue" value="${formData[properties.name]}" />
        </c:when>
        <c:when test="${not empty requestScope[properties.name]}">
            <c:set var="fieldValue" value="${requestScope[properties.name]}" />
        </c:when>
        <c:when test="${not empty properties.value}">
            <c:set var="fieldValue" value="${properties.value}" />
        </c:when>
    </c:choose>
    <input type="${properties.type}" class="${formConfig.fieldClass}" id="${properties.name}" name="${properties.name}" value="${fieldValue}" ${not empty properties.pattern ? patternStr : ''} ${not empty properties.placeholder ? placeholderStr : ''} ${properties.required ? 'required="required"' : ''}
        <c:forEach var="attr" items="${properties.additionalAttributes}">
            ${fn:split(attr,'\\=')[0]}="${fn:split(attr,'\\=')[1]}"
        </c:forEach> 
        />
</div>