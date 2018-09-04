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
<c:choose>
	<c:when test="${properties.accepts}">
		<c:set var="accepts" value="${properties.accepts}" />
	</c:when>
	<c:otherwise>
		<c:set var="accepts" value="image/*,audio/*,video/*,application/json,text/css,application/pdf" />
	</c:otherwise>
</c:choose>
<div class="repeating">
 	<div disabled="disabled" class="repeating__template is-hidden">
 		<div class="repeating__item Grid">
            <div class="file has-name is-fullwidth">
                <label class="file-label">
                   <input type="file" class="file-input" name="${properties.name}" ${required} accept="${accepts}" />
                   <span class="file-cta">
                      <span class="file-icon">
                        <i class="jam jam-upload"></i>
                      </span>
                      <span class="file-label">
                        Browse
                      </span>
                   </span>
                   <span class="file-name">
                        No File Selected
                    </span>
                    <span class='control'>
                        <span class="button repeating__remove button">-</span>
                    </span>
                </label>
            </div>
	 	</div>
 	</div>
 	<div class="repeating__container">
	 	<div class="repeating__item Grid">
 			<div class="file has-name is-fullwidth">
 			    <label class="file-label">
	 			   <input type="file" class="file-input" name="${properties.name}" ${required} accept="${accepts}" />
	 			   <span class="file-cta">
				      <span class="file-icon">
				        <i class="jam jam-upload"></i>
				      </span>
				      <span class="file-label">
				        Browse
				      </span>
                   </span>
				   <span class="file-name">
                        No File Selected
                    </span>
	                    <span class="button repeating__remove button">-</span>
	 			</label>
	 		</div>
	 	</div>
	</div>
	<button class="repeating__add button">+</button>
</div>