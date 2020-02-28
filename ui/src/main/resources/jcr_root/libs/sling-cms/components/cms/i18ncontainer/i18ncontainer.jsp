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
<div class="scroll-container reload-container" data-path="${resource.path}.html${sling:encode(slingRequest.requestPathInfo.suffix,'HTML_ATTR')}">
    <sling:include path="${resource.path}" resourceType="sling-cms/components/general/container" />
    <c:forEach var="language" items="${sling:listChildren(slingRequest.requestPathInfo.suffixResource)}">
        <c:if test="${firstChild == null && not empty language.valueMap['jcr:language']}">
            <c:set var="firstChild" value="${language}" />
        </c:if>
    </c:forEach>
    <nav class="level">
        <div class="level-left">
            <div class="level-item">
                <a class="Button Fetch-Modal" data-title="Add Entry" data-path=".Main-Content form" href="/cms/i18n/entry/create.html${firstChild.path}">+ Entry</a>
            </div>
        </div>
    </nav>
    <form method="post" action="${sling:encode(slingRequest.requestPathInfo.suffix,'HTML_ATTR')}" enctype="multipart/form-data" class="Form-Ajax" data-add-date="false">
        <fieldset class="form-wrapper field">
            <input type="hidden" name="_charset_" value="utf-8" />
            <table class="table is-fullwidth is-striped">
                <thead>
                    <tr>
                        <th class="Column-key" scope="col">
                            Key
                        </th>
                        <c:forEach var="language" items="${sling:listChildren(slingRequest.requestPathInfo.suffixResource)}">
                            <c:if test="${not empty language.valueMap['jcr:language']}">
                                <th class="Column-${language.valueMap['jcr:language']}" scope="col">
                                    <sling:adaptTo adaptable="${language}" adaptTo="org.apache.sling.cms.core.models.LocaleResource" var="localeResource" />
                                    <sling:encode value="${localeResource.locale.displayLanguage}" mode="HTML" /> <sling:encode value="${localeResource.locale.displayCountry}" mode="HTML" />
                                    <br/>
                                    <small>(<sling:encode value="${language.valueMap['jcr:language']}" mode="HTML" />)</small>
                                </th>
                            </c:if>
                        </c:forEach>
                    </tr>
                </thead>
                <tbody>
                    <sling:adaptTo var="helper" adaptable="${slingRequest.requestPathInfo.suffixResource}" adaptTo="org.apache.sling.cms.core.models.i18nHelper" />
                    <c:forEach var="key" items="${helper.keys}">
                        <tr>
                            <td>
                                <sling:encode value="${key}" mode="HTML" />
                            </td>
                            <c:forEach var="language" items="${sling:listChildren(slingRequest.requestPathInfo.suffixResource)}">
                                <c:if test="${not empty language.valueMap['jcr:language']}">
                                    <td>
                                        <c:set var="keyfound" value="false" />
                                        <c:forEach var="entry" items="${sling:listChildren(language)}">
                                            <c:if test="${entry.valueMap['sling:key'] == key}">
                                                <c:set var="keyfound" value="true" />
                                                <input name="${language.name}/${entry.name}/sling:message" class="input" type="text" value="${sling:encode(entry.valueMap['sling:message'],'HTML_ATTR')}" />
                                                <input name="${language.name}/${entry.name}/sling:key" type="hidden" value="${key}" />
                                            </c:if>
                                        </c:forEach>
                                        <c:if test="${keyfound == 'false'}">
                                            <input name="${language.name}/${key}/sling:message" class="input" type="text" value="" />
                                            <input name="${language.name}/${key}/sling:key" type="hidden" value="${key}" />
                                            <input name="${language.name}/${key}/jcr:primaryType" type="hidden" value="sling:MessageEntry" />
                                        </c:if>
                                    </td>
                                </c:if>
                            </c:forEach>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <button class="button is-primary">Save i18n Dictionary</button>
        </fieldset>
    </form>
</div>
