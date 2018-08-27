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
<nav class="navbar" role="navigation" aria-label="main mavigation">
<div class="navbar-brand">
<a class="navbar-item" href="http://sling.apache.org" >
    <img style="max-height:2rem" src="/static/clientlibs/sling-cms/img/sling-logo.svg" width="100" alt="Apache Sling"/>
</a>
<h1 class="navbar-item">
    <a href="/cms/start.html" title="CMS Home"><span class="icon"><i class="jam jam-home-f"></i></span></a>
</h1>
<a role="button" class="navbar-burger" aria-label="menu" aria-expanded="false">
  <span aria-hidden="true"></span>
  <span aria-hidden="true"></span>
  <span aria-hidden="true"></span>
</a>
</div>
<div class="navbar-menu">
<div class="navbar-end">
<a class="navbar-item " href="/system/sling/logout" title="Logout of Apache Sling CMS"><i class="jam jam-log-out"></i><span> ${resourceResolver.userID}</span></a>
</div>
</div>
</nav>