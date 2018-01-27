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
<sling:findResources var="sites" query="SELECT * FROM [sling:Site] AS s WHERE ISDESCENDANTNODE(s,'/content') ORDER BY NAME()" language="JCR-SQL2" />
<c:forEach var="site" items="${sites}">
	<li class="Nav-Item">
		<a href="/cms/site/content.html${site.path}" title="Browse the content of a site">
			<sling:encode value="${site.valueMap['jcr:title']}" mode="HTML" />
		</a>
	</li>
</c:forEach>