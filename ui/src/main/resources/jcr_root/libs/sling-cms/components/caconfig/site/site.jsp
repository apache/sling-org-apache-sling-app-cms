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

 <c:set var="cmsEditEnabled" value="true" scope="request" />
<sling:call script="/libs/sling-cms/components/editor/scripts/init.jsp" />

<sling:include path="${slingRequest.requestPathInfo.suffix}/settings" resourceType="sling-cms/components/caconfig/sitesettings" />

<sling:include path="${slingRequest.requestPathInfo.suffix}/rewrite" resourceType="sling-cms/components/caconfig/rewriter" />

<sling:call script="/libs/sling-cms/components/editor/scripts/finalize.jsp" />
<c:set var="cmsEditEnabled" value="false" scope="request" />

<sling:include path="/mnt/overlay/sling-cms/content/siteconfig/editor" resourceType="sling-cms/components/general/container" replaceSuffix="${slingRequest.requestPathInfo.suffix}" />