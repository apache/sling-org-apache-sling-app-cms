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
<div class="panel">
    <h4 class="panel-heading">Allowed Paths</h4>
    <div class="panel-body p-2">
        <ul>
            <c:forEach var="allowedPath" items="${properties.allowedPaths}">
                <li>
                    <pre><sling:encode value="${allowedPath}" mode="HTML" /></pre>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>
<hr/>
<div class="panel">
    <h4 class="panel-heading">
        Template Content
    </h4>
    <div class="panel-body p-2">
        <pre><sling:encode value="${properties.template}" mode="HTML" /></pre>
    </div>
</div>
<hr/>
<div class="panel">
    <h4 class="panel-heading">
        Component Policies
    </h4>
    <div class="panel-body p-2">
        <c:set var="oldAvailableTypes" value="${availableTypes}" />
        <c:set var="availableTypes" value="SlingCMS-PolicyMappingConfig" scope="request" />
        <sling:include path="policies" resourceType="sling-cms/components/general/reloadcontainer" />
        <c:set var="availableTypes" value="${oldAvailableTypes}" scope="request" />
    </div>
</div>
<hr/>
<div class="panel">
    <h4 class="panel-heading">
        Configuration Fields
    </h4>
    <div class="panel-body p-2">
        <c:set var="oldAvailableTypes" value="${availableTypes}" />
        <c:set var="availableTypes" value="SlingCMS-FieldConfig" scope="request" />
        <sling:include path="fields" resourceType="sling-cms/components/general/reloadcontainer" />
        <c:set var="availableTypes" value="${oldAvailableTypes}" scope="request" />
    </div>
</div>