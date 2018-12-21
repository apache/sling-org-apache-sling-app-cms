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
var Sling = {};
Sling.CMS = {
    handlers: {
        handledelete: function(res, msg){
            if(window.location.pathname.indexOf(res.path) !== -1){
                window.top.Sling.CMS.ui.confirmMessage(msg, res.title,function(){
                    window.location = '/cms';
                });
            } else {
                Sling.CMS.ui.confirmReload(res, msg);
            }
        },
        handlemove: function(res, msg){
            var changes = res.changes[0];
            if(changes.type === 'moved' && window.location.pathname.indexOf(changes.argument[0]) !== -1){
                window.top.Sling.CMS.ui.confirmMessage(msg, res.title,function(){
                    window.location = window.location.href.replace(changes.argument[0], changes.argument[1]);
                });
            } else {
                Sling.CMS.ui.confirmReload(res, msg);
            }
        },
        handleugc: function(res, msg){
            Sling.CMS.ui.confirmMessage(msg, res.title,function(){
                window.location = '/cms/usergenerated/content.html'+res.parentLocation;
            });
        }
    },
    ui: {
        confirmMessage: function(title, message, complete){
            var $modal = $('<div class="modal"><div class="modal-background"></div><div class="is-draggable modal-content"><div class="box"><h3 class="modal-title">'+title+'</h3><p>'+message+'</p><br/><button type="button" class="close-modal button is-primary">OK</button></div></div><button class="modal-close is-large" aria-label="close"></button>');
            $('body').append($modal);
            $modal.addClass('is-active');
            $modal.find('.modal-close,.close-modal,.modal-background').click(function(){
                complete();
            });
            $modal.find('.delete,.close-modal').focus();
            return $modal;
        },
        confirmReload: function(res, msg) {
            if(window.self !== window.top){
                window.top.Sling.CMS.ui.confirmMessage(msg, res.title,function(){
                    window.top.location.reload();
                });
            } else {
                Sling.CMS.ui.confirmMessage(msg, res.title,function(){
                    Sling.CMS.ui.reloadContext();
                });
            }
        },
        confirmReloadComponent: function(res, msg, path) {
            window.top.Sling.CMS.ui.confirmMessage(msg, res.title,function(){
                var modal = window.top.Sling.CMS.ui.loaderModal('Refreshing...');
                window.parent.window.CMSEditor.ui.reloadComponent(path, function(){
                    modal.remove();
                });
            });
        },
        loaderModal: function(message){
            message = message || 'Loading...';
            var modal = document.createElement('div');
            modal.classList.add('modal');
            modal.innerHTML = '<div class="modal-background"></div><div class="modal-content"><div class="box"><h3>'+message+'</h3><div class="loader is-loading"></div></div></div>';
            document.querySelector('body').appendChild(modal);
            modal.classList.add('is-active');
            return modal;
        },
        reloadContext: function(){
            // close all existing modals
            document.querySelectorAll('.modal').forEach(modal => modal.remove());
            // reset the actions
            document.querySelectorAll('.actions-target *').forEach(child => child.remove());
            var containers = document.querySelectorAll('.reload-container');
            var modal = Sling.CMS.ui.loaderModal('Refreshing...');
            var count = containers.length;
            if(count !== 0){
                containers.forEach(function(container){
                    var request = new XMLHttpRequest();
                    var link = container.dataset.path;
                    request.open('GET', link, true);
                    request.onload = function () {
                        var tmp = document.createElement('div');
                        tmp.innerHTML = request.responseText;
                        container.replaceWith(tmp.querySelector('.reload-container'));
                        tmp.remove();
                        count--;
                        if(count === 0){
                            modal.remove();
                        }
                    };
                    request.send();
                });
            } else {
                location.reload();
            }
        },
        suggest: function(selector, type, base){
            var xhr;
            new autoComplete({
                minChars: 1,
                selector: selector,
                source: function(term, response){
                    try {
                        xhr.abort();
                    } catch(e){}
                    if(term === '/'){
                        term = base;
                    }
                    xhr = $.getJSON('/bin/cms/paths', { path: term, type: type }, function(data){
                        response(data);
                    });
                }
            });
        }
    },
    utils: {
        form2Obj: function ($form){
            var data = {};
            $.map($form.serializeArray(), function(n, i){
                data[n['name']] = n['value'];
            });
            return data;
        }
    }
};

window.onbeforeunload = function() {
    if (document.querySelector('.modal')) {
        return "Are you sure you want to leave this page?";
    }
}
    
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
                    var source   = $('#content-template').html();
                    var template = Handlebars.compile(source);
                    var updateContent = function(){
                        if(!$wrapper.is(':disabled')){
                            var data = Sling.CMS.utils.form2Obj($ctr.parents('form'));
                            $('input[name=":content"]').val(template(data));
                        }
                    }
                    $ctr.find('input,textarea,select').change(updateContent);
                    $ctr.parents('form').submit(updateContent);
                });
            });
        }
    }
});

rava.bind('.sling-cms-include-config', {
    callbacks : {
        created :  function() {
            var $ctr = $(this);
            var load = function(){
                var config = $($ctr.data('source')).find('option:selected').data('config');
                
                if(config){
                    $ctr.load(config + $ctr.parents('form').attr('action'));
                }
            };
            $($ctr.data('source')).change(load);
            load();
        }
    }
});

