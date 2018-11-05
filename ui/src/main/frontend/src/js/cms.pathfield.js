/*
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
 */
/* eslint-env browser, es6 */
(function (rava, Sling) {
    'use strict';
    var pathfield = null;
    rava.decorate("input.pathfield", {
        callbacks: {
            created : function () {
                var type = this.dataset.type,
                    base = this.dataset.base;
                Sling.CMS.ui.suggest(this, type, base);
            }
        }
    });
    rava.decorate('.search-button', {
        events: {
            click: function () {
                pathfield =  this.closest('.field').querySelector('.pathfield');
            }
        }
    });
    rava.decorate('.search-select-button', {
        events: {
            click : function () {
                pathfield.value = this.dataset.path;
                this.closest('.modal').remove();
            }
        }
    });
    
}(window.rava = window.rava || {}, window.Sling = window.Sling || {}));