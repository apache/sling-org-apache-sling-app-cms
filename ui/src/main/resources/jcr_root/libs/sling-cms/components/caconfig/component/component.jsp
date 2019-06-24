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
<c:if test="${not empty properties.type}">
    <h4>
        <sling:encode value="${properties.type}" mode="HTML" />
    </h4>
    <dl>
        <c:forEach var="val" items="${properties}">
            <c:if test="${not fn:contains(val.key,':') and val.key != 'type'}">
                <dt>
                    <sling:encode value="${val.key}" mode="HTML" />
                </dt>
                <dd>
                      <c:catch var="ex">
                        <c:forEach var="item" items="${val.value}">
                            <sling:encode value="${item}" mode="HTML" />
                        </c:forEach>
                    </c:catch>
                    <c:if test="${ex != null}">
                        <sling:encode value="${val.value}" mode="HTML" />
                    </c:if>
                </dd>
            </c:if>
    </c:forEach>
    </dl>
</c:if>
