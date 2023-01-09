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
<sling:adaptTo var="references" adaptable="${slingRequest.requestPathInfo.suffixResource}" adaptTo="org.apache.sling.cms.References" />
<table class="table is-fullwidth">
    <thead>
        <tr>
            <th scope="col"><fmt:message key="Type" /></th>
            <th scope="col"><fmt:message key="Content" /></th>
            <th scope="col"><fmt:message key="Subpath" /></th>
            <th scope="col"><fmt:message key="Property" /></th>
        </tr>
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
                        <td title="${sling:encode(ref.containingPage.path,'HTML_ATTR')}">
                            <a href="/cms/site/content.html${sling:encode(ref.containingPage.parent.path,'HTML_ATTR')}?resource=${sling:encode(ref.containingPage.path,'HTML_ATTR')}" target="_blank">
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
                            <span class="icon" title="Other">
                                <span class="jam jam-file"></span>
                            </span>
                        </td>
                        <td>
                            ${sling:encode(ref.resource.path,'HTML')}
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