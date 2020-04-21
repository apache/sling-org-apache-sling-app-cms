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
 <sling:getCAConfigResources var="editors" resource="${slingRequest.requestPathInfo.suffixResource}" bucket="files" name="editors" />
<c:set var="mimetype" value="|${slingRequest.requestPathInfo.suffixResource.valueMap['jcr:content/jcr:mimeType']}|" />
<c:forEach var="editor" items="${editors}">
    <c:set var="mimetypes" value="|${fn:join(sling:getRelativeResource(editor,'jcr:content').valueMap.mimetypes,'|')}|" />
    <c:choose>
        <c:when test="${fn:length(mimetypes) == 2}">
            <c:set var="general" value="${editor}" />
        </c:when>
        <c:when test="${fn:contains(mimetypes,mimetype)}">
            <c:set var="matches" value="${editor}" />
        </c:when>
    </c:choose>
</c:forEach>
<form method="post" action="${sling:encode(slingRequest.requestPathInfo.suffix,'HTML_ATTR')}" enctype="multipart/form-data" class="Form-Ajax">
    <fieldset class="form-wrapper field">
        <c:choose>
            <c:when test="${matches != null}">
                <sling:include path="${matches.path}/fields" resourceType="sling-cms/components/general/container" />
            </c:when>
            <c:when test="${general != null}">
                <sling:include path="${general.path}/fields" resourceType="sling-cms/components/general/container" />
            </c:when>
            <c:otherwise>
                No editor configured for <sling:encode value="${slingRequest.requestPathInfo.suffixResource.valueMap['jcr:content/jcr:mimeType']}" mode="HTML" />!
            </c:otherwise>
        </c:choose>
        <div class="Field-Group">
            <button type="submit" class="button is-primary">
                Save File
            </button>
            <button type="button" class="button close">Cancel</button>
        </div>
    </fieldset>
</form>