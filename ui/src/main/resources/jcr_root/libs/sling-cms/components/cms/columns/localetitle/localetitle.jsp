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
<sling:adaptTo adaptable="${resource}" adaptTo="org.apache.sling.cms.core.models.LocaleResource" var="localeResource" />
<td title="${sling:encode(localeResource.locale.displayLanguage,'HTML_ATTR')} ${sling:encode(localeResource.locale.displayCountry,'HTML_ATTR')}" data-value="${localeResource.locale}">
	${localeResource.locale.displayLanguage} ${localeResource.locale.displayCountry}
</td>