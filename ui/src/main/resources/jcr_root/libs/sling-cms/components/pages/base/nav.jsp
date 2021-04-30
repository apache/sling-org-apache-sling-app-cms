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
<nav class="navbar has-background-light" role="navigation" aria-label="main mavigation">
    <div class="navbar-brand">
        <a class="navbar-item" href="${branding.logoLink}">
            <img src="${branding.logo}" width="100" alt="${branding.appName}"/>
        </a>
        <a href="/cms/start.html" class="navbar-item" title="CMS Home">
            <span class="icon">
                <em class="jam jam-home-f">
                    <span class="is-sr-only">Home</span>
                </em>
            </span>
        </a>
        <a role="button" class="navbar-burger" aria-label="menu" aria-expanded="false" data-target="#top-navbar-menu">
          <span aria-hidden="true"></span>
          <span aria-hidden="true"></span>
          <span aria-hidden="true"></span>
        </a>
    </div>
    <div class="navbar-menu" id="top-navbar-menu">
        <div class="navbar-end">
            <div class="navbar-item has-dropdown is-hoverable">
                <sling:adaptTo adaptable="${resourceResolver}" adaptTo="org.apache.sling.cms.AuthorizableWrapper" var="auth" />
                <sling:getResource path="${auth.authorizable.path}/profile" var="profile" />
                <span class="navbar-link">
                    <c:if test="${sling:getRelativeResource(profile,'thumbnail') != null}">
                        <img src="${profile.path}/thumbnail.transform/sling-cms-thumbnail32.png" alt="${resourceResolver.userID}" />
                    </c:if>&nbsp;
                    <sling:encode value="${profile.valueMap.name}" default="${resourceResolver.userID}" mode="HTML" />
                </span>
                <div class="navbar-dropdown">
                    <a class="navbar-item Fetch-Modal" data-title="User Profile" data-path=".Main-Content form" href="/cms/auth/user/profile.html${auth.authorizable.path}">
                        <em class="jam jam-id-card">
                            <span class="is-sr-only">Profile</span>
                        </em>&nbsp;
                        Profile
                    </a>
                    <a class="navbar-item" href="${branding.helpLink}" target="_blank" rel="noopener noreferrer">
                        <em class="jam jam-help">
                            <span class="is-sr-only">Help</span>
                        </em>&nbsp;
                        Help
                    </a>
                    <hr class="navbar-divider">
                    <a class="navbar-item" href="/system/sling/logout">
                        <em class="jam jam-log-out">
                            <span class="is-sr-only">Logout</span>
                        </em>&nbsp;
                        Logout
                    </a>
                </div>
            </div>
        </div>
    </div>
</nav>