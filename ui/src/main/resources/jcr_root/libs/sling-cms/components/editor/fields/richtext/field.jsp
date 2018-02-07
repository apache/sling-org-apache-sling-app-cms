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
<textarea class="richtext" name="${properties.name}" ${required} ${disabled}>${editProperties[properties.name]}</textarea>
<c:set var="pageQuery" value="SELECT * FROM [sling:Page] AS s WHERE ISDESCENDANTNODE([${sling:getAbsoluteParent(slingRequest.requestPathInfo.suffixResource,2).path}])" />
<c:set var="imageQuery" value="SELECT * FROM [sling:File] AS s WHERE ISDESCENDANTNODE([${sling:getAbsoluteParent(slingRequest.requestPathInfo.suffixResource,2).path}]) AND [jcr:content/jcr:mimeType] LIKE 'image/%'" />
<datalist id="richtext-pages">
	<c:forEach var="page" items="${sling:findResources(resourceResolver,pageQuery,'JCR-SQL2')}">
		<option value="${page.path}.html">
			<sling:encode value="${page.valueMap['jcr:content/jcr:title']}" mode="HTML" />
		</option>
	</c:forEach>
</datalist>
<datalist id="richtext-images">
	<c:forEach var="image" items="${sling:findResources(resourceResolver,imageQuery,'JCR-SQL2')}">
		<option value="${image.path}">
			<sling:encode value="${image.valueMap['jcr:content/jcr:title']}" mode="HTML" default="${image.name }" />
		</option>
	</c:forEach>
</datalist>