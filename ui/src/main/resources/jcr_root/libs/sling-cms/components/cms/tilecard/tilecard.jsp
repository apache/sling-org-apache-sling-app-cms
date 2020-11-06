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
<div class="tile is-parent is-3 contentnav__item">
    <div class="tile is-child">
        <div class="card is-linked" title="${sling:encode(properties.title,'HTML_ATTR')}" data-value="${resource.path}">
            <div class="card-image">
                <figure class="image is-5by4">
                    <img src="${branding.gridIconsBase}${sling:encode(properties.icon,'HTML_ATTR')}" loading="lazy" alt="${sling:encode(properties.title,'HTML_ATTR')}">
                </figure>
            </div>
            <footer class="card-footer">
                <a class="card-footer-item" href="${sling:encode(properties.link,'HTML_ATTR')}" title="${sling:encode(properties.title,'HTML')}">
                    ${sling:encode(properties.title,'HTML')}
                </a>
            </footer>
        </div>
    </div>
</div> 