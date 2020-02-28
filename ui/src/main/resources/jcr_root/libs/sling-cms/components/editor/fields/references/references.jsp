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
    <div class="field ${properties.toggle ? 'is-hidden toggle-value' : ''}" data-toggle-source=":operation" data-toggle-value="move">
        <div class="field">
            <label class="checkbox">
                <input type="checkbox" name="${properties.name}" value="true" />
                <sling:encode value="${properties.label}" mode="HTML" />
            </label>
        </div>
        <div class="fixed-box field">
            <table class="table" data-paginate="false">
                <thead>
                    <tr>
                        <th scope="col">Type</th>
                        <th scope="col">Content</th>
                        <th scope="col">Subpath</th>
                        <th scope="col">Property</th>
                </thead>
                <tbody>
                    <c:forEach var="ref" items="${references.references}">
                        <tr>
                            <c:choose>
                                <c:when test="${ref.page}">
                                    <td>
                                        <span class="icon" title="Page">
                                            <span class="jam jam-document"></span>
                                        </span>
                                    </td>
                                    <td title="${ref.containingPage.path}">
                                        <a href="/cms/site/content.html${ref.containingPage.parent.path}" target="_blank">
                                            ${sling:encode(ref.containingPage.title,'HTML')}
                                        </a>
                                    </td>
                                    <td>
                                        ${sling:encode(ref.subPath,'HTML')}
                                    </td>
                                    <td>
                                        ${sling:encode(ref.property,'HTML')}
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td>
                                        <span class="icon"  title="Other">
                                            <span class="jam jam-file"></span>
                                        </span>
                                    </td>
                                    <td>
                                        ${ref.resource.path}
                                    </td>
                                    <td>
                                    </td>
                                    <td>
                                        ${sling:encode(ref.property,'HTML')}
                                    </td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
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