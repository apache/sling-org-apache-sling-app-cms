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
var info = Sling.getSessionInfo();
if (info.userID !== 'anonymous') {
	var login = document.querySelector('#Login');
	var logout = document.querySelector('#Logout');
	if(login != null && login){
		login.style.display = 'none';
	}
	if(logout != null && logout){
		logout.style.display = 'block';
		logout.insertAdjacentHTML('beforeend', info.userID);
	}
}

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
		util: {
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
				
				$form = $(this);
				var jcrcontent = false;
				$form.find('input').each(function(idx,inp){
					if(inp.name.indexOf('jcr:content') != -1){
						jcrcontent = true;
					}
				});
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
				$.ajax({
					url: $form.attr('action'),
					type: 'POST',
					data: new FormData(this),
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
							});
						} else {
							Sling.CMS.ui.confirmMessage(msg, err,function(){
							});
						}
					}
				});
				return false;
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
	}

	Sling.CMS.ext['repeating'] = {
		decorate: function($ctx){
			$ctx.find('.repeating').each(function(){
				var $rep = $(this);
				$rep.find('.repeating__remove').click(function(){
					var $rem = $(this);
					$rem.parents('.repeating__item').remove();
					return false;
				});
				$rep.find('.repeating__add').click(function(){
					var $div = $('<div/>').html($rep.find('.repeating__template').html());
					Sling.CMS.decorate($div);
					$('.repeating__container').append($div);
					return false;
				});
				
			});
		}
	};
	Sling.CMS.ext['richtext'] = {
		decorate: function($ctx){
			$ctx.find('.richtext').summernote({
			    height: 200
			});
		}
	}

	$(document).ready(function() {
		Sling.CMS.init();
	});
