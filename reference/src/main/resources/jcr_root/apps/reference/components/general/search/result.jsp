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
<div class="searchresult ${sling:encode(searchConfig.resultClass,'HTML_ATTR')}">
    <div class="searchresult__header ${sling:encode(searchConfig.resultHeaderClass,'HTML_ATTR')}">
        <a href="${sling:encode(result.path,'HTML_ATTR')}.html" class="searchresult__link">
            <sling:encode value="${result.valueMap['jcr:content/jcr:title']}" mode="HTML" />
        </a>
    </div>
    <p class="searchresult__body">
        <sling:encode value="${result.valueMap['jcr:content/jcr:description']}" mode="HTML" />
    </p>
    <a href="${sling:encode(result.path,'HTML_ATTR')}.html" class="searchresult__link">
        ${sling:encode(fn:replace(result.path,sling:getAbsoluteParent(result,3).path,''),'HTML')}.html
    </a>
</div>