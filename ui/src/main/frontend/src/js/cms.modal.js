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
(function (nomnom) {
    'use strict';
    nomnom.decorate("a.Fetch-Modal", {
        events: {
            click: function (event) {
                event.preventDefault();
                this.setAttribute("disabled", "disabled");
                this.getModal(this.getAttribute('data-title'), encodeURI(this.getAttribute('href')), this);
            }
        },
        methods: {
            getModal: function (title, link, button) {
                var modal = document.createElement('div');
                modal.classList.add('modal');
                modal.innerHTML = '<div class="modal-background"></div><div class="modal-card is-draggable"><header class="modal-card-head"><p class="modal-card-title">' + title + '</p><button class="delete" aria-label="close"></button></header><section class="modal-card-body"><div class="loader is-loading"></div></section><footer class="modal-card-foot"></footer>';
                document.querySelector('body').appendChild(modal);
                
                var request = new XMLHttpRequest();
                request.open('GET', link, true);
                request.onload = function () {
                    button.removeAttribute("disabled");
                    if (request.responseURL.indexOf('/system/sling/form/login?resource=') !== -1) {
                        window.location.reload();
                    } else {
                        modal.querySelector('.modal-card-body').innerHTML = request.responseText;
                    }
                };
                request.send();
                
                modal.querySelector('.delete,.close-modal').addEventListener("click", function () {
                    modal.remove();
                    return false;
                });
                modal.classList.add('is-active');
            }
        }
    });

}(window.nomnom = window.nomnom || {}));