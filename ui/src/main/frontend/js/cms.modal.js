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
    rava.bind("a.Fetch-Modal", {
        events: {
            click: function (event) {
                event.preventDefault();
                this.disabled = true;
                this.getModal(this.getAttribute('data-title'), this.getAttribute('href'), this);
            }
        },
        methods: {
            getModal: function (title, link, button) {
                if (window.self !== window.top && window.parent.parent) {
                    window.parent.parent.postMessage({
                        "action": "slingcms.openmodal",
                        "url": link,
                        "title": title
                    }, window.location.origin);
                    button.removeAttribute('disabled');
                } else {
                    var modal = Sling.CMS.ui.loaderModal(),
                        request = new XMLHttpRequest();
                    request.open('GET', link, true);
                    request.onload = function () {
                        if (this.status === 500 || this.status === 404) {
                            Sling.CMS.ui.confirmMessage(request.statusText, request.statusText, function () {
                                Sling.CMS.ui.reloadContext();
                            });
                        } else {
                            button.removeAttribute("disabled");
                            if (request.responseURL.indexOf('/system/sling/form/login?resource=') !== -1) {
                                window.location.reload();
                            } else {
                                modal.innerHTML = request.responseText;
                            }
                        }
                    };
                    modal.querySelector('.modal-background').addEventListener('click', function () {
                        request.abort();
                        button.removeAttribute('disabled');
                    });
                    request.send();
                }
            }
        }
    });
    
    rava.bind(".modal", {
        events: {
            ".close,.modal-close,.close-modal,.modal-background": {
                click: function (event) {
                    event.preventDefault();
                    this.remove();
                }
            }
        }
    });
    
    window.addEventListener("message", function (event) {
        if (event.data.action === 'slingcms.openmodal') {
            Sling.CMS.pathfield = event.source;
            var modal = Sling.CMS.ui.loaderModal(),
                request = new XMLHttpRequest();
            request.open('GET', event.data.url, true);
            request.onload = function () {
                if (this.status === 500 || this.status === 404) {
                    Sling.CMS.ui.confirmMessage(request.statusText, request.statusText, function () {
                        Sling.CMS.ui.reloadContext();
                    });
                } else {
                    if (request.responseURL.indexOf('/system/sling/form/login?resource=') !== -1) {
                        window.location.reload();
                    } else {
                        modal.innerHTML = request.responseText;
                    }
                }
            };
            modal.querySelector('.modal-background').addEventListener('click', function () {
                request.abort();
            });
            request.send();
        }
        
    }, false);

}(window.rava = window.rava || {}, window.Sling = window.Sling || {}));