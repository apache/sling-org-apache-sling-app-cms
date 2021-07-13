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
<option value="">
    <fmt:message key="-- Default --" />
</option>
<option disabled></option>
<option value="sling-cms/components/general/container" ${slingRequest.requestPathInfo.suffixResource.valueMap.overrideType == 'sling-cms/components/general/container' ? 'selected' : ''}>
    <fmt:message key="Container" />
</option>
<option disabled></option>
<sling:adaptTo adaptable="${slingRequest.requestPathInfo.suffixResource}" adaptTo="org.apache.sling.cms.ComponentPolicyManager" var="componentPolicyMgr" />
<c:set var="configRsrc" value="${componentPolicyMgr.componentPolicy.componentConfigs['reference/components/general/reference']}" />
<c:set var="join" value="' OR [componentType]='" />
<c:set var="query" value="SELECT * FROM [sling:Component] WHERE [componentType] IS NOT NULL AND ([componentType]='${fn:join(configRsrc.valueMap.availableComponentTypes,join)}') ORDER BY [jcr:title]" />
<c:forEach var="component" items="${sling:findResources(resourceResolver,query,'JCR-SQL2')}">
    <c:choose>
        <c:when test="${fn:startsWith(component.path,'/libs/')}">
            <c:set var="type" value="${fn:substringAfter(component.path, '/libs/')}" />
        </c:when>
        <c:otherwise>
            <c:set var="type" value="${fn:substringAfter(component.path, '/apps/')}" />
        </c:otherwise>
    </c:choose>
    <option ${slingRequest.requestPathInfo.suffixResource.valueMap.overrideType == type ? 'selected' : ''} value="${type}">
        ${sling:encode(component.valueMap['jcr:title'],'HTML')}
    </option>
</c:forEach>