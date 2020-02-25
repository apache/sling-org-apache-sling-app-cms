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
<form method="post"
    action="${sling:encode(slingRequest.requestPathInfo.suffix,'HTML_ATTR')}${sling:encode(properties.actionSuffix,'HTML_ATTR')}"
    enctype="multipart/form-data" class="Form-Ajax"
    data-add-date="${properties.addDate != false}"
    data-callback="${properties.callback}">
    <fieldset class="form-wrapper field">
        <input type="hidden" name="_charset_" value="utf-8" />
        <sling:include path="fields" resourceType="sling-cms/components/general/container" />
        <button type="submit" class="button is-primary">
            <sling:encode value="${properties.button}" default="Save" mode="HTML" />
        </button>
        <c:if test="${properties.skipcancel != true}">
            <a href="${sling:encode(header.referer,'HTML_ATTR')}" class="button close">Cancel</a>
        </c:if>
    </fieldset>
</form>