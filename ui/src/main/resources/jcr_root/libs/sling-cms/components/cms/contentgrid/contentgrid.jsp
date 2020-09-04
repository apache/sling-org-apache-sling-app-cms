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
<div class="reload-container scroll-container contentnav" data-path="${resource.path}.grid.html${sling:encode(slingRequest.requestPathInfo.suffix,'HTML_ATTR')}">
    <div class="tile is-ancestor">
        <c:forEach var="child" items="${sling:listChildren(slingRequest.requestPathInfo.suffixResource)}" varStatus="status">
            <c:set var="showCard" value="${false}" />
            <c:forEach var="type" items="${sling:listChildren(sling:getRelativeResource(resource,'types'))}">
                <c:if test="${child.valueMap['jcr:primaryType'] == type.name}">
                    <c:set var="showCard" value="${true}" />
                </c:if>
            </c:forEach>
            <c:if test="${showCard}">
                <div class="tile is-parent is-3 contentnav__item">
                    <sling:getResource base="${resource}" path="types/${child.valueMap['jcr:primaryType']}/columns/name" var="nameConfig" />
                    <c:choose>
                        <c:when test="${not empty child.valueMap['jcr:content/jcr:title']}">
                            <c:set var="title" value="${child.valueMap['jcr:content/jcr:title']}" />
                        </c:when>
                        <c:when test="${not empty child.valueMap['jcr:title']}">
                            <c:set var="title" value="${child.valueMap['jcr:title']}" />
                        </c:when>
                        <c:otherwise>
                            <c:set var="title" value="${child.name}" />
                        </c:otherwise>
                    </c:choose>
                    <div class="tile is-child">
                        <div class="card is-linked" title="${sling:encode(child.name,'HTML_ATTR')}" data-value="${child.path}">
                            <div class="card-image">
                                <figure class="image is-5by4">
                                    <c:choose>
                                        <c:when test="${child.resourceType == 'sling:File' || child.resourceType == 'nt:file'}">
                                            <img src="${child.path}.transform/sling-cms-thumbnail.png" loading="lazy" alt="${child.name}">
                                        </c:when>
                                        <c:when test="${child.resourceType == 'sling:Site'}">
                                            <img src="/static/sling-cms/thumbnails/site.png.transform/sling-cms-thumbnail.png" loading="lazy" alt="${sling:encode(child.name, 'HTML_ATTR')}">
                                        </c:when>
                                        <c:when test="${child.resourceType == 'sling:OrderedFolder' || child.resourceType == 'sling:Folder' || child.resourceType == 'nt:folder'}">
                                            <img src="/static/sling-cms/thumbnails/folder.png.transform/sling-cms-thumbnail.png" loading="lazy" alt="${sling:encode(child.name, 'HTML_ATTR')}">
                                        </c:when>
                                        <c:when test="${child.resourceType == 'sling:Page'}">
                                            <c:set var="templateThumbnail" value="${child.valueMap['jcr:content/sling:template']}/thumbnail"/>
                                            <c:choose>
                                                <c:when test="${sling:getResource(resourceResolver, templateThumbnail) != null}">
                                                    <img src="${templateThumbnail}.transform/sling-cms-thumbnail.png" loading="lazy" alt="${sling:encode(child.name, 'HTML_ATTR')}">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="/static/sling-cms/thumbnails/page.png.transform/sling-cms-thumbnail.png" loading="lazy" alt="${child.name}">
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <img src="/static/sling-cms/thumbnails/file.png.transform/sling-cms-thumbnail.png" loading="lazy" alt="${child.name}">
                                        </c:otherwise>
                                    </c:choose>
                                </figure>
                                <div class="is-vhidden cell-actions">
                                    <sling:getResource base="${resource}" path="types/${child.valueMap['jcr:primaryType']}/columns/actions" var="colConfig" />
                                    <c:forEach var="ac" items="${sling:listChildren(colConfig)}">
                                        <c:set var="actionConfig" value="${ac}" scope="request" />
                                        <sling:include path="${child.path}" resourceType="${actionConfig.resourceType}" />
                                    </c:forEach>
                                </div>
                            </div>
                            <footer class="card-footer">
                                <div class="card-footer-item card-footer-group">
                                    <span>${sling:encode(title,'HTML')}</span>
                                    <c:catch var="ex">
                                        <fmt:formatDate type="both" dateStyle="long" timeStyle="long" value = "${child.valueMap['jcr:content/jcr:lastModified'].time}" var="lastMod" />
                                        <small>${lastMod}</small>
                                    </c:catch>
                                </div>
                            </footer>
                            <footer class="card-footer">
                                <sling:adaptTo adaptable="${resourceResolver}" adaptTo="org.apache.sling.cms.publication.PublicationManager" var="publicationManager" />
                                <sling:adaptTo adaptable="${child}" adaptTo="org.apache.sling.cms.PublishableResource" var="publishableResource" />
                                <c:if test="${child.resourceType == 'sling:Page' || child.resourceType == 'sling:File' || child.resourceType == 'nt:file'}">
                                    <c:choose>
                                        <c:when test="${publishableResource.published && publicationManager.publicationMode == 'CONTENT_DISTRIBUTION'}">
                                            <a class="Fetch-Modal card-footer-item" href="/cms/shared/publish.html${child.path}" title="Content Published" data-title="Unpublish" data-path=".Main-Content form">
                                                Republish
                                            </a>
                                            <a class="Fetch-Modal card-footer-item" href="/cms/shared/unpublish.html${child.path}" title="Content Published" data-title="Unpublish" data-path=".Main-Content form">
                                                Unpublish
                                            </a>
                                        </c:when>
                                        <c:when test="${publishableResource.published}">
                                            <a class="Fetch-Modal card-footer-item" href="/cms/shared/unpublish.html${child.path}" title="Content Published" data-title="Unpublish" data-path=".Main-Content form">
                                                Unpublish
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <a class="Fetch-Modal card-footer-item" href="/cms/shared/publish.html${child.path}" title="Content Not Published" data-title="Publish" data-path=".Main-Content form">
                                                Publish
                                            </a>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                                    <c:if test="${child.resourceType == 'sling:Site' || child.resourceType == 'sling:OrderedFolder' || child.resourceType == 'sling:Folder' || child.resourceType == 'nt:folder' || child.resourceType == 'sling:Page'}">
                                    <a href="${nameConfig.valueMap.prefix}${child.path}" class="card-footer-item item-link">Open</a>
                                </c:if>
                            </footer>
                        </div>
                    </div>
                </div> 
            </c:if>
        </c:forEach>
    </div>
</div>
