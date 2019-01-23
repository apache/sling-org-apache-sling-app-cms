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
<c:set var="file" value="${sling:adaptTo(slingRequest.requestPathInfo.suffixResource,'org.apache.sling.cms.File')}" />
<div class="field">
    <c:if test="${not empty properties.label}">
        <label class="label">
            <sling:encode value="${properties.label}" mode="HTML" />
        </label>
    </c:if>
    <div class="control">
        <dl class="fixed-box box">
        <c:forEach var="element" items="${file.metadata}">
            <dt><sling:encode value="${element.key}" mode="HTML" /></dt>
            <dd>
                <c:catch var="ce">
                    <fmt:formatDate value="${element.value.time}" type="both" timeStyle="long" dateStyle="long" />
                </c:catch>
                <c:if test="${ce != null}">
                    <sling:encode value="${element.value}" mode="HTML" />
                </c:if>
            </dd>
        </c:forEach>
        </dl>
    </div>
</div>
