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
<h3><fmt:message key="Rewrite Configuration" /></h3>
<dl>
    <dt><fmt:message key="DocType" /></dt>
    <dd>
        <sling:encode value="${resource.valueMap.doctype}" mode="HTML" />
    </dd>
    <dt><fmt:message key="Rewritten Attributes" /></dt>
    <dd>
        <ul>
            <c:forEach var="attribute" items="${resource.valueMap.attributes}">
                <li>
                    <sling:encode value="${attribute}" mode="HTML" />
                </li>
            </c:forEach>
        </ul>
    </dd>
</dl>