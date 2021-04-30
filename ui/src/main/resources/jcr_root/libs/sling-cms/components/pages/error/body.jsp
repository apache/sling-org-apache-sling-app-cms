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
<body class="cms">
    <div class="gradient"></div>
    <div class="main-section has-background-light">
        <c:choose>
            <c:when test="${resourceResolver.userID == 'anonymous'}">
                <main class="columns is-centered is-vcentered" style="min-height: calc(100vh - 85px)">
                    <div class="column is-two-thirds-tablet is-half-desktop is-one-third-widescreen">
                        <div class="box mt-6">
                            <img src="${branding.logo}" width="100" alt="${branding.appName}" class="pb-3">
                            <sling:call script="content.jsp" />
                        </div>
                    </div>
                </main>
            </c:when>
            <c:otherwise>
                <sling:call script="nav.jsp" />
                <div class="columns">
                    <div class="column is-2 sidebar is-full-height">
                        <sling:include
                            path="/mnt/overlay/sling-cms/content/start/jcr:content/nav"
                            resourceType="sling-cms/components/general/container" />
                    </div>
                    <div class="column has-background-white-bis is-full-height">
                        <main class="Main-Content"> <sling:call
                            script="content.jsp" /> </main>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    <sling:call script="scripts.jsp" />
</body>