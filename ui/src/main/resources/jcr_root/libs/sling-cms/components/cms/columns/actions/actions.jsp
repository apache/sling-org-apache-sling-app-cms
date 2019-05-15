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
<td class="is-vhidden cell-actions">
    <c:forEach var="actionConfig" items="${sling:listChildren(colConfig)}">
        <c:choose>
            <c:when test="${actionConfig.valueMap.modal}">
                <a class="button Fetch-Modal" data-title="${sling:encode(actionConfig.valueMap.title,'HTML_ATTR')}" data-path="${actionConfig.valueMap.ajaxPath != null ? actionConfig.valueMap.ajaxPath : '.Main-Content form'}" href="${actionConfig.valueMap.prefix}${resource.path}" title="${sling:encode(actionConfig.valueMap.title,'HTML_ATTR')}">
                    <span class="jam jam-${actionConfig.valueMap.icon}">
                        <span class="is-vhidden">
                            ${sling:encode(actionConfig.valueMap.title,'HTML')}
                        </span>
                    </span>
                </a>
            </c:when>
            <c:otherwise>
                <a class="button" ${actionConfig.valueMap.new != false ? 'target="_blank"' : ''} href="${actionConfig.valueMap.prefix}${resource.path}" title="${sling:encode(actionConfig.valueMap.title,'HTML_ATTR')}">
                    <span class="jam jam-${actionConfig.valueMap.icon}">
                        <span class="is-vhidden">
                            ${sling:encode(actionConfig.valueMap.title,'HTML')}
                        </span>
                    </span>
                </a>
            </c:otherwise>
        </c:choose>
    </c:forEach>
</td>