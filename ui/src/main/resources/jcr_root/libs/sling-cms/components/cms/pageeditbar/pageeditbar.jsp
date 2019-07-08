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
<link rel="stylesheet"
    href="/static/clientlibs/sling-cms-editor/css/editor.min.css" />
<sling:call script="/libs/sling-cms/components/editor/scripts/init.jsp" />
<div class="sling-cms-editor">
    <div class="level has-background-grey">
        <div class="level-left">
            <a href="/cms/start.html" target="_blank" title="${branding.appName}"
                class="button"> <img
                src="${branding.logo}"
                class="sling-cms-logo" />
            </a>
        </div>
        <div class="level-right">
            <div class="field has-addons">
                <sling:include path="actions"
                    resourceType="sling-cms/components/cms/pageeditbar/actions" />
            </div>
        </div>
    </div>
</div>
<sling:call
    script="/libs/sling-cms/components/editor/scripts/finalize.jsp" />