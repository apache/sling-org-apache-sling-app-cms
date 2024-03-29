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
<c:set var="page" value="${sling:adaptTo(resource,'org.apache.sling.cms.PageManager').page}" />
<c:choose>
    <c:when test="${not empty properties.action}">
        <c:set var="action" value="${properties.action}" />
    </c:when>
    <c:otherwise>
        <c:set var="action" value="${page.path}.html" />
    </c:otherwise>
</c:choose>
<form method="get" action="${action}" class="get-form" data-target="${sling:encode(properties.target,'HTML_ATTR')}" data-load="${sling:encode(properties.load,'HTML_ATTR')}">
    <fieldset class="form-wrapper field">
        <input type="hidden" name="_charset_" value="utf-8" />
        <sling:include path="fields" resourceType="sling-cms/components/general/container" />
        <div class="Field-Group">
            <fmt:message key="${properties.button}" var="buttonMessage" />
            <button type="submit" class="button is-primary" title="${sling:encode(buttonMessage, 'HTML')}">
                <sling:encode value="${buttonMessage}" mode="HTML" />
            </button>
        </div>
    </fieldset>
</form>