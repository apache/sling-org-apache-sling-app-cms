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
<ul class="Nav">
	<li  class="Nav-Header">Sites</li>
	<sling:include path="sitenav" resourceType="sling-cms/components/cms/sitenav" />
	<li class="Nav-Item"><a href="/cms/site/create.html/content" title="Create a new Site" class="Fetch-Modal" data-title="Create Site" data-path=".Main-Content form">+ Site</a></li>
</ul>
<ul class="Nav">
	<li  class="Nav-Header">Config</li>
	<sling:include path="sitenav" resourceType="sling-cms/components/cms/confignav" />
	<li class="Nav-Item"><a href="/cms/config/create.html/etc/config" title="Create a new Config" class="Fetch-Modal" data-title="Create Site Config" data-path=".Main-Content form">+ Config</a></li>
</ul>
<ul>
	<li  class="Nav-Header">Admin</li>
	<li class="Nav-Item"><a href="/bin/browser.html" title="Browse the JCR content of this site">Node Browser</a></li>
	<li class="Nav-Item"><a href="/bin/packages.html" title="Create content packages">Content Packages</a></li>
	<li class="Nav-Item"><a href="/bin/users.html/" title="Manage Users and Groups">Users &amp; Groups</a></li>
	<li class="Nav-Item"><a href="/system/console/bundles" title="Edit the OSGi bundles of this site">System Console</a></li>
</ul>
<div>
<strong>Session</strong>
<div id="Login" style="display: block;"><a href="/system/sling/login.html" title="Login to Apache Sling">Login</a></div>
<div id="Logout" style="display: none;"><a href="/system/sling/logout" title="Logout of Apache Sling">Logout,</a> </div>
</div>