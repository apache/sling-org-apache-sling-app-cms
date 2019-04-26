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
 <div class="reload-container table__wrapper" data-path="${resource.path}.html${slingRequest.requestPathInfo.suffix}">
    <form method="get" class="table__filter">
        <p class="control has-icons-left">
            <label class="is-hidden" for="search-term">Search</label>
            <input class="input is-small" type="text" name="search" id="search-term">
            <span class="icon is-small is-left">
                <i class="jam jam-search" aria-hidden="true"></i>
            </span>
        </p>
    </form>
    <table class="table is-fullwidth is-striped sortable">
        <thead>
            <tr>
                <th>
                    #
                </th>
                <c:forEach var="column" items="${sling:listChildren(sling:getRelativeResource(resource,'columns'))}">
                    <th class="${column.name == 'actions' ? 'is-hidden' : '' }" data-attribute="${column.name}">
                        <sling:encode value="${column.valueMap.title}" mode="HTML" />
                    </th>
                </c:forEach>
            </tr>
        </thead>
        <tbody>
            <c:set var="parentPath" value="${slingRequest.requestPathInfo.suffix}${not empty properties.appendSuffix ? properties.appendSuffix : ''}" />
            <c:set var="count" value="1" />
            <c:forEach var="child" items="${sling:listChildren(sling:getResource(resourceResolver, parentPath))}" varStatus="status">
                <sling:getResource var="typeConfig" base="${resource}" path="types/${child.valueMap['jcr:primaryType']}" />
                <c:if test="${typeConfig != null && !fn:contains(child.name,':')}">
                    <tr class="sortable__row" data-resource="${child.path}" data-type="${typeConfig.path}">
                        <td class="Cell-Static" title="# ${status.index + 1}" data-sort-value="<fmt:formatNumber pattern="0000" value="${count}" />">
                            ${count}
                        </td>
                        <c:forEach var="column" items="${sling:listChildren(sling:getRelativeResource(typeConfig,'columns'))}">
                            <c:set var="configPath" value="columns/${column.name}"/>
                            <c:set var="colConfig" value="${sling:getRelativeResource(typeConfig,configPath)}" scope="request" />
                            <c:if test="${colConfig != null}">
                                <sling:include path="${child.path}" resourceType="${colConfig.valueMap['sling:resourceType']}" />
                            </c:if>
                        </c:forEach>
                    </tr>
                    <c:set var="count" value="${count + 1}" />
                </c:if>
            </c:forEach> 
        </tbody>
    </table>
</div>