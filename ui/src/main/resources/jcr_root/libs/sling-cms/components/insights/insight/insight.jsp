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
<article class="message ${insight.primaryMessage.styleClass}">
    <div class="message-header toggle-hidden" data-target="#${insight.provider.id}-body">
        <p>
            <sling:encode value="${insight.provider.title}" mode="HTML" />
            <c:if test="${insight.scored}">
                <span class="score" data-score="${insight.score}">
                    <fmt:formatNumber type="percent" maxFractionDigits="2" value="${insight.score}" />
                </span>
            </c:if>
        </p>
        <c:if test="${not empty insight.moreDetailsLink}">
            <a class="button ${insight.primaryMessage.styleClass}" href="${insight.moreDetailsLink}" target="_blank">
                <em class="jam jam-info icon"></em>
            </a>
        </c:if>
    </div>
    <div class="message-body is-hidden" id="${insight.provider.id}-body">
        <strong><sling:encode value="${insight.primaryMessage.text}" mode="HTML" /></strong>
        <c:if test="${fn:length(insight.scoreDetails) > 0}">
            <ul>
                <c:forEach var="detail" items="${insight.scoreDetails}">
                    <li>
                        <c:if test="${detail.style != 'DEFAULT'}">
                            <sling:encode value="${detail.style}" mode="HTML" /> -
                        </c:if>
                        <sling:encode value="${detail.text}" mode="HTML" />
                    </li>
                </c:forEach>
            </ul>
        </c:if>
    </div>
</article>