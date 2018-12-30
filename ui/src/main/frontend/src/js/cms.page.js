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

    function removeProperty(obj, propertyToDelete) {
        for(prop in obj) {
            if (prop === propertyToDelete)
                delete obj[prop];
            else if (typeof obj[prop] === 'object')
                removeProperty(obj[prop], propertyToDelete);
        }
    }


    var getPageTemplate = function(pageConfig){
        var source = "";
        $.ajax({
            url: pageConfig + ".infinity.json",
            success: function(result){
                if (typeof result.template === "string") {
                    //String template
                    source = result.template;
                } else if (typeof result.template === "object"){
                    //Json template
                    removeProperty(result.template, "jcr:created");
                    removeProperty(result.template, "jcr:createdBy");
                    source = JSON.stringify(result.template);
                }
            },
            async: false
        });

        return source;
    };

    rava.bind('.page-properties-container', {
        callbacks : {
            created :  function(){
                var $ctr = $(this);
                var $wrapper = $ctr.closest('.form-wrapper');
                $($ctr.data('source')).change(function(){
                    var $source = $(this);
                    $source.attr('disabled', 'disabled');
                    $ctr.html('');
                    var config = $(this).val();
                    $ctr.load($ctr.data('path')+config, function(){
                        $source.removeAttr('disabled');

                        var source = getPageTemplate(config);

                        var template = Handlebars.compile(source);
                        var updateContent = function(){
                            if(!$wrapper.is(':disabled')){
                                var data = Sling.CMS.utils.form2Obj($ctr.parents('form'));
                                $('input[name=":content"]').val(template(data));
                            }
                        };
                        $ctr.find('input,textarea,select').change(updateContent);
                        $ctr.parents('form').submit(updateContent);
                    });
                });
            }
        }
    });

}(window.rava = window.rava || {}, window.Sling = window.Sling || {}));