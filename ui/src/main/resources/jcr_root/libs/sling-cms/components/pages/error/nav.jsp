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
<nav class="navbar has-background-light" role="navigation" aria-label="main navigation">
<div class="navbar-brand">
<a class="navbar-item" href="http://sling.apache.org" >
    <img src="${sling:encode(branding.logo,'HTML_ATTR')}" width="100" alt="${sling:encode(branding.appName,'HTML_ATTR')}"/>
</a>
<a href="/cms/start.html" class="navbar-item" title="CMS Home"><span class="icon"><em class="jam jam-home-f"></em></span></a>
</div>
<div class="navbar-menu">
</div>
</nav>