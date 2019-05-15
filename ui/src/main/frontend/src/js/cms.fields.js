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
    
    /* Update the name on file selection */
    rava.bind(".file", {
        events: {
            input: {
                change : function (event) {
                    var nameField = event.target.closest('.file').querySelector('.file-name');
                    if (nameField) {
                        nameField.textContent = event.target.files[0].name;
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