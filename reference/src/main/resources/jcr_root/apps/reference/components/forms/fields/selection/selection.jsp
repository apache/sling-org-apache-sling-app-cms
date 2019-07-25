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
<c:if test="${not empty properties.tagRoot}">
    <c:set var="children" value="${sling:listChildren(sling:getResource(resourceResolver,properties.tagRoot))}" />
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
        <c:choose>
            <c:when test="${properties.display == 'radioCheckbox'}">
                <c:forEach var="tag" items="${children}">
                    <div class="${formConfig.checkFieldClass}">
                        <c:set var="selected" value="${false}" />
                        <c:choose>
                            <c:when test="${properties.multiple}">
                                <c:forEach var="val" items="${formData[properties.name]}">
                                    <c:if test="${val == tag.name}">
                                        <c:set var="selected" value="${true}" />
                                    </c:if>
                                </c:forEach>
                            </c:when>
                            <c:when test="${formData[properties.name] == tag.name}">
                                <c:set var="selected" value="${true}" />
                            </c:when>
                        </c:choose>
                        <input class="${formConfig.checkInputClass}" type="${properties.multiple ? 'checkbox' : 'radio'}" name="${properties.name}" id="${properties.name}-${tag.name}" value="${tag.name}" ${selected ? 'checked="checked"' : ''} />
                        <label class="${formConfig.checkLabelClass}" for="${properties.name}-${tag.name}">
                            <sling:encode value="${tag.valueMap['jcr:title']}" mode="HTML" />
                        </label>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <select id="${properties.name}" class="form-control" ${properties.multiple ? 'multiple="multiple"' : ''} name="${properties.name}">
                    <c:if test="${not empty properties.noSelection && !properties.muliple}">
                        <option>
                            <sling:encode value="${properties.noSelection}" mode="HTML" />
                        </option>
                    </c:if>
                    <c:forEach var="tag" items="${children}">
                        <c:set var="selected" value="${false}" />
                        <c:choose>
                            <c:when test="${properties.multiple}">
                                <c:forEach var="val" items="${formData[properties.name]}">
                                    <c:if test="${val == tag.name}">
                                        <c:set var="selected" value="${true}" />
                                    </c:if>
                                </c:forEach>
                            </c:when>
                            <c:when test="${formData[properties.name] == tag.name}">
                                <c:set var="selected" value="${true}" />
                            </c:when>
                        </c:choose>
                        <option value="${tag.name}" ${selected ? 'selected="selected"' : ''} >
                            <sling:encode value="${tag.valueMap['jcr:title']}" mode="HTML" />
                        </option>
                    </c:forEach>
                </select>
            </c:otherwise>
        </c:choose>
    </div>
</c:if>