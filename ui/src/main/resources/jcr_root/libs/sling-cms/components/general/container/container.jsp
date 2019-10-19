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
    <c:when test="${not empty requestScope.availableTypes}">
        <c:set var="availableTypes" value="${requestScope.availableTypes}" />
    </c:when>
    <c:when test="${empty requestScope.availableTypes}">
        <sling:adaptTo var="policyMgr" adaptable="${resource}" adaptTo="org.apache.sling.cms.ComponentPolicyManager" />
        <c:set var="policy" value="${policyMgr.componentPolicy}" />
        <c:if test="${policy != null && not empty policy.availableComponentTypes}">
            <c:set var="availableTypes" value="${fn:join(policy.availableComponentTypes,',')}" />
        </c:if>
    </c:when>
</c:choose>
<c:forEach var="child" items="${sling:listChildren(resource)}">
    <sling:include resource="${child}" />
</c:forEach>
<c:if test="${cmsEditEnabled == 'true'}">
    <div class="sling-cms-droptarget" data-create="${sling:getResource(resourceResolver, resource.path) == null}" data-path="${resource.path}" data-order="last"></div>
    <div class="sling-cms-editor">
        <div class="level has-background-light has-text-black-ter">
            <div class="level-left">
                <div class="level-item">
                    <a href="/cms/editor/add.html${resource.path}?availableTypes=${availableTypes}" class="button action-button is-small" data-sling-cms-action="add" data-sling-cms-path="${resource.path}" data-sling-cms-available-types="${availableTypes}" title="Add Component">
                        &#43;
                    </a>
                </div>
            </div>
            <div class="level-right">
                <div class="level-item">
                    Add Components
                </div>
            </div>
        </div>
    </div>
</c:if>