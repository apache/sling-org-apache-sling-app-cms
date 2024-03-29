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
<option value=""><fmt:message key="Select Job" /></option>
<sling:adaptTo var="jobManager" adaptable="${slingRequest}" adaptTo="org.apache.sling.cms.CMSJobManager" />
<c:forEach var="job" items="${jobManager.availableJobs}">
    <option value="${sling:encode(job.configurationPath,'HTML_ATTR')}">
        <fmt:message key="${job.titleKey}" var="title" />
        <sling:encode value="${title}" mode="HTML" />
    </option>
</c:forEach>