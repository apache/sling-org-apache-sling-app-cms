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


rava.decorate(".Form-Ajax", {
    callbacks: {
        created : function () {
            var close = this.querySelector('.close');
            if(close){
                close.addEventListener('click', function(){
                    if(window.parent && window.parent.window && window.parent.window.CMSEditor) {
                        window.parent.window.CMSEditor.ui.hideModal();
                    }
                });
            }
        }
    },
    events :{
        submit : function(event){
            event.preventDefault();
            var $form = $(this);
            var jcrcontent = false;
            $form.find('input,select,textarea').each(function(idx,inp){
                if(inp.name.indexOf('jcr:content') != -1){
                    jcrcontent = true;
                }
            });
            if($form.data('addDate') && $form.find('input[name="jcr:content/jcr:lastModified"]').length == 0){
                if(jcrcontent){
                    $form.append('<input type="hidden" name="jcr:content/jcr:lastModified" />');
                    $form.append('<input type="hidden" name="jcr:content/jcr:lastModifiedBy" />');
                    $form.append('<input type="hidden" name="jcr:content/jcr:created" />');
                    $form.append('<input type="hidden" name="jcr:content/jcr:createdBy" />');
                } else {
                    $form.append('<input type="hidden" name="jcr:lastModified" />');
                    $form.append('<input type="hidden" name="jcr:lastModifiedBy" />');
                    $form.append('<input type="hidden" name="jcr:created" />');
                    $form.append('<input type="hidden" name="jcr:createdBy" />');
                }
            }
            var callback = $form.data('callback');
            var data = new FormData(this);
            $form.find('.form-wrapper').attr('disabled', 'disabled');
            $.ajax({
                url: $form.attr('action'),
                type: 'POST',
                data: data,
                processData: false,
                contentType: false,
                dataType: 'json',
                success: function(res,msg){
                    if (callback && Sling.CMS.handlers[callback]){
                        Sling.CMS.handlers[callback](res, msg);
                    } else {
                        Sling.CMS.ui.confirmReload(res, msg);
                    }
                },
                error: function(xhr, msg, err){
                    if(window.self !== window.top){
                        window.top.Sling.CMS.ui.confirmMessage(msg, err,function(){
                            $form.find('.form-wrapper').removeAttr('disabled');
                        });
                    } else {
                        Sling.CMS.ui.confirmMessage(msg, err,function(){
                            $form.find('.form-wrapper').removeAttr('disabled');
                        });
                    }
                }
            });
            return false;
        }
    }
});


rava.decorate('.Get-Form', {
    events : {
        submit : function (event) {
            event.preventDefault();
            event.stopPropagation();
            var $form = $(this);
            var params = $form.serialize();
            $form.find('.form-wrapper').attr('disabled', 'disabled');
            $($form.data('target')).load($form.attr('action') + '?' + params +'  ' + $form.data('load'), function(){
                $form.find('.form-wrapper').removeAttr('disabled');
            });
            return false;
        }
    }
});


rava.decorate('.suffix-form', {
    events: {
        submit: function (event) {
            event.preventDefault();
            var suffix = $(this).find('input[name=suffix]').val();
            var path = $(this).attr('action');
            window.location = path + suffix;
            return false;
        }
    }
});
