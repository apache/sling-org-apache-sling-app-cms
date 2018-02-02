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

<c:forEach var="language" items="${sling:listChildren(slingRequest.requestPathInfo.suffixResource)}">
	<c:if test="${firstChild == null && not empty language.valueMap['jcr:language']}">
		<c:set var="firstChild" value="${language}" />
	</c:if>
</c:forEach>
<a class="Button Fetch-Modal" data-title="Add Entry" data-path=".Main-Content form" href="/cms/i18n/entry/create.html${firstChild.path}">+ Entry</a>
<form method="post" action="${slingRequest.requestPathInfo.suffix}" enctype="multipart/form-data" class="Form-Ajax" data-add-date="false">
	<table>
		<thead>
			<tr>
				<th class="Column-key">
					Key
				</th>
				<c:forEach var="language" items="${sling:listChildren(slingRequest.requestPathInfo.suffixResource)}">
					<c:if test="${not empty language.valueMap['jcr:language']}">
						<th class="Column-${language.valueMap['jcr:language']}">
							<sling:encode value="${language.valueMap['jcr:content/jcr:title']}" mode="HTML" />
							<br/>
							<small>(<sling:encode value="${language.valueMap['jcr:language']}" mode="HTML" />)</small>
						</th>
					</c:if>
				</c:forEach>
			</tr>
		</thead>
		<tbody>
			<sling:adaptTo var="helper" adaptable="${slingRequest.requestPathInfo.suffixResource}" adaptTo="org.apache.sling.cms.core.models.i18nHelper" />
			<c:forEach var="key" items="${helper.keys}">
				<tr>
					<td>
						<sling:encode value="${key}" mode="HTML" />
					</td>
					<c:forEach var="language" items="${sling:listChildren(slingRequest.requestPathInfo.suffixResource)}">
						<c:if test="${not empty language.valueMap['jcr:language']}">
							<td>
								<c:set var="keyfound" value="false" />
								<c:forEach var="entry" items="${sling:listChildren(language)}">
									<c:if test="${entry.valueMap['sling:key'] == key}">
										<c:set var="keyfound" value="true" />
										<input name="${language.name}/${entry.name}/sling:message" type="text" value="${entry.valueMap['sling:message']}" />
										<input name="${language.name}/${entry.name}/sling:key" type="hidden" value="${key}" />
									</c:if>
								</c:forEach>
								<c:if test="${keyfound == 'false'}">
									<c:set var="rand" value="${helper.random}" />
									<input name="${language.name}/entry_${rand}/sling:message" type="text" value="" />
									<input name="${language.name}/entry_${rand}/sling:key" type="hidden" value="${key}" />
									<input name="${language.name}/entry_${rand}/jcr:primaryType" type="hidden" value="sling:MessageEntry" />
								</c:if>
							</td>
						</c:if>
					</c:forEach>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<button>Save i18n Dictionary</button>
</form>