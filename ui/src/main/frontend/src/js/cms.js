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
/* eslint-env es6, browser */

(function (rava) {
    'use strict';
    window.Sling = window.Sling || {};
    var Sling = window.Sling;
    window.Sling.CMS = {
        handlers: {
            handledelete: function (res, msg) {
                if (window.location.pathname.indexOf(res.path) !== -1) {
                    window.top.Sling.CMS.ui.confirmMessage(msg, res.title, function () {
                        window.location = '/cms';
                    });
                } else {
                    Sling.CMS.ui.confirmReload(res, msg);
                }
            },
            handlemove: function (res, msg) {
                var changes = res.changes[0];
                if (changes.type === 'moved' && window.location.pathname.indexOf(changes.argument[0]) !== -1) {
                    window.top.Sling.CMS.ui.confirmMessage(msg, res.title, function () {
                        window.location = window.location.href.replace(changes.argument[0], changes.argument[1]);
                    });
                } else {
                    Sling.CMS.ui.confirmReload(res, msg);
                }
            },
            handleugc: function (res, msg) {
                Sling.CMS.ui.confirmMessage(msg, res.title, function () {
                    window.location = '/cms/usergenerated/content.html' + res.parentLocation;
                });
            }
        },
        ui: {
            confirmMessage: function (title, message, complete) {
                var modal = document.createElement('div');
                modal.innerHTML = '<div class="modal-background"></div><div class="is-draggable modal-content"><div class="box"><h3 class="modal-title">' + title + '</h3><p>' + message + '</p><br/><button type="button" class="close-modal button is-primary">OK</button></div></div><button class="modal-close is-large" aria-label="close"></button>';
                document.body.appendChild(modal);
                modal.classList.add('modal');
                modal.classList.add('is-active');
                modal.querySelectorAll('.modal-close,.close-modal,.modal-background').forEach(function (closer) {
                    closer.addEventListener('click', function () {
                        modal.remove();
                        complete();
                    });
                });
                modal.querySelector('.delete,.close-modal').focus();
                return modal;
            },
            confirmReload: function (res, msg) {
                if (window.self !== window.top) {
                    window.top.Sling.CMS.ui.confirmMessage(msg, res.title, function () {
                        window.top.location.reload();
                    });
                } else {
                    Sling.CMS.ui.confirmMessage(msg, res.title, function () {
                        Sling.CMS.ui.reloadContext();
                    });
                }
            },
            confirmReloadComponent: function (res, msg, path) {
                window.top.Sling.CMS.ui.confirmMessage(msg, res.title, function () {
                    var modal = window.top.Sling.CMS.ui.loaderModal('Refreshing...');
                    window.parent.window.CMSEditor.ui.reloadComponent(path, function () {
                        modal.remove();
                    });
                });
            },
            loaderModal: function (message) {
                message = message || 'Loading...';
                var modal = document.createElement('div');
                modal.classList.add('modal');
                modal.innerHTML = '<div class="modal-background"></div><div class="modal-content"><div class="box"><h3>' + message + '</h3><div class="loader is-loading"></div></div></div>';
                document.querySelector('body').appendChild(modal);
                modal.classList.add('is-active');
                return modal;
            },
            reloadContext: function () {
                // close all existing modals
                document.querySelectorAll('.modal').forEach(function (modal) {
                    modal.remove();
                });
                // reset the actions
                document.querySelectorAll('.actions-target *').forEach(function (child) {
                    child.remove();
                });
                var containers = document.querySelectorAll('.reload-container'),
                    modal = Sling.CMS.ui.loaderModal('Refreshing...'),
                    count = containers.length;
                if (count !== 0) {
                    containers.forEach(function (container) {
                        var request = new XMLHttpRequest(),
                            link = container.dataset.path;
                        if (link.indexOf('?') === -1) {
                            link += '?tstamp=' + Date.now();
                        } else {
                            link += '&tstamp=' + Date.now();
                        }
                        request.open('GET', link, true);
                        request.onload = function () {
                            var tmp = document.createElement('div');
                            tmp.innerHTML = request.responseText;
                            container.replaceWith(tmp.querySelector('.reload-container'));
                            tmp.remove();
                            count = count - 1;
                            if (count === 0) {
                                modal.remove();
                            }
                        };
                        request.send();
                    });
                } else {
                    location.reload();
                }
            }
        },
        utils: {
            form2Obj: function (form) {
                var data = {};
                form.querySelectorAll('input,textarea,select').forEach(function (f) {
                    if (!f.disabled && f.name) {
                        data[f.name] = f.value;
                    }
                });
                return data;
            }
        }
    };

    if (window.performance && window.performance.navigation.type === window.performance.navigation.TYPE_BACK_FORWARD) {
        Sling.CMS.ui.reloadContext();
    }

    window.onbeforeunload = function () {
        if (document.querySelector('.modal form')) {
            return "Are you sure you want to leave this page?";
        }
    };

    if (document.querySelector('.breadcrumb .is-active') !== null) {
        var itemTitle = document.querySelector('.breadcrumb .is-active').textContent.trim();
        if (itemTitle.length > 0) {
            document.title = itemTitle + ' :: ' + document.title;
        }
    }

    rava.bind('.sling-cms-include-config', {
        callbacks : {
            created :  function () {
                var container = this,
                    load = function () {
                        var config = document.querySelector(container.dataset.source).selectedOptions[0].dataset.config;
                        if (config) {
                            fetch(config + container.closest('form').attributes.action.value).then(function (result) {
                                return result.text();
                            }).then(function (markup) {
                                container.innerHTML = markup;
                            });
                        }
                    };
                document.querySelector(container.dataset.source).addEventListener('change', load);
                load();
            }
        }
    });

}(window.rava = window.rava || {}));