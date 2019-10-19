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
                    CMSEditor.util.attachEvents(document);
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
                        var component = document.querySelector('.sling-cms-component[data-sling-cms-resource-path="' + path + '"]'),
                            reloadPage = component.dataset.reload  === 'true';
                        while (!component && path.length > 1) {
                            var pathArr = path.split('\/');
                            pathArr.pop();
                            path = pathArr.join('/');
                            component = document.querySelector('.sling-cms-component[data-sling-cms-resource-path="' + path + '"]');
                            if(component.dataset.reload  === 'true') {
                                reloadPage = true;
                            }
                        }
                        if (!component || reloadPage) {
                            CMSEditor.ui.hideModal();
                            window.top.location.reload();
                        }
                        fetch('/cms/page/pagewrapper.html' + path + '?forceResourceType=' + component.dataset.slingCmsResourceType, {
                            redirect: 'manual'
                        }).then(function (response) {
                            if (!response.ok) {
                                CMSEditor.ui.hideModal();
                                window.top.location.reload();
                            }
                            return response.text();
                        }).then(function (html) {
                            var tmp = document.createElement('div');
                            tmp.innerHTML = html;
                            CMSEditor.util.attachEvents(tmp);
                            component.replaceWith(tmp.querySelector('.sling-cms-component'));
                            tmp.remove();
                            CMSEditor.ui.hideModal();
                            cb();
                        });
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
                    attachDrag: function(ed) {
                        let dragImage;
                        let dragActive = false;
                        let handleOver = function(evt) {
                            evt.preventDefault();
                            document.querySelectorAll('.sling-cms-droptarget').forEach(dt => {
                                dt.classList.remove('sling-cms-droptarget__is-over');
                            });
                            evt.target.classList.add('sling-cms-droptarget__is-over');
                        };
                        let handleLeave = function(evt) {
                            evt.target.classList.remove('sling-cms-droptarget__is-over');
                        };
                        let handleDrop = function(evt) {
                            evt.preventDefault();

                            let sourcePath = evt.dataTransfer.getData('path');
                            let sourceName = evt.dataTransfer.getData('name');
                            let targetPath = evt.target.dataset.path + '/' + sourceName;
                            
                            let move = function() {
                                var formData = new FormData();
                                formData.append('_charset_', 'utf-8');
                                if(sourcePath !== targetPath){
                                    formData.append(':dest', targetPath);
                                    formData.append(':operation', 'move');
                                }
                                formData.append(':nameHint', sourceName);
                                formData.append(':order',  evt.target.dataset.order);
                                let ui;
                                if(typeof Sling !== 'undefined') {
                                    ui = Sling.CMS.ui;
                                } else {
                                    ui = window.parent.Sling.CMS.ui
                                }
                                fetch(sourcePath, {
                                    method: 'POST',
                                    body: formData,
                                    cache: 'no-cache',
                                    headers: {
                                        "Accept": "application/json"
                                    }
                                }).then(function (response) {
                                    if (!response.ok) {
                                        throw new Error(response.statusText);
                                    }
                                    return response.json();
                                }).catch(function (error) {
                                    ui.confirmMessage(error.message, error.message, function () {});
                                }).then(function (res) {
                                    ui.confirmReload(res, 'success');
                                });
                                evt.target.classList.remove('sling-cms-droptarget__is-over');
                            }
                            if(evt.target.dataset.create) {
                                var formData = new FormData();
                                formData.append('_charset_', 'utf-8');
                                formData.append('jcr:primaryType',  'nt:unstructured');
                                let ui;
                                if(typeof Sling !== 'undefined') {
                                    ui = Sling.CMS.ui;
                                } else {
                                    ui = window.parent.Sling.CMS.ui
                                }
                                fetch(evt.target.dataset.path, {
                                    method: 'POST',
                                    body: formData,
                                    cache: 'no-cache',
                                    headers: {
                                        "Accept": "application/json"
                                    }
                                }).then(function (response) {
                                    if (!response.ok) {
                                        throw new Error(response.statusText);
                                    }
                                    return response.json();
                                }).catch(function (error) {
                                    ui.confirmMessage(error.message, error.message, function () {});
                                }).then(function (res) {
                                    move();
                                });
                            } else {
                                move();
                            }
                        };
                        let activateTargets = function(){
                            if(dragActive) {
                                document.querySelectorAll('.sling-cms-droptarget').forEach(dt => {
                                    dt.classList.add('sling-cms-droptarget__is-active');
                                    dt.addEventListener('dragover', handleOver);
                                    dt.addEventListener('dragleave', handleLeave);
                                    dt.addEventListener('drop', handleDrop);
                                });
                            }
                        }
                        let deactivateTargets = function(){
                            dragActive = false;
                            document.querySelectorAll('.sling-cms-droptarget').forEach(dt => {
                                dt.classList.remove('sling-cms-droptarget__is-active');
                                dt.removeEventListener('dragover', handleOver);
                                dt.removeEventListener('dragleave', handleLeave);
                                dt.removeEventListener('drop', handleDrop);
                            });
                            if(dragImage) {
                                dragImage.remove();
                            }
                        }
                        ed.addEventListener('dragstart', function(evt) {
                            evt.stopPropagation();
                            dragActive = true;
                            evt.dataTransfer.effectAllowed = 'move';
                            dragImage = document.createElement('div');
                            dragImage.setAttribute('style', 'position: absolute; left: 0px; top: 0px; width: ' + ed.offsetWidth + 
                                'px; height: ' + ed.offsetHeight+  'px; padding: 0.5em; background: grey; z-index: -1');
                            dragImage.innerText = ed.querySelector('.level-right').innerText;
                            document.body.appendChild(dragImage);
                            evt.dataTransfer.setDragImage(dragImage, 20, 20);
                            evt.dataTransfer.setData('path', ed.closest('.sling-cms-component').dataset.slingCmsResourcePath);
                            evt.dataTransfer.setData('name', ed.closest('.sling-cms-component').dataset.slingCmsResourceName);
                            setTimeout(activateTargets, 10);
                        });
                        ed.addEventListener('dragend', deactivateTargets);
                    },
                    attachEvents: function (ctx) {
                        CMSEditor.util.attachClick(ctx, '.sling-cms-editor .action-button', function (event) {
                            event.preventDefault();
                            CMSEditor.ui.showModal(this.href, this.title);
                        });
                        if (ctx.matches && ctx.matches('.sling-cms-component')) {
                            CMSEditor.util.attachHover(c);
                        }
                        ctx.querySelectorAll('.sling-cms-component').forEach(c => {
                            CMSEditor.util.attachHover(c);
                        });
                        ctx.querySelectorAll('.sling-cms-editor').forEach(CMSEditor.util.attachDrag);
                    },
                    attachHover: function (ctx) {
                        let resetActive = function() {
                            document.querySelectorAll('.sling-cms-component__is-active').forEach(a => {
                                a.classList.remove('sling-cms-component__is-active'); 
                            });
                        }
                        ctx.addEventListener('mouseover', function(evt){
                            resetActive();
                            ctx.classList.add('sling-cms-component__is-active');
                            evt.stopPropagation();
                        });
                        ctx.addEventListener('mouseleave', resetActive);
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