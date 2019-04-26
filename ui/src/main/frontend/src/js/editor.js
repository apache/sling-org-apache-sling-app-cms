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
        var mouseX,
            mouseY,
            CMSEditor = {
                draggable: null,
                init: function () {
                    CMSEditor.util.attachClicks(document);
                    // closing the modal
                    CMSEditor.util.attachClick(document, '.sling-cms-editor .modal-close, .sling-cms-editor .modal-background', function () {
                        CMSEditor.ui.hideModal();
                    });
                    window.addEventListener('keypress', function (e) {
                        if (e.keyCode === 27 && CMSEditor.ui.modalDisplayed === true) {
                            CMSEditor.ui.hideModal();
                        }
                    });
                },
                ui: {
                    draggable: function (element) {
                        CMSEditor.draggable = element;
                        var mouseDown = false,
                            elementX = 0,
                            elementY = 0,
                            moveComplete = function () {
                                mouseDown = false;
                                elementX = parseInt(CMSEditor.draggable.style.left, 10) || 0;
                                elementY = parseInt(CMSEditor.draggable.style.top, 10) || 0;
                                return false;
                            };

                        // mouse button down over the element
                        CMSEditor.draggable.addEventListener('mousedown', function (evt) {
                            if (!evt.target.matches('.modal-title, .modal-title *')) {
                                return;
                            }
                            mouseX = evt.clientX;
                            mouseY = evt.clientY;
                            mouseDown = true;
                        });
                        CMSEditor.draggable.addEventListener('mouseup', moveComplete);
                        document.addEventListener('mouseout', moveComplete);
                        document.addEventListener('mousemove', function (event) {
                            if (!mouseDown) {
                                return;
                            }
                            var deltaX = event.clientX - mouseX, deltaY = event.clientY - mouseY;
                            CMSEditor.draggable.style.left = elementX + deltaX + 'px';
                            CMSEditor.draggable.style.top = elementY + deltaY + 'px';
                            return false;
                        });
                    },
                    modalDisplayed: false,
                    hideModal: function () {
                        if (CMSEditor.ui.modalDisplayed) {
                            if (document.querySelector('.sling-cms-editor .modal')) {
                                document.querySelector('.sling-cms-editor .modal').remove();
                            }
                            CMSEditor.ui.modalDisplayed = false;
                        }
                    },
                    reloadComponent: function (path, cb) {
                        var component = document.querySelector('.sling-cms-component[data-sling-cms-resource-path="' + path + '"]');
                        while (!component && path.length > 1) {
                            var pathArr = path.split('\/');
                            pathArr.pop();
                            path = pathArr.join('/');
                            component = document.querySelector('.sling-cms-component[data-sling-cms-resource-path="' + path + '"]');
                        }
                        if (!component) {
                            CMSEditor.ui.hideModal();
                            window.top.location.reload();
                        }
                        var request = new XMLHttpRequest();
                        request.open('GET', '/cms/page/pagewrapper.html' + path + '?forceResourceType=' + component.dataset.slingCmsResourceType, true);
                        request.onload = function () {
                            if (request.status === 200) {
                                var tmp = document.createElement('div');
                                tmp.innerHTML = request.responseText;
                                CMSEditor.util.attachClicks(tmp);
                                component.replaceWith(tmp.querySelector('.sling-cms-component'));
                                tmp.remove();
                                CMSEditor.ui.hideModal();
                                cb();
                            } else {
                                CMSEditor.ui.hideModal();
                                window.top.location.reload();
                            }
                        };
                        request.send();
                    },
                    showModal: function (url, title) {
                        title = title || '';
                        if (CMSEditor.ui.modalDisplayed) {
                            CMSEditor.ui.hideModal();
                        }
                        document.querySelector('.sling-cms-editor-final').innerHTML = '<div class="modal"><div class="modal-background"></div><div class="modal-content"><div class="box"><h3 class="modal-title"></h3><section class="modal-body"></section></div></div><button class="modal-close is-large" aria-label="close"></button>';
                        document.querySelector('.sling-cms-editor .modal-title').innerText = title;
                        document.querySelector('.sling-cms-editor .modal-body').innerHTML = '<iframe class="modal-frame" src="' + url + '"></iframe>';
                        document.querySelector('.sling-cms-editor .modal').classList.add('is-active');
                        CMSEditor.util.attachClick(document, '.sling-cms-editor .modal-background, .sling-cms-editor .modal-close', function () {
                            CMSEditor.ui.hideModal();
                        });
                        CMSEditor.ui.draggable(document.querySelector('.sling-cms-editor .modal-content'));
                        CMSEditor.ui.modalDisplayed = true;
                    }
                },
                util: {
                    attachClick: function (ctx, exp, cb) {
                        ctx.querySelectorAll(exp).forEach(function (el) {
                            el.addEventListener('click', cb, false);
                        });
                    },
                    attachClicks: function (ctx) {
                        CMSEditor.util.attachClick(ctx, '.sling-cms-editor .button[data-sling-cms-action=add]', function () {
                            CMSEditor.ui.showModal('/cms/editor/add.html' + this.dataset.slingCmsPath + '?availableTypes=' + this.dataset.slingCmsAvailableTypes, this.title);
                        });
                        CMSEditor.util.attachClick(ctx, '.sling-cms-editor .button[data-sling-cms-action=delete]', function () {
                            CMSEditor.ui.showModal('/cms/editor/delete.html' + this.dataset.slingCmsPath, this.title);
                        });
                        CMSEditor.util.attachClick(ctx, '.sling-cms-editor .button[data-sling-cms-action=edit]', function () {
                            CMSEditor.ui.showModal(
                                '/cms/editor/edit.html' + this.dataset.slingCmsPath + '?editor=' + this.dataset.slingCmsEdit,
                                CMSEditor.util.findParent(this, '.sling-cms-component').dataset.slingCmsTitle || this.title
                            );
                        });
                        CMSEditor.util.attachClick(ctx, '.sling-cms-editor .button[data-sling-cms-action=reorder]', function () {
                            CMSEditor.ui.showModal('/cms/editor/reorder.html' + this.dataset.slingCmsPath, this.title);
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
        window.onbeforeunload = function () {
            if (CMSEditor.ui.modalDisplayed) {
                return "Are you sure you want to leave this page?";
            }
        };
        if (document.readyState === 'complete') {
            window.CMSEditor.init();
        } else {
            document.addEventListener('DOMContentLoaded', CMSEditor.init, false);
        }
    }
}());