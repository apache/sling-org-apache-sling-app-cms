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
<br/>
<h3><sling:encode value="${properties['jcr:title']}" mode="HTML" /> (${resource.path})</h3>
<hr/>
<div>
    <h4>
        Available Component Types
    </h4>
    <ul>
        <c:forEach var="availableComponentType" items="${properties.availableComponentTypes}">
            <li>
                <sling:encode value="${availableComponentType}" mode="HTML" />
            </li>
        </c:forEach>
    </ul>
</div>
<hr/>
<div>
    <h4>
        Component Configurations
    </h4>
    <c:set var="oldAvailableTypes" value="${availableTypes}" />
    <c:set var="availableTypes" value="SlingCMS-ComponentConfig" scope="request" />
    <sling:include path="componentConfigurations" resourceType="sling-cms/components/general/reloadcontainer" />
    <c:set var="availableTypes" value="${oldAvailableTypes}" scope="request" />
</div>
