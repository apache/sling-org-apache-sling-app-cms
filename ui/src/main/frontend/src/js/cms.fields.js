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

/** 
 * Utility scripts for decorating form fields
 */
/* eslint-env browser, es6 */
(function (rava, wysihtml, wysihtmlParserRules) {
    'use strict';
    
    /* Support for file uploads */
    var setProgress = function(meter, progress){
        meter.innerText = Math.round(progress)+'%';
        meter.value = Math.round(progress);
    }
    var uploadFile = function(meter, action, file) {
        var formData = new FormData();
        
        formData.append('*', file);
        formData.append("*@TypeHint", "sling:File");
        
        var xhr = new XMLHttpRequest();
        xhr.upload.addEventListener('loadstart', function(){
            setProgress(meter, 0);
        }, false);
        xhr.upload.addEventListener('progress', function(event) {
            var percent = event.loaded / event.total * 100;
            setProgress(meter, percent);
        }, false);
        xhr.upload.addEventListener('load', function(){
            meter.classList.add('is-info');
        }, false);
        xhr.addEventListener('readystatechange', function(event) {
            var status, text, readyState;
            try {
                readyState = event.target.readyState;
                text = event.target.responseText;
                status = event.target.status;
            }catch(e) {
            }
            if (readyState == 4){
                meter.classList.remove('is-info');
                if(status == '200' && text) {
                    meter.classList.add('is-success');
                } else {
                    meter.classList.add('is-danger');
                    console.warn('Failed to upload %s, recieved message %s', file.name, text);
                }
            }
        }, false);
        xhr.open('POST', action, true);
        xhr.send(formData);
    }
    var handleFile = function(scope, file){
        var it = document.createElement('div'),
            ctr = scope.closest('.control').querySelector('.file-item-container'),
            meter = null;
        it.innerHTML = document.querySelector('.file-item-template').innerHTML;
        meter = it.querySelector('.progress');
        it.querySelector('.file-item-name').innerText = file.name;
        ctr.classList.remove('is-hidden');
        ctr.appendChild(it);
        
        uploadFile(meter,scope.closest('form').action, file);
    }
    rava.bind(".file", {
        callbacks: {
            created : function () {
                var field = this,
                    close = field.closest('form').querySelector('a.close');

                field.addEventListener("dragover", function(event) {
                    event.preventDefault();
                }, false);
                field.addEventListener("dragenter", function(event) {
                    event.preventDefault();
                    field.classList.add('is-primary');
                }, false);
                field.addEventListener("dragleave", function(event) {
                    event.preventDefault();
                    field.classList.remove('is-primary');
                }, false);
                field.addEventListener("drop", function(event) {
                    event.preventDefault();
                    field.classList.remove('is-primary');
                    if (event.dataTransfer.items) {
                        for (var i = 0; i < event.dataTransfer.items.length; i++) {
                            if (event.dataTransfer.items[i].kind === 'file') {
                                handleFile(field, event.dataTransfer.items[i].getAsFile());
                            }
                        }
                    } else {
                        for (var i = 0; i < event.dataTransfer.files.length; i++) {
                            handleFile(field, event.dataTransfer.files[i]);
                        }
                    }
                }, false);
                field.closest('form').querySelector('button[type=submit]').remove();
                close.innerText = 'Done';
                close.addEventListener('click', function(event){
                    window.Sling.CMS.ui.reloadContext();
                });
            }
        },
        events: {
            input: {
                change : function (event) {
                    for(var i = 0; i < event.target.files.length; i++){
                        handleFile(event.target, event.target.files[i]);
                    }
                }
            }
        }
    });

    /* Support for updating the namehint when creating a component */
    rava.bind(".namehint", {
        callbacks: {
            created : function () {
                var field = this;
                this.closest('.Form-Ajax').querySelector('select[name="sling:resourceType"]').addEventListener('change', function (evt) {
                    var resourceType = evt.target.value.split("\/");
                    field.value = resourceType[resourceType.length - 1];
                });
            }
        }
    });
    
    /* Support for repeating form fields */
    rava.bind(".repeating", {
        callbacks: {
            created : function () {
                var ctr = this;
                this.querySelectorAll(".repeating__add").forEach(function (el) {
                    el.addEventListener('click', function (event) {
                        event.stopPropagation();
                        event.preventDefault();
                        var node = ctr.querySelector('.repeating__template > .repeating__item').cloneNode(true);
                        ctr.querySelector('.repeating__container').appendChild(node);
                    });
                });
            }
        }
    });
    rava.bind(".repeating__item", {
        events: {
            ":scope .repeating__remove" : {
                click: function (event) {
                    event.stopPropagation();
                    event.preventDefault();
                    this.remove();
                }
            }
        }
    });
    
    rava.bind('.rte', {
        callbacks : {
            created : function () {
                new wysihtml.Editor(this.querySelector('.rte-editor'), {
                    toolbar: this.querySelector('.rte-toolbar'),
                    parserRules:  wysihtmlParserRules
                });
            }
        }
    });
}(window.rava = window.rava || {}, window.wysihtml = window.wysihtml || {}, window.wysihtmlParserRules = window.wysihtmlParserRules || {}));