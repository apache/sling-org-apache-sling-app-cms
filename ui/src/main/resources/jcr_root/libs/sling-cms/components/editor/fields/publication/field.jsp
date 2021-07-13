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
<sling:adaptTo adaptable="${slingRequest.requestPathInfo.suffixResource.parent}" adaptTo="org.apache.sling.cms.PublishableResource" var="publishableResource" />

<div class="columns">
    <div class="column">
        <dl>
            <dt>
                <fmt:message key="Published" />
            </dt>
            <dd>
                <sling:encode value="${publishableResource.published}" mode="HTML" />
            </dd>
            <c:if test="${publishableResource.lastPublication.time != null}">
                <dt>
                    <fmt:message key="Last Publication" />
                </dt>
                <dd>
                    <fmt:formatDate value="${publishableResource.lastPublication.time}" type="both" />
                </dd>
            </c:if>
            <c:if test="${not empty publishableResource.lastPublicationBy}">
                <dt>
                    <fmt:message key="Last Publication By" />
                </dt>
                <dd>
                    <sling:encode value="${publishableResource.lastPublicationBy}" mode="HTML" />
                </dd>
            </c:if>
            <c:if test="${not empty publishableResource.lastPublicationType}">
                <dt>
                    <fmt:message key="Last Publication Type" />
                </dt>
                <dd>
                    <sling:encode value="${publishableResource.lastPublicationType}" mode="HTML" />
                </dd>
            </c:if>
        </dl>
    </div>
    <div class="column">
        <sling:adaptTo adaptable="${resourceResolver}" adaptTo="org.apache.sling.cms.publication.PublicationManager" var="publicationManager" />
        <fmt:message key="Publish" var="publishMessage" />
        <fmt:message key="Republish" var="republishMessage" />
        <fmt:message key="Unpublish" var="unpublishMessage" />
        <fmt:message key="Content Published" var="contentPublishedMessage" />
        <fmt:message key="Content Not Published" var="contentNotPublishedMessage" />
        <c:choose>
            <c:when test="${publishableResource.published && publicationManager.publicationMode == 'CONTENT_DISTRIBUTION'}">
                <a class="Fetch-Modal button is-success is-outlined" href="/cms/shared/publish.html${sling:encode(slingRequest.requestPathInfo.suffixResource.parent.path,'HTML_ATTR')}" title="${contentPublishedMessage}" data-title="${republishMessage}" data-path=".Main-Content form">
                    ${republishMessage}
                </a>
                <a class="Fetch-Modal button is-success is-outlined" href="/cms/shared/unpublish.html${sling:encode(slingRequest.requestPathInfo.suffixResource.parent.path,'HTML_ATTR')}" title="${contentPublishedMessage}" data-title="${unpublishMessage}" data-path=".Main-Content form">
                    ${unpublishMessage}
                </a>
            </c:when>
            <c:when test="${publishableResource.published}">
                <a class="Fetch-Modal button is-success is-outlined" href="/cms/shared/unpublish.html${sling:encode(slingRequest.requestPathInfo.suffixResource.parent.path,'HTML_ATTR')}" title="${contentPublishedMessage}" data-title="${unpublishMessage}" data-path=".Main-Content form">
                    ${unpublishMessage}
                </a>
            </c:when>
            <c:otherwise>
                <a class="Fetch-Modal button is-warning is-outlined" href="/cms/shared/publish.html${sling:encode(slingRequest.requestPathInfo.suffixResource.parent.path,'HTML_ATTR')}" title="${contentNotPublishedMessage}" data-title="${publishMessage}" data-path=".Main-Content form">
                    ${publishMessage}
                </a>
            </c:otherwise>
        </c:choose>
    </div>
</div>


