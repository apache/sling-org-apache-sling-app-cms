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
/* eslint-env browser */
(function () {
    'use strict';
    if (!window.CMSEditor) {
        var CMSEditor = {
            init: function () {
                CMSEditor.util.attachClick('.sling-cms-editor .button[data-sling-cms-action=add]', function () {
                    CMSEditor.ui.showModal('/cms/editor/add.html' + this.dataset.slingCmsPath + '?availableTypes=' + this.dataset.slingCmsAvailableTypes, this.title);
                });
                CMSEditor.util.attachClick('.sling-cms-editor .button[data-sling-cms-action=delete]', function () {
                    CMSEditor.ui.showModal('/cms/editor/delete.html' + this.dataset.slingCmsPath, this.title);
                });
                CMSEditor.util.attachClick('.sling-cms-editor .button[data-sling-cms-action=edit]', function () {
                    CMSEditor.ui.showModal(
                        '/cms/editor/edit.html' + this.dataset.slingCmsPath + '?editor=' + this.dataset.slingCmsEdit,
                        CMSEditor.util.findParent(this, '.sling-cms-component').dataset.slingCmsTitle || this.title
                    );
                });
                CMSEditor.util.attachClick('.sling-cms-editor .button[data-sling-cms-action=reorder]', function () {
                    CMSEditor.ui.showModal('/cms/editor/reorder.html' + this.dataset.slingCmsPath, this.title);
                });

                // closing the modal
                CMSEditor.util.attachClick('.sling-cms-editor .close-modal', function () {
                    CMSEditor.ui.hideModal();
                });
                window.addEventListener('keypress', function (e) {
                    if (e.keyCode === 27 && CMSEditor.ui.modalDisplayed === true) {
                        CMSEditor.ui.hideModal();
                    }
                });

                var mouseX, mouseY;

                function draggable(element) {
                    var mouseDown = false, elementX = 0, elementY = 0;

                      // mouse button down over the element
                    element.addEventListener('mousedown', function (evt) {
                        if (evt.target.matches('.modal-card-body *')) {
                            return;
                        }
                        mouseX = evt.clientX;
                        mouseY = evt.clientY;
                        mouseDown = true;
                    });

                    var moveComplete = function () {
                        mouseDown = false;
                        elementX = parseInt(element.style.left, 10) || 0;
                        elementY = parseInt(element.style.top, 10) || 0;
                        return false;
                    };

                    element.addEventListener('mouseup', moveComplete);
                    document.addEventListener('mouseout', moveComplete);

                    document.addEventListener('mousemove', function (event) {
                        if (!mouseDown) {
                            return;
                        }
                        var deltaX = event.clientX - mouseX, deltaY = event.clientY - mouseY;
                        element.style.left = elementX + deltaX + 'px';
                        element.style.top = elementY + deltaY + 'px';
                        return false;
                    });
                }
                draggable(document.querySelector('.sling-cms-editor .modal-card'));
            },
            ui: {
                modalDisplayed: false,
                hideModal: function () {
                    if (CMSEditor.ui.modalDisplayed) {
                        document.querySelector('.sling-cms-editor .modal').classList.remove('is-active');
                        CMSEditor.ui.modalDisplayed = false;
                    }
                },
                showModal: function (url, title) {
                    title = title || '';
                    if (CMSEditor.ui.modalDisplayed) {
                        CMSEditor.ui.hideModal();
                    }
                    document.querySelector('.sling-cms-editor .modal-card-title').innerText = title;
                    document.querySelector('.sling-cms-editor .modal-card-body').innerHTML = '<iframe class="modal-frame" src="' + url + '"></iframe>';
                    document.querySelector('.sling-cms-editor .modal').classList.add('is-active');

                    CMSEditor.ui.modalDisplayed = true;
                }
            },
            util: {
                attachClick: function (exp, cb) {
                    document.querySelectorAll(exp).forEach(function (el) {
                        el.addEventListener('click', cb, false);
                    });
                },
                findParent: function (el, exp) {
                    if (el === null || el.parentElement === null) {
                        return null;
                    } else if (el.parentElement.matches(exp)) {
                        return el.parentElement;
                    } else {
                        return CMSEditor.util.findParent(el.parentElement, exp);
                    }
                }
            }
        };
        window.CMSEditor = CMSEditor;

        if (document.readyState === 'complete') {
            window.CMSEditor.init();
        } else {
            document.addEventListener('DOMContentLoaded', CMSEditor.init, false);
        }
    }
}());