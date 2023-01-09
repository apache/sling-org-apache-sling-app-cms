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
<c:if test="${cmsEditEnabled == 'true'}">
    <h3><fmt:message key="Create User Generated Content" /></h3>
    <dl>
        <dt><fmt:message key="Action" /></dt>
        <dd>${sling:encode(properties.approveAction,'HTML')}</dd>
        <dt><fmt:message key="Additional Properties" /></dt>
        <dd>
            <c:if test="${not empty properties.additionalProperties}">
                ${sling:encode(fn:join(properties.additionalProperties,', '),'HTML')}
            </c:if>
        </dd>
        <dt><fmt:message key="Bucket" /></dt>
        <dd>${sling:encode(properties.bucket,'HTML')}</dd>
        <dt><fmt:message key="Content Type" /></dt>
        <dd>${sling:encode(properties.contentType,'HTML')}</dd>
        <dt><fmt:message key="Name" /></dt>
        <dd>${sling:encode(properties.name,'HTML')}</dd>
        <dt><fmt:message key="Path Depth" /></dt>
        <dd>${sling:encode(properties.pathDepth,'HTML')}</dd>
        <dt><fmt:message key="Preview" /></dt>
        <dd>${sling:encode(properties.preview,'HTML')}</dd>
        <dt><fmt:message key="Target Path" /></dt>
        <dd>${sling:encode(properties.targetPath,'HTML')}</dd>
        <dt><fmt:message key="Wrap in Page?" /></dt>
        <dd>${sling:encode(properties.wrapPage,'HTML')}</dd>
    </dl>
</c:if>