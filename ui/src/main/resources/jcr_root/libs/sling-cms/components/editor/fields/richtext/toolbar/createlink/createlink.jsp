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
<div data-wysihtml-dialog="createLink" style="display: none;">
    <div class="has-padding-1 has-background-white-ter rte-form">
        <p>Link</p>
        <div class="field has-addons">
            <div class="control is-expanded">
                <input class="input pathfield is-small" data-wysihtml-dialog-field="href" data-type="nt:hierarchyNode" data-base="/content" autocomplete="off" />
            </div>
            <div class="control">
              <a href="/cms/shared/search.html" class="button is-small Fetch-Modal search-button" data-title="Search" data-path=".Main-Content > *">
                  <span class="jam jam-search"></span>
              </a>
            </div>
        </div>
       <div class="field">
            <select class="select is-small" data-wysihtml-dialog-field="target">
                <option value="">Same Window</option>
                <option value="_blank">New Window</option>
            </select>
       </div>
        <div class="buttons">
            <a data-wysihtml-dialog-action="save" class="button is-small is-success">
                <em class="jam jam-check icon"></em>
            </a>
            <a data-wysihtml-dialog-action="cancel" class="button is-small">
                <em class="jam jam-close icon"></em>
            </a>
        </div>
    </div>
</div>