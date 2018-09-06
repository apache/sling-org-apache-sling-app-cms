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
			},
			fetchModal: function(title, link, path, complete){
				var $modal = $('<div class="modal"><div class="modal-background"></div><div class="modal-card is-draggable"><header class="modal-card-head"><p class="modal-card-title">'+title+'</p><button class="delete" aria-label="close"></button></header><section class="modal-card-body"></section><footer class="modal-card-foot"></footer></div>');
				$('body').append($modal);
				$modal.find('.modal-card-body').load(link + " " +path,function(){
					$modal.addClass('is-active');
					$modal.find('.delete,.close-modal').click(function(){
						$modal.css('display','none').remove();
						return false;
					});
					Sling.CMS.decorate($modal);
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

	Sling.CMS.ext['ajaxform'] = {
		decorate: function($ctx){
			$ctx.find('.Form-Ajax').submit(function(){
				
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
				$form.find('.Form-Ajax__wrapper').attr('disabled', 'disabled');
				$.ajax({
					url: $form.attr('action'),
					type: 'POST',
					data: data,
					processData: false,
					contentType: false,
					dataType: 'json',
					success: function(res,msg){
						if (callback && Sling.CMS.ext[callback]){
							Sling.CMS.ext[callback](res, msg);
						} else {
							Sling.CMS.ext.reload(res, msg);
						}
					},
					error: function(xhr, msg, err){
						if(window.self !== window.top){
							window.top.Sling.CMS.ui.confirmMessage(msg, err,function(){
								$form.find('.Form-Ajax__wrapper').removeAttr('disabled');
							});
						} else {
							Sling.CMS.ui.confirmMessage(msg, err,function(){
								$form.find('.Form-Ajax__wrapper').removeAttr('disabled');
							});
						}
					}
				});
				return false;
			});
		}
	};

	Sling.CMS.ext['draggable'] = {
		decorate: function($ctx) {
			var draggable = function(){
				var element = this;
				var mouseX;
				var mouseY;
				var mouseDown = false;
				var elementX = 0;
				var elementY = 0;

				  // mouse button down over the element
				element.addEventListener('mousedown', function(evt){
					if(document.querySelector('.modal-card-body').contains(evt.target)){
						return;
					}
					mouseX = evt.clientX;
					mouseY = evt.clientY;
					mouseDown = true;
				});
				
				var moveComplete = function(){
					mouseDown = false;
					elementX = parseInt(element.style.left) || 0;
					elementY = parseInt(element.style.top) || 0;
					return false;
				}
				
				element.addEventListener('mouseup', moveComplete);
				document.addEventListener('mouseout', moveComplete);
				
				document.addEventListener('mousemove', function(event) {
					if (!mouseDown) {
						return;
					}
				    var deltaX = event.clientX - mouseX;
				    var deltaY = event.clientY - mouseY;
				    element.style.left = elementX + deltaX + 'px';
				    element.style.top = elementY + deltaY + 'px';
				    return false;
				});
				
			};
			if($ctx.is('.is-draggable')){
				$ctx.each(draggable)
			}
			$ctx.find('.is-draggable').each(draggable);
		}
	};
	
	Sling.CMS.ext['load-versions'] = {
		loaded: false,
		decorate: function($ctx) {
			if(!Sling.CMS.ext['load-versions'].loaded){
				$ctx.find('.load-versions').each(function(){
					var $ctr = $(this);
					var $table = $ctr.closest('.table');
					$.getJSON($ctr.data('url'),function(res){
						$table.dataTable().api().destroy();
						var source   = $('#'+$ctr.data('template')).html();
						var template = Handlebars.compile(source);
						$ctr.append(template(res));
						Sling.CMS.ext['load-versions'].loaded = true;
						Sling.CMS.decorate($ctr.closest('.version-container'));
					});
				});
			}
		}
	};

	//support links which fetch HTML and display a modal
	Sling.CMS.ext['fetch-modal'] = {
		decorate : function($ctx){
			$ctx.find('a.Fetch-Modal').click(function(){
				var $link = $(this);
				$link.attr('disabled', 'disabled');
				Sling.CMS.ui.fetchModal($link.attr('data-title'), encodeURI($link.attr('href')), $link.attr('data-path'), function(){
					$link.removeAttr('disabled');
				});
				return false;
			});
		}
	};
	
	Sling.CMS.ext['getform'] = {
		decorate: function($ctx){
			$ctx.find('.Get-Form').submit(function(){
				var $form = $(this);
				var params = $form.serialize();
				$form.find('.Form-Ajax__wrapper').attr('disabled', 'disabled');
				
				$($form.data('target')).load($form.attr('action') + '?' + params +'  ' + $form.data('load'), function(){
					$form.find('.Form-Ajax__wrapper').removeAttr('disabled');
					Sling.CMS.decorate($($form.data('target')));
				});
				return false;
			});
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

	Sling.CMS.ext['pathfield'] = {
		suggest: function(field, type, base) {
			var xhr;
			new autoComplete({
				minChars: 1,
			    selector: field,
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
		},
		decorate: function($ctx){
			$ctx.find('input.pathfield').each(function(){
				var type = $(this).data('type');
				var base = $(this).data('base');
				Sling.CMS.ext.pathfield.suggest(this, type, base);
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
	
	Sling.CMS.ext['searchselect'] = {
		decorate: function($ctx) {
			$ctx.find('.Search-Select-Button').click(function(evt){
				var $btn = $(evt.target);
				var $active = Sling.CMS.ext['searchbutton'].active;
				$active.val($btn.data('path'));
				$btn.closest('.Modal').remove();
			});
		}
	}
	
	Sling.CMS.ext['searchbutton'] = {
		active: null,
		decorate: function($ctx) {
			$ctx.find('.Search-Button').click(function(evt){
				Sling.CMS.ext['searchbutton'].active = $(evt.target).closest('.Field-Input').find('.Field-Path');
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

	Sling.CMS.ext['taxonomy'] = {
		decorate: function($ctx){
			$ctx.find('.taxonomy').each(function(){
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
			});
			$ctx.find('.taxonomy__item').click(function(){
				$(this).remove();
				return false;
			});
		}
	};
	
	Sling.CMS.ext['toggle-hidden'] = {
		decorate: function($ctx){
			$ctx.find('.toggle-hidden').click(function(){
				$($(this).data('target')).toggleClass('is-hidden');
			});
		}
	};
	
	Sling.CMS.ext['toggle-value'] = {
		decorate: function($ctx) {
			$ctx.find('.toggle-value').each(function(){
				var $tog = $(this);
				$('input[name="'+$tog.data('toggle-source')+'"], select[name="'+$tog.data('toggle-source')+'"]').change(function(){
					if($(this).val() !== $tog.data('toggle-value')){
						$tog.addClass('is-hidden');
					} else {
						$tog.removeClass('is-hidden');
					}
				});
			})
		}
	};
	
	Sling.CMS.ext['file-upload'] = {
		decorate: function($ctx) {
			$ctx.find('.file').on('change', "input", function(){
				var node = $(this);
				node.parent().find('.file-name').text(this.files[0].name);
			});
		}
	};

	$(document).ready(function() {
		Sling.CMS.init();
	});
