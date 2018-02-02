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
<link rel="stylesheet" href="/etc/clientlibs/sling-cms-editor/editor.css" />
<sling:call script="/libs/sling-cms/components/editor/scripts/init.jsp" />
<div class="Sling-CMS__edit-bar">
	<a href="/cms/start.html" target="_blank" class="Sling-CMS__component-title" target="Sling CMS">
		<img src="/etc/clientlibs/sling-cms/img/sling-logo.svg" class="Sling-CMS__logo" />
	</a>
</div>
<sling:call script="/libs/sling-cms/components/editor/scripts/finalize.jsp" />