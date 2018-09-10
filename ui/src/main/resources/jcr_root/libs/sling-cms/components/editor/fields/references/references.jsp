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
<c:if test="${slingRequest.requestPathInfo.suffix != null}">
	<sling:getResource path="${slingRequest.requestPathInfo.suffix}" var="editedResource" />
	<c:set var="editProperties" value="${sling:adaptTo(editedResource,'org.apache.sling.api.resource.ValueMap')}" scope="request"/>
</c:if>

<sling:adaptTo var="references" adaptable="${slingRequest.requestPathInfo.suffixResource}" adaptTo="org.apache.sling.cms.References" />
<c:if test="${fn:length(references.references) gt 0}">
	<div class="box field ${properties.toggle ? 'is-hidden toggle-value' : ''}" data-toggle-source=":operation" data-toggle-value="move">
		<div class="field">
			<label class="checkbox">
				<input type="checkbox" name="${properties.name}" value="true" />
				<sling:encode value="${properties.label}" mode="HTML" />
			</label>
		</div>
		<div class="reference-list field">
			<ul>
				<c:forEach var="ref" items="${references.references}">
					<li>${ref}</li>
				</c:forEach>
			</ul>
		</div>
		
		<c:if test="${properties.includeDestination}">
			<div class="field">
				<label for=":dest">
					Replacement Path
				</label>
				<div class="control">
					<input type="text" name=":dest" class="pathfield input" />
				</div>
			</div>
		</c:if>
	</div>
</c:if>