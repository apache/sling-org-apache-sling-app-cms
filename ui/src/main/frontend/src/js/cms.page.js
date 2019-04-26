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
(function (Handlebars, rava, Sling) {
    'use strict';

    function removeProperty(obj, propertyToDelete) {
        var prop;
        for (prop in obj) {
            if (prop === propertyToDelete) {
                delete obj[prop];
            } else if (typeof obj[prop] === 'object') {
                removeProperty(obj[prop], propertyToDelete);
            }
        }
    }

    var getPageTemplate = function (pageConfig, cb) {
        fetch(pageConfig + ".infinity.json").then(function (response) {
            return response.json();
        }).then(function (result) {
            var template;
            if (typeof result.template === "string") {
                //String template
                template = result.template;
            } else if (typeof result.template === "object") {
                //Json template
                removeProperty(result.template, "jcr:created");
                removeProperty(result.template, "jcr:createdBy");
                template = JSON.stringify(result.template);
            }
            cb(template);
        });
    };

    rava.bind('.page-properties-container', {
        callbacks : {
            created :  function () {
                var container = this,
                    wrapper = container.closest('.form-wrapper');
                document.querySelector(container.dataset.source).addEventListener('change', function () {
                    var sourceSelect = this,
                        config = this.value;
                    sourceSelect.disabled = true;
                    container.innerHTML = '';
                    fetch(container.dataset.path + config).then(function (response) {
                        return response.text();
                    }).then(function (formHtml) {
                        getPageTemplate(config, function (source) {
                            var template = Handlebars.compile(source),
                                updateContent = function () {
                                    if (!wrapper.disabled) {
                                        var data = Sling.CMS.utils.form2Obj(container.closest('form'));
                                        document.querySelector('input[name=":content"]').value = template(data);
                                    }
                                };
                            container.innerHTML = formHtml;
                            document.querySelectorAll('input,textarea,select').forEach(function (el) {
                                el.addEventListener('change', updateContent);
                            });
                            container.closest('form').addEventListener('submit', updateContent);
                            sourceSelect.disabled = false;
                        });
                    });
                });
            }
        }
    });

}(window.Handlebars = window.Handlebars || {}, window.rava = window.rava || {}, window.Sling = window.Sling || {}));