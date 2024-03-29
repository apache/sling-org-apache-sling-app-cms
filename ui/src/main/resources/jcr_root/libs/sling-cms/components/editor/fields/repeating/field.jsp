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
<div class="repeating">
    <fieldset disabled="disabled" class="repeating__template is-hidden">
        <div class="repeating__item field has-addons">
            <div class="control is-expanded">
                <input type="${sling:encode(properties.type,'HTML_ATTR')}" value="" class="input" name="${sling:encode(properties.name,'HTML_ATTR')}" ${required} ${disabled} />
            </div>
            <div class="control">
                <button class="repeating__remove button">
                    <span class="jam jam-minus">
                        <span class="is-sr-only">
                            <fmt:message key="Remove" />
                        </span>
                    </span>
                </button>
            </div>
        </div>
    </fieldset>
    <div class="repeating__container">
        <c:forEach var="value" items="${value}">
            <div class="repeating__item field has-addons">
                <div class="control is-expanded">
                    <input type="${sling:encode(properties.type,'HTML_ATTR')}" value="${sling:encode(value,'HTML_ATTR')}" class="input" name="${sling:encode(properties.name,'HTML_ATTR')}" ${required} ${disabled} />
                </div>
                <div class="control">
                    <button class="repeating__remove button">
                        <span class="jam jam-minus">
                            <span class="is-sr-only">
                                <fmt:message key="Remove" />
                            </span>
                        </span>
                    </button>
                </div>
            </div>
        </c:forEach>
    </div>
    <fmt:message key="Add" var="addMsg" />
    <button type="button" class="repeating__add button" aria-label="${addMsg}" name="${sling:encode(properties.name,'HTML_ATTR')}">
        <span class="jam jam-plus"></span>
    </button>
</div>