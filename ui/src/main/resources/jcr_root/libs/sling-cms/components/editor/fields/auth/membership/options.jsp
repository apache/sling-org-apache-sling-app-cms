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
<datalist id="labelfield-${fn:replace(resource.name,':','-')}">
    <c:set var="query" value="SELECT * FROM [rep:Group] WHERE ISDESCENDANTNODE([/home/groups]) ORDER BY [rep:principalName]" />
    <c:forEach var="group" items="${sling:findResources(resourceResolver,query,'JCR-SQL2')}">
        <option value="${sling:encode(group.path,'HTML_ATTR')}">${sling:encode(group.valueMap['rep:principalName'],'HTML')}</option>
    </c:forEach>
</datalist>