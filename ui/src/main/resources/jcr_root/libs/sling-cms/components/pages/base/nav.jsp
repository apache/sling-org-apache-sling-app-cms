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
	<img src="/sling-logo.svg" alt="Apache Sling Logo"/>
</a>
<h1>
	<a href="/cms/start.html">
		CMS
	</a>
</h1>
<sling:include path="/mnt/overlay/sling-cms/content/start/jcr:content/nav" resourceType="sling-cms/components/general/container" />
<h3 class="Nav-Header Toggle-Hidden" data-target="#Tools-Nav">Tools</h3>
<ul id="Tools-Nav" class="Hide">
	<li class="Nav-Item"><a href="/bin/browser.html" title="Browse the JCR content of this site">Node Browser</a></li>
	<li class="Nav-Item"><a href="/bin/packages.html" title="Create content packages">Content Packages</a></li>
	<li class="Nav-Item"><a href="/bin/users.html/" title="Manage Users and Groups">Users &amp; Groups</a></li>
	<li class="Nav-Item"><a href="/system/console/bundles" title="Edit the OSGi bundles of this site">System Console</a></li>
</ul>
<div>
<h3 class="Nav-Header">Session</h3>
<div id="Login" style="display: block;"><a href="/system/sling/login.html" title="Login to Apache Sling">Login</a></div>
<div id="Logout" style="display: none;"><a href="/system/sling/logout" title="Logout of Apache Sling">Logout,</a> </div>
</div>