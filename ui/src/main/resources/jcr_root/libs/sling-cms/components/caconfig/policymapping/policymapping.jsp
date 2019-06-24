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
<dl>
    <dt>Path Pattern</dt>
    <dd><sling:encode value="${properties.pathPattern}" mode="HTML" /></dd>
    
    <dt>Policy</dt>
    <dd>
        <c:if test="${not empty properties.policyPath}">
            <sling:getResource var="policy" path="${properties.policyPath}" />
            <sling:encode value="${policy.valueMap['jcr:title']}" mode="HTML" />
        </c:if>
    </dd>
</dl>