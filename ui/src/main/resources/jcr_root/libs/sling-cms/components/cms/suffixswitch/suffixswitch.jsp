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
<c:choose>
    <c:when test="${not empty slingRequest.requestPathInfo.suffix}">
        <sling:include path="content" resourceType="sling-cms/components/general/container" />
    </c:when>
    <c:otherwise>
        <form method="get" action="${properties.target}" class="suffix-form">
            <label class="label" for="suffix">
                <sling:encode value="${properties.label}" mode="HTML" />
                <span class="has-text-danger">*</span>
            </label>
            <div class="field has-addons">
                <div class="control is-expanded">
                    <input class="input pathfield" type="text" name="suffix" required="required" data-type="${properties.type}" data-base="${properties.base}" autocomplete="off" />
                </div>
                <div class="control">
                    <a href="/cms/shared/search.html" class="button Fetch-Modal search-button" data-title="Search" data-path=".Main-Content > *">
                        <span class="jam jam-search"></span>
                    </a>
                </div>
            </div>
            <div class="field">
                <button type="submit" class="button is-primary">${sling:encode(properties.button,'HTML')}</button>
            </div>
        </form>
    </c:otherwise>
</c:choose>