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
<nav class="breadcrumb" aria-label="breadcrumbs">
    <c:set var="siteCfgResource" value="${slingRequest.requestPathInfo.suffixResource.parent.parent}" />
    <c:set var="cfgResource" value="${siteCfgResource.parent}" />
    <ul>
        <li>
            <a href="/cms/config/list.html${cfgResource.path}">
                <sling:encode value="${cfgResource.valueMap['jcr:content/jcr:title']}" mode="HTML" default="${cfgResource.name}" />
            </a>
        </li>
        <li>
            <a href="/cms/config/edit.html${siteCfgResource.path}">
                <sling:encode value="${siteCfgResource.valueMap['jcr:title']}" mode="HTML" default="${siteCfgResource.name}" />
            </a>
        </li>
        <li class="is-active">
            <a href="#">
                <sling:encode value="${resource.valueMap['jcr:title']}" mode="HTML" />
            </a>
        </li>
    </ul>
</nav>
 <c:set var="cmsEditEnabled" value="true" scope="request" />
<sling:call script="/libs/sling-cms/components/editor/scripts/init.jsp" />
<sling:include path="${slingRequest.requestPathInfo.suffix}" resourceType="sling-cms/components/cms/templateeditor/config" />
<sling:call script="/libs/sling-cms/components/editor/scripts/finalize.jsp" />
<c:set var="cmsEditEnabled" value="false" scope="request" />