/*
w * Licensed to the Apache Software Foundation (ASF) under one
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
    
    rava.bind(".Form-Ajax", {
        events: {
            ":scope .close" : {
                click : function () {
                    if (window.parent && window.parent.window && window.parent.window.CMSEditor) {
                        window.parent.window.CMSEditor.ui.hideModal();
                    }
                }
            },
            submit : function (event) {
                event.preventDefault();
                var form = this,
                    jcrcontent = false,
                    callback = form.dataset.callback,
                    formData = new FormData(form);
                form.querySelectorAll('input,select,textarea').forEach(function (el) {
                    if (el.name.indexOf('jcr:content') !== -1) {
                        jcrcontent = true;
                    }
                });
                if (form.dataset.addDate && !form.querySelector('input[name="jcr:content/jcr:lastModified"]')) {
                    var dateContainer = document.createElement('div');
                    if (jcrcontent) {
                        dateContainer.innerHTML = '<input type="hidden" name="jcr:content/jcr:lastModified" />' +
                            '<input type="hidden" name="jcr:content/jcr:lastModifiedBy" />' +
                            '<input type="hidden" name="jcr:content/jcr:created" />' +
                            '<input type="hidden" name="jcr:content/jcr:createdBy" />';
                    } else {
                        dateContainer.innerHTML = '<input type="hidden" name="jcr:lastModified" />' +
                            '<input type="hidden" name="jcr:lastModifiedBy" />' +
                            '<input type="hidden" name="jcr:created" />' +
                            '<input type="hidden" name="jcr:createdBy" />';
                    }
                    form.appendChild(dateContainer);
                }
                fetch(form.action, {
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
                    if (window.self !== window.top) {
                        window.top.Sling.CMS.ui.confirmMessage(error.message, error.message, function () {
                            form.querySelector('.form-wrapper').disabled = false;
                        });
                    } else {
                        Sling.CMS.ui.confirmMessage(error.message, error.message, function () {
                            form.querySelector('.form-wrapper').disabled = false;
                        });
                    }
                }).then(function (res) {
                    if (callback && Sling.CMS.handlers[callback]) {
                        Sling.CMS.handlers[callback](res, 'success');
                    } else if (window.parent.window.CMSEditor) {
                        var reloadParent = false,
                            path = res.path;
                        res.changes.forEach(function (change) {
                            if (change.type !== 'modified') {
                                reloadParent = true;
                            }
                        });
                        if (reloadParent) {
                            let pathArr = path.split('\/');
                            pathArr.pop();
                            path = pathArr.join('/');
                        }
                        Sling.CMS.ui.confirmReloadComponent(res, 'success', path);
                    } else {
                        Sling.CMS.ui.confirmReload(res, 'success');
                    }
                });
                form.querySelector('.form-wrapper').disabled = true;
            }
        }
    });

    rava.bind('.get-form', {
        events : {
            submit : function (event) {
                event.preventDefault();
                event.stopPropagation();
                var modal = Sling.CMS.ui.loaderModal('Loading...');
                var form = this,
                    wrapper = form.querySelector('.form-wrapper');
                fetch(form.action + '?' + new URLSearchParams(new FormData(form)).toString()).then(function (response) {
                    if (!response.ok) {
                        modal.remove();
                        throw new Error(response.statusText);
                    }
                    return response.text();
                }).then(function (text) {
                    var tmp = document.createElement('div');
                    tmp.innerHTML = text;
                    document.querySelector(form.dataset.target).innerHTML = tmp.querySelector(form.dataset.load).innerHTML;
                    tmp.remove();
                    if (wrapper) {
                        wrapper.disabled = false;
                    }
                    modal.remove();
                });
                if (wrapper) {
                    wrapper.disabled = true;
                }
            }
        }
    });
    
    rava.bind('.suffix-form', {
        events: {
            submit: function (event) {
                event.preventDefault();
                var suffix = this.querySelector('input[name=suffix]').value,
                    path = this.action;
                window.location = path + suffix;
                return false;
            }
        }
    });

}(window.rava = window.rava || {}, window.Sling = window.Sling || {}));