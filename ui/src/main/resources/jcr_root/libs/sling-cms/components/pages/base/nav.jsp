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
<a class="Cell-Pad" href="http://sling.apache.org" target="_blank" title="Visit the Apache Sling website">
	<img src="/etc/clientlibs/sling-cms/img/sling-logo.svg" alt="Apache Sling Logo"/>
</a>
<h1>
	<a href="/cms/start.html">
		CMS
	</a>
</h1>
<sling:include path="/mnt/overlay/sling-cms/content/start/jcr:content/nav" resourceType="sling-cms/components/general/container" />
<div>
<h3 class="Nav-Header">Session</h3>
<div><a href="/system/sling/logout" title="Logout of Apache Sling">Logout, ${resourceResolver.userID}</a> </div>
</div>