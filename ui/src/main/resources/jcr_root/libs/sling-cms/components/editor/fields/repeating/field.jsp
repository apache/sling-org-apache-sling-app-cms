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
 <div class="repeating">
 	<fieldset disabled="disabled" class="repeating__template Hide">
 		<div class="repeating__item Grid">
 			<div class="Cell Mobile-80">
	 			<input type="${properties.type}" value="" name="${properties.name}" ${required} ${disabled} />
	 		</div>
	 		<div class="Cell Mobile-20">
		 		<button class="repeating__remove">-</button>
		 	</div>
	 	</div>
 	</fieldset>
 	<div class="repeating__container">
	 	<c:forEach var="value" items="${editProperties[properties.name]}">
	 		<div class="repeating__item Grid">
	 			<div class="Cell Mobile-80">
		 			<input type="${properties.type}" value="${value}" name="${properties.name}" ${required} ${disabled} />
		 		</div>
		 		<div class="Cell Mobile-20">
			 		<button class="repeating__remove">-</button>
			 	</div>
		 	</div>
	 	</c:forEach>
 	</div>
 	<button class="repeating__add">+</button>
 </div>