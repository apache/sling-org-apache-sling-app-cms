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
<sling:adaptTo adaptable="${slingRequest.requestPathInfo.suffixResource}" adaptTo="org.apache.sling.cms.PublishableResource" var="publishableResource" />
<dl class="my-3">
    <dt>
        <fmt:message key="slingcms.published" />
    </dt>
    <dd>
        <sling:encode value="${publishableResource.published}"  mode="HTML" />
    </dd>
    <c:if test="${publishableResource.lastPublication.time != null}">
        <dt>
            <fmt:message key="slingcms.lastpublication" />
        </dt>
        <dd>
            <fmt:formatDate value="${publishableResource.lastPublication.time}" type="both" />
        </dd>
    </c:if>
    <c:if test="${not empty publishableResource.lastPublicationBy}">
        <dt>
            <fmt:message key="slingcms.lastpublicationby" />
        </dt>
        <dd>
            <sling:encode value="${publishableResource.lastPublicationBy}"  mode="HTML" />
        </dd>
    </c:if>
    <c:if test="${not empty publishableResource.lastPublicationType}">
        <dt>
            <fmt:message key="slingcms.lastpublicationtype" />
        </dt>
        <dd>
            <sling:encode value="${publishableResource.lastPublicationType}"  mode="HTML" />
        </dd>
    </c:if>
</dl>
<hr/>