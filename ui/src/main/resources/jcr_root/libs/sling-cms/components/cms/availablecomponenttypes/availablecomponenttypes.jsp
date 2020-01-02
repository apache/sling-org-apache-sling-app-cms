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
<div class="field">
    <label class="label" for="availableComponentTypes">
        Available Component Types
    </label>
    <div class="control">
        <c:set var="currentTypes" value="|${fn:join(slingRequest.requestPathInfo.suffixResource.valueMap.availableComponentTypes, '|')}|"/>
        <sling:adaptTo var="componentManager" adaptable="${resourceResolver}" adaptTo="org.apache.sling.cms.ComponentManager" />
        <c:forEach var="type" items="${componentManager.componentTypes}">
            <label class="checkbox is-block">
                <c:set var="search" value="|${type}|" />
                <c:choose>
                    <c:when test="${fn:contains(currentTypes,search)}">
                        <c:set var="checked">checked="checked"</c:set>
                    </c:when>
                    <c:otherwise>
                        <c:set var="checked" value="" />
                    </c:otherwise>
                </c:choose>
                <input name="availableComponentTypes" type="checkbox" ${checked} value="${sling:encode(type,'HTML_ATTR')}">
                ${sling:encode(type,'HTML')}
            </label>
        </c:forEach>
    </div>
    <input type="hidden" name="availableComponentTypes@TypeHint" value="String[]" />
</div>