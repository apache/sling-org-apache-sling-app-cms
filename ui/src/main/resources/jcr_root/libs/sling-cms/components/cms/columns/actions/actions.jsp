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
<td class="is-hidden cell-actions">
<sling:adaptTo adaptable="${slingRequest}"
    adaptTo="org.apache.sling.cms.core.models.components.AvailableActions" var="model" />
<c:forEach var="child" items="${model.children}">
        <c:choose>
            <c:when test="${child.valueMap.modal}">
                <a class="button Fetch-Modal"
                    data-title="${sling:encode(child.valueMap.title,'HTML_ATTR')}"
                    data-path="${child.valueMap.ajaxPath != null ? actionConfig.valueMap.ajaxPath : '.Main-Content form'}"
                    href="${child.valueMap.prefix}${resource.path}"
                    title="${sling:encode(child.valueMap.title,'HTML_ATTR')}">
                    <span class="jam jam-${child.valueMap.icon}">
                </span>
                </a>
            </c:when>
            <c:otherwise>
                <a class="button"
                    ${child.valueMap.new != false ? 'target="_blank"' : ''}
                    href="${child.valueMap.prefix}${resource.path}"
                    title="${sling:encode(child.valueMap.title,'HTML_ATTR')}">
                    <span class="jam jam-${child.valueMap.icon}">
                </span>
                </a>
            </c:otherwise>
        </c:choose>
    </c:forEach></td>