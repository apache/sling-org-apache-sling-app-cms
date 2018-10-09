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
        ext: {},
        decorate: function($ctx){
            for (var key in Sling.CMS.ext) {
                if(typeof Sling.CMS.ext[key].decorate == 'function'){
                    console.log('Invoking decorate for '+key);
                    Sling.CMS.ext[key].decorate($ctx);
                }
            }
        },
        init: function(){
            for (var key in Sling.CMS.ext) {
                if(typeof Sling.CMS.ext[key].init == 'function'){
                    console.log('Invoking init for '+key);
                    Sling.CMS.ext[key].init();
                }
            }
            Sling.CMS.decorate($(document));
        },
        ui: {
            confirmMessage: function(title, message, complete){
                var $modal = $('<div class="modal"><div class="modal-background"></div><div class="modal-card is-draggable"><header class="modal-card-head"><p class="modal-card-title">'+title+'</p><button class="delete" aria-label="close"></button></header><section class="modal-card-body">'+message+'</section><footer class="modal-card-foot"><button type="button" class="close-modal button is-primary">OK</button></footer></div>');
                $('body').append($modal);
                Sling.CMS.decorate($modal);
                $modal.addClass('is-active');
                $modal.find('.delete,.close-modal').click(function(){
                    $modal.css('display','none').remove();
                    complete();
                });
                return $modal;
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

    Sling.CMS.ext['includeconfig'] = {
        decorate: function($ctx){
            $ctx.find('.sling-cms-include-config').each(function(){
                var $ctr = $(this);
                var load = function(){
                    var config = $($ctr.data('source')).find('option:selected').data('config');
                    
                    if(config){
                        $ctr.load(config + $ctr.parents('form').attr('action'), function(){
                            Sling.CMS.decorate($ctr.children());
                        });
                    }
                };
                $($ctr.data('source')).change(load);
                load();
            });
        }
    };

    Sling.CMS.ext['handledelete'] = function(res, msg){
        if(window.location.pathname.indexOf(res.path) !== -1){
            window.top.Sling.CMS.ui.confirmMessage(msg, res.title,function(){
                window.location = '/cms';
            });
        } else {
            Sling.CMS.ext.reload(res, msg);
        }
    }

    Sling.CMS.ext['handlemove'] = function(res, msg){
        var changes = res.changes[0];
        if(changes.type === 'moved' && window.location.pathname.indexOf(changes.argument[0]) !== -1){
            window.top.Sling.CMS.ui.confirmMessage(msg, res.title,function(){
                window.location = window.location.href.replace(changes.argument[0], changes.argument[1]);
            });
        } else {
            Sling.CMS.ext.reload(res, msg);
        }
    }

    Sling.CMS.ext['handleugc'] = function(res, msg){
        Sling.CMS.ui.confirmMessage(msg, res.title,function(){
            window.location = '/cms/usergenerated/content.html'+res.parentLocation;
        });
    }

    Sling.CMS.ext['namehint'] = {
        decorate: function($ctx){
            $ctx.find('.namehint').each(function(){
                var $nh = $(this);
                $nh.parents('.Form-Ajax').find('select[name="sling:resourceType"]').change(function(){
                    var resourceType = $(this).val().split("\/");
                    $nh.val(resourceType[resourceType.length - 1]);
                });
            });
        }
    };
    
    Sling.CMS.ext['pageproperties'] = {
        decorate: function($ctx){
            $ctx.find('.Sling-CMS__page-properties').each(function(){
                var $ctr = $(this);
                var $wrapper = $ctr.closest('.Form-Ajax__wrapper');
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
                        Sling.CMS.decorate($ctr.children());
                    });
                });
            });
        }
    };

    Sling.CMS.ext['reload'] = function(res, msg) {
        if(window.self !== window.top){
            window.top.Sling.CMS.ui.confirmMessage(msg, res.title,function(){
                window.top.location.reload();
            });
        } else {
            Sling.CMS.ui.confirmMessage(msg, res.title,function(){
                location.reload();
            });
        }
    }
 
    Sling.CMS.ext['repeating'] = {
        decorate: function($ctx){
            $ctx.find('.repeating').each(function(){
                var $rep = $(this);
                $rep.find('.repeating__add').click(function(){
                    var $div = $('<div/>').html($rep.find('.repeating__template').html());
                    Sling.CMS.decorate($div);
                    $rep.find('.repeating__container').append($div);
                    return false;
                });
            });
            $ctx.find('.repeating__remove').click(function(){
                var $rem = $(this);
                $rem.parents('.repeating__item').remove();
                return false;
            });
        }
    };


    Sling.CMS.ext['richtext'] = {
        decorate: function($ctx){
            $ctx.find('.richtext').summernote({
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
                        Sling.CMS.ext.pathfield.suggest($('.note-link-url')[0], 'content', '/content');
                        Sling.CMS.ext.pathfield.suggest($('.note-image-url')[0], 'content', '/content');
                    }
                }
            });
        }
    };
    
    Sling.CMS.ext['suffix-form'] = {
        init: function() {
            $('.suffix-form').submit(function(){
                var suffix = $(this).find('input[name=suffix]').val();
                var path = $(this).attr('action');
                window.location = path + suffix;
                return false;
            });
        }
    }
    
    Sling.CMS.ext['table'] = {
        decorate: function($ctx) {
            $ctx.find('table tbody tr').click(function(el){
                $('.actions-target > *').appendTo('tr.is-selected .cell-actions')
                $('tr').removeClass('is-selected');
                $(this).addClass('is-selected');
                $(this).find('.cell-actions > *').appendTo('.actions-target')
            });

            $ctx.find('table').each(function(){
                var sort = $(this).data('sort') !== 'false';
                var paginate = $(this).data('paginate') !== 'false';
                $(this).DataTable({
                    sort: sort,
                    paginate: paginate
                });
            });
        }
    };

    $(document).ready(function() {
        Sling.CMS.init();
    });

nomnom.decorate(".table .load-versions", class {
   
    nomnomCallback(){
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

nomnom.decorate('.search-button', class {
    
    "click::listen"(event) {
        Sling.CMS.ext['searchbutton'] =  Sling.CMS.ext['searchbutton'] || {};
        var searchbutton = Sling.CMS.ext['searchbutton'];
        searchbutton.active = $(event.target).closest('.field').find('.pathfield');
    }
    
});

nomnom.decorate('.search-select-button', class {
   
    "click::listen"(event) {
        var $btn = $(evt.target);
        var $active = Sling.CMS.ext['searchbutton'].active;
        $active.val($btn.data('path'));
        $btn.closest('.modal').remove();
    }
    
});

nomnom.decorate('.taxonomy', class {
    "click::listen"(){
        var $rep = $(this);
        $rep.find('.taxonomy__add').click(function(){
            var $span = $('<span/>').html($rep.find('.taxonomy__template').html());
            var val = $ctx.find('.taxonomy__field input').val();
            var found = false;
            $rep.find('.taxonomy__item input').each(function(idx, el){
                if($(el).val() === val){
                    found = true;
                }
            });
            if(found){
                return false;
            }
            $span.find('input').val(val);
            var title = $ctx.find('option[value="'+val+'"]').text();
            
            if(title !== ''){
                $span.find('.taxonomy__title').text(title);
                Sling.CMS.decorate($span);
                $('.taxonomy__container').append($span);
                $ctx.find('.taxonomy__field input').val('');
            }
            return false;
        });
    }
});
        
nomnom.decorate('.taxonomy__item', class {
    "click::listen"(){
        $(this).remove();
        return false;
    }
});

