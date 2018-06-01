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
 <div class="versionmanager">
 	<c:set var="versionable" value="false" />
 	<c:if test="${slingRequest.requestPathInfo.suffixResource.valueMap['jcr:mixinTypes'] != null && fn:contains(fn:join(slingRequest.requestPathInfo.suffixResource.valueMap['jcr:mixinTypes'],','),'mix:versionable')}">
 		<c:set var="versionable" value="true" />
 	</c:if>
 	<c:choose>
 		<c:when test="${versionable == 'true'}">
 			<form method="post" action="${slingRequest.requestPathInfo.suffix}" enctype="multipart/form-data" class="Form-Ajax" data-add-date="false">
				<input type="hidden" name=":operation" value="checkpoint" />
				<div class="Field-Group">
					<button type="submit" class="btn btn-success" title="Create a new version for the content">
						Create Version
					</button>
				</div>
			</form>
 		</c:when>
 		<c:otherwise>
 			<form method="post" action="${slingRequest.requestPathInfo.suffix}" enctype="multipart/form-data" class="Form-Ajax" data-add-date="false">
				<input type="hidden" name=":autoCheckout" value="true">
				<input type="hidden" name=":autoCheckin" value="true">
				<input type="hidden" name="jcr:mixinTypes@TypeHint" value="Type[]">
			    <input type="hidden" name="jcr:mixinTypes" value="mix:versionable">
				<div class="Field-Group">
					<button type="submit" class="btn btn-success" title="Make the content versionable">
						Make Versionable
					</button>
				</div>
			</form>
 		</c:otherwise>
 	</c:choose>
	<table>
		<thead>
			<tr>
				<th>Version</th>
				<th>Created</th>
				<th>Successors</th>
				<th>Predecessors</th>
				<th>Restore</th>
			</tr>
		</thead>
		<tbody class="fetch-json" data-url="${resource.path}.VI.json${slingRequest.requestPathInfo.suffix}" data-template="version-template">
			
		</tbody>
	</table>
	<script id="version-template" type="text/x-handlebars-template">
		{{#each versions }}
			<tr>
				<td>
					{{@key}}
				</td>
				<td>
					{{created}}
				</td>
				<td>
					{{#each successors }}
						{{this}}<br/>
					{{/each}}
				</td>
				<td>
					{{#each predecessors }}
						{{this}}<br/>
					{{/each}}
				</td>
				<td>
 					<form method="post" action="${slingRequest.requestPathInfo.suffix}" enctype="multipart/form-data" class="Form-Ajax" data-add-date="false">
						<input type="hidden" name=":operation" value="restore" />
						<input type="hidden" name=":version" value="{{@key}}" />
						<div class="Field-Group">
							<button type="submit" class="btn btn-success" title="Restore the content to {{@key}}">
								Restore Version
							</button>
						</div>
					</form>
				</td>
			</tr>
		{{/each}}
	</script>
 </div>
 