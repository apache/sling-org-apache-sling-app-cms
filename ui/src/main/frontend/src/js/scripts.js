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
			alert: function(level, message){
				$alert = $('<div class="Alert Alert-'+level+'">'+message+'</div>');
				$('.main').prepend($alert);
				setTimeout(function(){
					$alert.remove();
				}, 10000);
				window.scrollTo(0, 0);
			},
			confirmMessage: function(title, message, cb){
				var $modal = $('<div class="Modal"><div class="Modal-Content"><div class="Modal-Header">'+title+'</div><div class="Modal-Body">'+message+'</div><div class="Modal-Footer"><button type="button" class="Modal-Close">OK</button></div></div>');
				$('body').append($modal);
				$modal.css('display','block');
				Sling.CMS.decorate($modal);
				$modal.find('.Modal-Close').click(function(){
					$modal.css('display','none').remove();
					cb();
				});
				return $modal;
			},
			fetchModal: function(title, link, path, complete){
				var $modal = $('<div class="Modal"><div class="Modal-Content"><div class="Modal-Header">'+title+'<button type="button" class="Modal-Close Pull-Right">x</button></div><div class="Modal-Body"></div></div>');
				$('body').append($modal);
				$modal.find('.Modal-Body').load(link + " " +path,function(){
					$modal.css('display','block');
					Sling.CMS.decorate($modal);
					$modal.find('.Modal-Close').click(function(){
						$modal.css('display','none').remove();
						return false;
					});
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
	}

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
	}

	Sling.CMS.ext['ajaxform'] = {
		decorate: function($ctx){
			$ctx.find('.Form-Ajax').submit(function(){
				
				var $form = $(this);
				var $inputs = $form.find('input,select,textarea,button');
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

	Sling.CMS.ext['content-filter'] = {
		decorate: function($ctx) {
			var filterContent = function(){
				var term = $('.content-filter input[type=text]').val().toLowerCase();
				$('.sortable tbody tr').each(function(idx,el){
					if($(el).text().toLowerCase().indexOf(term) !== -1){
						$(el).show();
					} else {
						$(el).hide();
					}
				});
			}
			$('.content-filter').submit(function(){
				filterContent();
				return false;
			});
			$('.content-filter input[type=text]').keyup(filterContent).change(filterContent);
		}
	};
	
	Sling.CMS.ext['content-sort'] = {
		decorate: function($ctx) {
			$ctx.find('.sortable').each(function(){
				var $table = $(this);
				$table.find('.sortable__header').click(function() {
					var idx = Array.from(this.parentNode.children).indexOf(this);
					var $h = $(this);
					var sortStatus = 1;
					if($h.data('sort-status')){
						sortStatus = parseInt($h.data('sort-status'),10);
					}
					var name = $h.data('attribute');
				    var list = $table.find(".sortable__row").get();
				    list.sort(function(rowa, rowb) {
				    		var vala = null;
				    		var $ela = $($(rowa).find('td')[idx]);
				    		if($ela.data('sort-value')){
				    			vala = $ela.data('sort-value');
				    		} else {
				    			vala = $.trim($ela.text()).toLowerCase();
				    		}
				    		var valb = null;
				    		var $elb = $($(rowb).find('td')[idx]);
				    		if($elb.data('sort-value')){
				    			valb = $elb.data('sort-value');
				    		} else {
				    			valb = $.trim($elb.text()).toLowerCase();
				    		}
				    		$h.data('sort-status', sortStatus * -1);
				        return vala.localeCompare(valb) * sortStatus;
				    });
				    for (var i = 0; i < list.length; i++) {
				        list[i].parentNode.appendChild(list[i]);
				    }
				});
			});
		}
	}

	Sling.CMS.ext['fetch-json'] = {
		decorate: function($ctx) {
			$ctx.find('.fetch-json').each(function(){
				$ctr = $(this);
				$.getJSON($ctr.data('url'),function(res){
					var source   = $('#'+$ctr.data('template')).html();
					var template = Handlebars.compile(source);
					$ctr.append(template(res));
					Sling.CMS.decorate($ctr);
				});
			});
		}
	}

	Sling.CMS.ext['includeconfig'] = {
		decorate: function($ctx){
			$ctx.find('.Sling-CMS__include-config').each(function(){
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
				$($ctr.data('source')).change(function(){
					var config = $(this).val();
					$ctr.load($ctr.data('path')+config, function(){
						var source   = $('#content-template').html();
						var template = Handlebars.compile(source);
						var updateContent = function(){
							var data = Sling.CMS.utils.form2Obj($ctr.parents('form'));
							$('input[name=":content"]').val(template(data));
						}
						$ctr.find('input,textarea,select').change(updateContent);
						$ctr.parents('form').submit(updateContent);
						Sling.CMS.decorate($ctr.children());
					});
				});
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
				dialogsInBody: true,
				height: 200,
			    onCreateLink: function (url) {
			        return url;
			    },
			    callbacks: {
			    		onDialogShown: function(e){
			    			$('.note-link-url').attr('list','richtext-pages');
			    			$('.note-image-url').attr('list','richtext-images');
			    		}
			    }
			}).on('summernote.dialog.shown', function(e) {
			  console.log('Dialog shown!');
			}).on('summernote.keydown', function(we, e) {
				  console.log('Key is downed:', e.keyCode);
			});
;
		}
	}

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
			$ctx.find('.Toggle-Hidden').click(function(){
				$($(this).data('target')).toggleClass('Hide');
			});
		}
	}
	
	Sling.CMS.ext['toggle-value'] = {
		decorate: function($ctx) {
			$ctx.find('.toggle-value').each(function(){
				var $tog = $(this);
				$('input[name="'+$tog.data('toggle-source')+'"], select[name="'+$tog.data('toggle-source')+'"]').change(function(){
					if($(this).val() !== $tog.data('toggle-value')){
						$tog.addClass('hide');
					} else {
						$tog.removeClass('hide');
					}
				});
			})
		}
	}

	$(document).ready(function() {
		Sling.CMS.init();
	});
