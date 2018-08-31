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
<c:if test="${cmsEditEnabled == 'true'}">
	<script src="/static/clientlibs/sling-cms-editor/js/editor.min.js"></script>
	<div class="sling-cms-editor">
		<div class="modal">
			<div class="modal-background"></div>
			<div class="modal-card">
				<div class="modal-card">
					<header class="modal-card-head">
						<p class="modal-card-title"></p>
						<button class="button delete is-small" aria-label="close">
							<span class="jam jam-close"></span>
						</button>
					</header>
					<section class="modal-card-body">
					</section>
				</div>
			</div>
		</div>
	</div>
</c:if>