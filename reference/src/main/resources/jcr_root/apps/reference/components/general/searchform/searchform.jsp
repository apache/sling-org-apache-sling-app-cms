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
<sling:adaptTo var="pageMgr" adaptable="${resource}" adaptTo="org.apache.sling.cms.api.PageManager" />
<c:set var="searchConfig" value="${pageMgr.page.template.componentConfigs['reference/components/general/searchform']}" scope="request" />
<form action="${pageMgr.page.path}.html" method="GET" class="${searchConfig.valueMap.formClass}">
	<input type="text" name="q" class="${searchConfig.valueMap.inputClass}" placeholder="<fmt:message key="slingcms.search"/>" />
	<input type="submit" class="${searchConfig.valueMap.buttonClass}" value="<fmt:message key="slingcms.search"/>" />
</form>