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
<sling:adaptTo adaptable="${slingRequest}" adaptTo="org.apache.sling.thumbnails.RenderedResource" var="rendered" />
<option value=""><fmt:message key="None" /></option>
<fmt:message key="Asset-Specific Renditions" var="specificMsg" />
<optgroup label="${specificMsg}">
    <c:forEach var="rendition" items="${rendered.supportedRenditions}">
        <option ${slingRequest.requestPathInfo.suffixResource.valueMap.transformation == rendition ? 'selected' : ''} value="${sling:encode(rendition,'HTML_ATTR')}">
            ${sling:encode(rendition,'HTML')}
        </option>
    </c:forEach>
</optgroup>
<fmt:message key="All Renditions" var="allMsg" />
<optgroup label="${allMsg}">
    <sling:findResources query="SELECT * FROM [nt:unstructured] WHERE ISDESCENDANTNODE([/conf]) AND [sling:resourceType]='sling/thumbnails/transformation' ORDER BY [name]" language="JCR-SQL2" var="transformations" />
    <c:forEach var="transformation" items="${transformations}">
        <option ${slingRequest.requestPathInfo.suffixResource.valueMap.transformation == transformation.name ? 'selected' : ''} value="${sling:encode(transformation.valueMap.name,'HTML_ATTR')}">
            ${sling:encode(transformation.valueMap.name,'HTML')}
        </option>
    </c:forEach>
</optgroup>