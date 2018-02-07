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
<sling:getParent resource="${slingRequest.requestPathInfo.suffixResource}" var="site" level="2" />
<div class="Pull-Right">
	<a class="Fetch-Modal Button" data-title="Edit Site" data-path=".Main-Content form" href="/cms/site/edit.html${site.path}" title="Edit Site">&#x270f;</a>
	<a class="Fetch-Modal Button" data-title="Move / Copy Site" data-path=".Main-Content form" href="/cms/shared/movecopy.html${site.path}" title="Delete Site">&#x21c6;</a>
	<a class="Fetch-Modal Button" data-title="Delete Site" data-path=".Main-Content form" href="/cms/shared/delete.html${site.path}" title="Delete Site">&times;</a>
</div>
<h2>
	<sling:encode value="${site.valueMap['jcr:title']}" mode="HTML" />
</h2>
<p>
	<sling:encode value="${site.valueMap['jcr:description']}" mode="HTML" />
</p>
<hr/>