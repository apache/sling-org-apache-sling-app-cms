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
<sling:adaptTo var="optimizer" adaptable="${slingRequest.requestPathInfo.suffixResource}" adaptTo="org.apache.sling.fileoptim.models.OptimizeResource" />
<sling:adaptTo var="optimizedFile" adaptable="${sling:getRelativeResource(slingRequest.requestPathInfo.suffixResource,'jcr:content')}" adaptTo="org.apache.sling.fileoptim.models.OptimizedFile" />
<div class="columns">
    <div class="column">
        <h4>Info</h4>
        <c:choose>
            <c:when test="${optimizer.canOptimize && optimizer.result.optimized}">
                <dl>
                    <dt>
                        Algorithm
                    </dt>
                    <dd>
                        ${optimizer.result.algorithm}
                    </dd>
                    <dt>
                        Original Size
                    </dt>
                    <dd>
                        ${optimizer.result.originalSize}
                    </dd>
                    <dt>
                        Optimized Size
                    </dt>
                    <dd>
                        ${optimizer.result.optimizedSize}
                    </dd>
                    <dt>
                        Savings
                    </dt>
                    <dd>
                        <fmt:formatNumber value="${optimizer.result.savings * 100}" type="number" groupingUsed="false" maxFractionDigits="2" />%
                    </dd>
                </dl>
                <form action="${sling:encode(slingRequest.requestPathInfo.suffix,'HTML_ATTR')}" class="Form-Ajax" method="post">
                    <fieldset class="form-wrapper field">
                        <input type="hidden" name=":operation" value="fileoptim:optimize" />
                        <button type="submit" class="button is-primary">
                            Optimize
                        </button>
                        <button type="button" class="button close">Cancel</button>
                    </fieldset>
                </form>
            </c:when>
            <c:when test="${optimizer.optimized}">
                <strong>Already Optimized</strong>
                <dl>
                    <dt>
                        Algorithm
                    </dt>
                    <dd>
                        ${optimizedFile.algorithm}
                    </dd>
                    <dt>
                        Savings
                    </dt>
                    <dd>
                        <fmt:formatNumber value="${optimizedFile.savings * 100}" type="number" groupingUsed="false" maxFractionDigits="2" />%
                    </dd>
                </dl>
                <form action="${sling:encode(slingRequest.requestPathInfo.suffix,'HTML_ATTR')}" class="Form-Ajax" method="post">
                    <fieldset class="form-wrapper field">
                        <input type="hidden" name=":operation" value="fileoptim:restore" />
                        <button type="submit" class="button is-primary">
                            Restore Original
                        </button>
                        <button type="button" class="button close">Cancel</button>
                    </fieldset>
                </form>
            </c:when>
            <c:otherwise>
                <strong>File Cannot be Optimized</strong>
                <div>
                    <button type="button" class="button close">Close</button>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="column">
        <c:if test="${optimizer.canOptimize && optimizer.result.optimized}">
            <h4>Preview</h4>
            <img src="/system/fileoptim/preview?path=${sling:encode(slingRequest.requestPathInfo.suffix,'HTML_ATTR')}" class="preview" />
        </c:if>
    </div>
</div>
