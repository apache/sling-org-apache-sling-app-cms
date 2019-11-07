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
<c:if test="${sling:getRelativeResource(resource, 'thumbnail') != null}">
    <img src="${resource.path}/thumbnail.transform/sling-cms-thumbnail128.png" alt="Thumbnail" class="image is-128x128" />
</c:if>
<br/>
<div>
    <h4>Allowed Paths</h4>
    <ul>
        <c:forEach var="allowedPath" items="${properties.allowedPaths}">
            <li>
                <pre><sling:encode value="${allowedPath}" mode="HTML" /></pre>
            </li>
        </c:forEach>
    </ul>
</div>
<hr/>
<div>
    <h4>
        Component Policies
    </h4>
    <c:set var="oldAvailableTypes" value="${availableTypes}" />
    <c:set var="availableTypes" value="SlingCMS-PolicyMappingConfig" scope="request" />
    <sling:include path="policies" resourceType="sling-cms/components/general/reloadcontainer" />
    <c:set var="availableTypes" value="${oldAvailableTypes}" scope="request" />
</div>
<hr/>
<div>
    <h4>
        Template Content
    </h4>
    <pre><sling:encode value="${properties.template}" mode="HTML" /></pre>
</div>
<hr/>
<div>
    <h4>
        Configuration Fields
    </h4>
    <c:set var="oldAvailableTypes" value="${availableTypes}" />
    <c:set var="availableTypes" value="SlingCMS-FieldConfig" scope="request" />
    <sling:include path="fields" resourceType="sling-cms/components/general/reloadcontainer" />
    <c:set var="availableTypes" value="${oldAvailableTypes}" scope="request" />
</div>