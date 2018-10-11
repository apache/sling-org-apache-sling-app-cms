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
            var $modal = $('<div class="modal"><div class="modal-background"></div><div class="modal-card is-draggable"><header class="modal-card-head"><p class="modal-card-title">'+title+'</p><button class="delete" aria-label="close"></button></header><section class="modal-card-body">'+message+'</section><footer class="modal-card-foot"><button type="button" class="close-modal button is-primary">OK</button></footer></div>');
            $('body').append($modal);
            $modal.addClass('is-active');
            $modal.find('.delete,.close-modal').click(function(){
                $modal.css('display','none').remove();
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
                    location.reload();
                });
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
    


nomnom.decorate('.suffix-form', {
    events :{
        submit : function(event){
            event.preventDefault();
            var suffix = $(this).find('input[name=suffix]').val();
            var path = $(this).attr('action');
            window.location = path + suffix;
            return false;
        }
    }
});
    
nomnom.decorate('.richtext', {
    initCallback: function(){
        $(this).summernote({
            toolbar: [
                ['style', ['bold', 'italic', 'clear','strikethrough', 'superscript', 'subscript']],
                ['insert', ['picture', 'link', 'table', 'hr']],
                ['para', ['style','ul', 'ol', 'paragraph']],
                ['misc', ['codeview', 'undo','redo','help']]
            ],
            followingToolbar: false,
            dialogsInBody: true,
            height: 200,
            onCreateLink: function (url) {
                return url;
            },
            callbacks: {
                onDialogShown: function(){
                    Sling.CMS.ui.suggest($('.note-link-url')[0], 'content', '/content');
                    Sling.CMS.ui.suggest($('.note-image-url')[0], 'content', '/content');
                }
            }
        });
    }
});
    
nomnom.decorate('.page-properties-container', {
    initCallback: function(){
        var $ctr = $(this);
        var $wrapper = $ctr.closest('.form-wrapper');
        $($ctr.data('source')).change(function(){
            var config = $(this).val();
            $ctr.load($ctr.data('path')+config, function(){
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
});
    
nomnom.decorate(".namehint", {
    initCallback: function(){
        var $nh = $(this);
        $nh.parents('.Form-Ajax').find('select[name="sling:resourceType"]').change(function(){
            var resourceType = $(this).val().split("\/");
            $nh.val(resourceType[resourceType.length - 1]);
        });
     }
});

nomnom.decorate(".table .load-versions", {
   
    initCallback: function() {
        var $ctr = $(this);
        var $table = $ctr.closest('.table');
        $.getJSON($ctr.data('url'),function(res){
            $table.dataTable().api().destroy();
            var source   = $('#'+$ctr.data('template')).html();
            var template = Handlebars.compile(source);
            $ctr.append(template(res));
        });
    }
    
});


nomnom.decorate('.search-button', {
    events :{
        click : function(){
            Sling.CMS.ext['searchbutton'] =  Sling.CMS.ext['searchbutton'] || {};
            var searchbutton = Sling.CMS.ext['searchbutton'];
            searchbutton.active = $(event.target).closest('.field').find('.pathfield');
        }
    }
});


nomnom.decorate('.search-select-button', {
    events :{
        click : function(event){
            var $btn = $(evt.target);
            var $active = Sling.CMS.ext['searchbutton'].active;
            $active.val($btn.data('path'));
            $btn.closest('.modal').remove();
        }
    }
});



nomnom.decorate(".table", {
   
    initCallback: function(){
        var $table = $(this);
        var sort = $table.data('sort') !== 'false';
        var paginate = $table.data('paginate') !== 'false';
        $table.DataTable({
            sort: sort,
            paginate: paginate
        });
    },
    
    events :{
        "tbody tr > *" : {
            click: function(event){
                $('.actions-target > *').appendTo('tr.is-selected .cell-actions');
                $(this).find('tr').removeClass('is-selected');
                var $target = $(event.target).closest("tr");
                $target.addClass('is-selected');
                $target.find('.cell-actions > *').appendTo('.actions-target')
            }
        }
    }

});

nomnom.decorate('.sling-cms-include-config', {
    initCallback: function() {
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
});

