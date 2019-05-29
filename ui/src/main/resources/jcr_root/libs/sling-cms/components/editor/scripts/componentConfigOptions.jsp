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
<option value="">Select Component</option>
<c:forEach var="component" items="${sling:findResources(resourceResolver,'SELECT * FROM [sling:Component] WHERE [componentType] IS NOT NULL ORDER BY [jcr:title]','JCR-SQL2')}">
    <c:if test="${sling:getRelativeResource(component,'config') != null && sling:getRelativeResource(component,'config').resourceType != 'sling:Folder' && sling:getRelativeResource(component,'config').resourceType != 'sling:Component'}">
        <c:choose>
            <c:when test="${fn:startsWith(component.path,'/apps/')}">
                <c:set var="rt" value="${fn:substringAfter(component.path,'/apps/')}" />
            </c:when>
            <c:otherwise>
                <c:set var="rt" value="${fn:substringAfter(component.path,'/libs/')}" />
            </c:otherwise>
        </c:choose>
        <option value="${rt}" ${rt == editProperties.type ? 'selected' : ''} data-config="${component.path}/config.html"><sling:encode value="${component.valueMap['jcr:title']}" mode="HTML" /></option>
    </c:if>
</c:forEach>