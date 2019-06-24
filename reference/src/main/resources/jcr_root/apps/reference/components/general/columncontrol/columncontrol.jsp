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
    <c:when test="${properties.container == true}">
        <sling:adaptTo adaptable="${resource}" adaptTo="org.apache.sling.cms.ComponentPolicyManager" var="componentPolicyMgr" />
        <c:set var="configRsrc" value="${componentPolicyMgr.componentPolicy.componentConfigs['reference/components/general/columncontrol']}" />
        <div class="${configRsrc.valueMap.containerclass}">
            <div class="row">
                <c:forEach var="col" items="${fn:split(properties.layout,',')}" varStatus="status">
                    <div class="${sling:encode(col,'HTML_ATTR')}">
                        <sling:include path="col-${status.index}" resourceType="sling-cms/components/general/container" />
                    </div>
                </c:forEach>
            </div>
         </div>
     </c:when>
     <c:otherwise>
         <div class="row">
            <c:forEach var="col" items="${fn:split(properties.layout,',')}" varStatus="status">
                <div class="${sling:encode(col,'HTML_ATTR')}">
                    <sling:include path="col-${status.index}" resourceType="sling-cms/components/general/container" />
                </div>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>