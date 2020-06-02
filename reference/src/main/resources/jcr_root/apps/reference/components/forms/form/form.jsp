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
<c:set var="formConfig" value="${sling:adaptTo(resource,'org.apache.sling.cms.ComponentConfiguration').properties}" scope="request" />
<c:set var="formData" value="${sling:adaptTo(slingRequest,'org.apache.sling.cms.reference.forms.FormRequest').formData}" scope="request" />
<form class="${formConfig.formClass}" action="${resource.path}.allowpost.html" method="post" data-analytics-id="${sling:encode(properties.formId,'HTML_ATTR')}">
    <c:if test="${param.message == 'success'}">
        <div class="${formConfig.alertClass}">
            ${properties.successMessage}
        </div>
    </c:if>
    <c:if test="${param.error == 'actions'}">
        <div class="${formConfig.alertClass}">
            ${properties.actionsErrorMessage}
        </div>
    </c:if>
    <c:if test="${param.error == 'fields'}">
        <div class="${formConfig.alertClass}">
            ${properties.fieldsErrorMessage}
        </div>
    </c:if>

    <c:set var="oldAvailableTypes" value="${availableTypes}" />

    <c:if test="${cmsEditEnabled == 'true'}">
        <strong>Providers</strong>
    </c:if>
    <c:set var="availableTypes" value="${formConfig.providerConfigGroups}" scope="request" />
    <sling:include path="providers" resourceType="sling-cms/components/general/reloadcontainer" />
    
    <c:if test="${cmsEditEnabled == 'true'}">
        <strong>Fields</strong>
    </c:if>
    <c:set var="availableTypes" value="${formConfig.fieldConfigGroups}" scope="request" />
    <sling:include path="fields" resourceType="sling-cms/components/general/reloadcontainer" />

    <c:if test="${cmsEditEnabled == 'true'}">
        <strong>Actions</strong>
    </c:if>
    <c:set var="availableTypes" value="${formConfig.actionConfigGroups}" scope="request" />
    <sling:include path="actions" resourceType="sling-cms/components/general/reloadcontainer" />
    <c:set var="availableTypes" value="${oldAvailableTypes}" scope="request" />
    
    <button type="submit" class="${formConfig.submitClass}">${properties.submitText}</button>
    
</form>