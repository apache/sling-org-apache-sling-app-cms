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
<c:set var="encoding" value="multipart/form-data" />
<c:if test="${not empty properties.encoding}">
    <c:set var="encoding" value="${properties.encoding}" />
</c:if>
<form method="post"
    action="${sling:encode(slingRequest.requestPathInfo.suffix,'HTML_ATTR')}${sling:encode(properties.actionSuffix,'HTML_ATTR')}"
    enctype="${sling:encode(encoding,'HTML_ATTR')}" class="Form-Ajax"
    data-add-date="${properties.addDate != false}"
    data-callback="${sling:encode(properties.callback,'HTML_ATTR')}">
    <div class="form-wrapper pt-4 field">
        <input type="hidden" name="_charset_" value="utf-8" />
        <sling:include path="fields" resourceType="sling-cms/components/general/container" />
        <div class="field">
            <button type="submit" class="button is-primary">
                <c:choose>
                    <c:when test="${not empty properties.button}">
                        <fmt:message key="${properties.button}" var="button" />
                    </c:when>
                    <c:otherwise>
                        <fmt:message key="Save" var="button" />
                    </c:otherwise>
                </c:choose>
                <sling:encode value="${button}" mode="HTML" />
            </button>
            <c:if test="${properties.skipcancel != true}">
                <a href="${sling:encode(header.referer,'HTML_ATTR')}" class="button close">
                    <fmt:message key="Cancel" />
                </a>
            </c:if>
        </div>
    </div>
</form>