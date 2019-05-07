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
(function (rava, Sling, autoComplete) {
    'use strict';
    
    Sling.CMS.pathfield = null;
    
    rava.bind("input.pathfield", {
        callbacks: {
            created : function () {
                var type = this.dataset.type,
                    base = this.dataset.base;
                new autoComplete({
                    minChars: 1,
                    selector: this,
                    source: function (term, response) {
                        if (term === '/') {
                            term = base;
                        }
                        fetch('/bin/cms/paths?path=' + encodeURIComponent(term) + '&type=' + encodeURIComponent(type)).then(function (response) {
                            return response.json();
                        }).then(function (data) {
                            response(data);
                        });
                    }
                });
            }
        }
    });
    
    rava.bind('.search-button', {
        events: {
            click: function () {
                Sling.CMS.pathfield =  this.closest('.field').querySelector('.pathfield');
            }
        }
    });
    
    rava.bind('.search-select-button', {
        events: {
            click : function () {
                var path = this.dataset.path;
                if (Sling.CMS.pathfield instanceof HTMLInputElement) {
                    Sling.CMS.pathfield.value = this.dataset.path;
                } else {
                    Sling.CMS.pathfield.postMessage({
                        "action": "slingcms.setpath",
                        "path": path
                    }, window.location.origin);
                }
                this.closest('.modal').remove();
            }
        }
    });

    window.addEventListener("message", function (event) {
        if (event.data.action === 'slingcms.setpath') {
            Sling.CMS.pathfield.value = event.data.path;
        }
    }, false);
    
}(window.rava = window.rava || {}, window.Sling = window.Sling || {}, window.autoComplete = window.autoComplete || {}));