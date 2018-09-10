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
 <%@page import="org.apache.sling.cms.core.models.components.Breadcrumbs"%>
 <%@include file="/libs/sling-cms/global.jsp"%>
 <sling:adaptTo adaptable="${slingRequest}" adaptTo="org.apache.sling.cms.core.models.components.ContentTable" var="model"/>
 <div class="container is-fullwidth">
<table class="table is-fullwidth is-striped">
    <thead>
        <tr>
            <c:forEach var="column" items="${model.columnData}">
                <th class="${column.classString}" data-attribute="${column.name}">
                    <sling:encode value="${column.title}" mode="HTML" />
                </th>
            </c:forEach>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="child" items="${model.children}">
                <tr class="sortable__row" data-resource="${child.path}" data-type="${child.dataType}">
                    <c:forEach var="column" items="${model.columnData}">
                            <c:set var="colConfig" value="${column.resource}" scope="request" />
                            <sling:include path="${child.path}" resourceType="${column.fieldResourceType}" />
                    </c:forEach>
                </tr>
        </c:forEach> 
    </tbody>
</table>
</div>