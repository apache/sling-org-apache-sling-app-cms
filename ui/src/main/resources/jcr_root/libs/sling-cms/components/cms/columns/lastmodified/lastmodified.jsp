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
<c:set var="modifiedProperty" value="${colConfig.valueMap.subPath}jcr:lastModified" />
<c:set var="modifiedByProperty" value="${colConfig.valueMap.subPath}jcr:lastModifiedBy" />
<c:catch var="dateEx">
    <fmt:formatDate var="lastModified" type = "both"  dateStyle = "medium" timeStyle = "medium" value="${resource.valueMap[modifiedProperty].time}" />
    <c:set var="colValue" value="${lastModified} - ${resource.valueMap[modifiedByProperty]}" />
    <td title="${sling:encode(colValue,'HTML_ATTR')}">
        <sling:encode value="${lastModified}" mode="HTML" /><br/>
        <sling:encode value="${resource.valueMap[modifiedByProperty]}" mode="HTML" />
    </td>
</c:catch>
<c:if test="${dateEx != null}">
    <td title="">
    </td>
</c:if>