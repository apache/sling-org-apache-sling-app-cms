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
<c:set var="user" value="${sling:adaptTo(slingRequest.requestPathInfo.suffixResource,'org.apache.sling.cms.AuthorizableWrapper').authorizable }" />
<c:choose>
    <c:when test="${user.disabled}">
        <dl>
            <dt><fmt:message key="Status"/></dt>
            <dd><fmt:message key="Disabled"/></dd>
            <dt><fmt:message key="Reason"/></dt>
            <dd><sling:encode value="${user.disabledReason}" mode="HTML" /></dd>
        </dl>
        <input type="hidden" name=":reason" value="" />
    </c:when>
    <c:otherwise>
        <dl>
            <dt><fmt:message key="Status"/></dt>
            <dd><fmt:message key="Enabled"/></dd>
            <dt><label for=":reason"><fmt:message key="Disabled Reason"/></label></dt>
            <dd><input type="text" class="input" name=":reason" value="" /></dd>
        </dl>
    </c:otherwise>
</c:choose>