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
<c:set var="searchConfig" value="${sling:adaptTo(resource,'org.apache.sling.cms.ComponentConfiguration').properties}" scope="request" />
<form action="${searchConfig.searchPage}.html" method="GET" class="${sling:encode(searchConfig.formClass,'HTML_ATTR')}">
    <fmt:message key="Search" var="searchMessage" />
    <input type="text" name="q" class="${sling:encode(searchConfig.inputClass,'HTML_ATTR')}" placeholder="${sling:encode(searchMessage,'HTML_ATTR')}" />
    <input type="submit" class="${searchConfig.buttonClass}" value="${sling:encode(searchMessage,'HTML_ATTR')}" />
</form>