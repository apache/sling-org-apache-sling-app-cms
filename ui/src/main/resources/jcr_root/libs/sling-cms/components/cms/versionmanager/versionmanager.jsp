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
             <form method="post" action="${sling:encode(slingRequest.requestPathInfo.suffix,'HTML_ATTR')}" enctype="multipart/form-data" class="Form-Ajax" data-add-date="false">
                <fieldset class="form-wrapper field">
                    <input type="hidden" name=":operation" value="checkpoint" />
                    <div class="Field-Group">
                        <button type="submit" class="button is-primary" title="Create a new version for the content">
                            Create Version
                        </button>
                    </div>
                </fieldset>
            </form>
         </c:when>
         <c:otherwise>
             <form method="post" action="${sling:encode(slingRequest.requestPathInfo.suffix,'HTML_ATTR')}" enctype="multipart/form-data" class="Form-Ajax" data-add-date="false">
                <fieldset class="form-wrapper field">
                    <input type="hidden" name=":autoCheckout" value="true">
                    <input type="hidden" name=":autoCheckin" value="true">
                    <input type="hidden" name="jcr:mixinTypes@TypeHint" value="Type[]">
                    <input type="hidden" name="jcr:mixinTypes" value="mix:versionable">
                    <div class="Field-Group">
                        <button type="submit" class="button is-primary" title="Make the content versionable">
                            Make Versionable
                        </button>
                    </div>
                </fieldset>
            </form>
         </c:otherwise>
     </c:choose>
     <div class="version-container">
        <div class="table-container">
            <table class="table is-fullwidth" data-sort="false" data-paginate="false">
                <thead>
                    <tr>
                        <th scope="col">Version</th>
                        <th scope="col">Created</th>
                        <th scope="col">Successors</th>
                        <th scope="col">Predecessors</th>
                        <th scope="col">Restore</th>
                    </tr>
                </thead>
                <tbody class="load-versions">
                    <c:forEach var="version" items="${sling:adaptTo(slingRequest.requestPathInfo.suffixResource,'org.apache.sling.cms.core.models.VersionInfo').versions}" varStatus="status">
                        <tr>
                            <td>
                                ${version.name}
                            </td>
                            <td>
                                <fmt:formatDate value="${version.created.time}" type="both"  dateStyle="long" timeStyle="long" />
                            </td>
                            <td>
                                <c:forEach var="successor" items="${version.successors}">
                                    ${successor.name}<br/>
                                </c:forEach>
                            </td>
                            <td>
                                <c:forEach var="predecessor" items="${version.predecessors}">
                                    ${predecessor.name}<br/>
                                </c:forEach>
                            </td>
                            <td>
                                <c:if test="${!status.first}">
                                    <form method="post" action="${sling:encode(slingRequest.requestPathInfo.suffix,'HTML_ATTR')}" enctype="multipart/form-data" class="Form-Ajax" data-add-date="false">
                                        <fieldset class="form-wrapper field">
                                            <input type="hidden" name=":operation" value="restore" />
                                            <input type="hidden" name=":version" value="${version.name}" />
                                            <div class="Field-Group">
                                                <button type="submit" class="button" title="Restore the content to ${version.name}">
                                                    Restore Version
                                                </button>
                                            </div>
                                        </fieldset>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
 </div>
 