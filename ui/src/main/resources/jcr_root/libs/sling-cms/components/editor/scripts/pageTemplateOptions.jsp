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
<option value="">Select Page Type</option>
<sling:adaptTo var="templateMgr" adaptable="${slingRequest.requestPathInfo.suffixResource}" adaptTo="org.apache.sling.cms.PageTemplateManager" />
<c:forEach var="template" items="${templateMgr.availableTemplates}">
    <option value="${template.resource.path}">
        <sling:encode value="${template.title}" mode="HTML" />
    </option>
</c:forEach>