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
if(!window.CMSEditor){
	window.CMSEditor = {
		init: function(){
			$(".Sling-CMS__edit-button[data-sling-cms-action=add]").click(function(){
				CMSEditor.ui.showModal('/cms/editor/add.html'+$(this).attr('data-sling-cms-path')+'?availableTypes='+$(this).data('sling-cms-available-types'));
			});
			$(".Sling-CMS__edit-button[data-sling-cms-action=add-named]").click(function(){
				CMSEditor.ui.showModal('/cms/editor/add-named.html'+$(this).attr('data-sling-cms-path')+'?availableTypes='+$(this).data('sling-cms-available-types'));
			});
			$(".Sling-CMS__edit-button[data-sling-cms-action=delete]").click(function(){
				CMSEditor.ui.showModal('/cms/editor/delete.html'+$(this).attr('data-sling-cms-path'));
			});
			$(".Sling-CMS__edit-button[data-sling-cms-action=edit]").click(function(){
				CMSEditor.ui.showModal('/cms/editor/edit.html'+$(this).attr('data-sling-cms-path'));
			});
			$(".Sling-CMS__edit-button[data-sling-cms-action=editpage]").click(function(){
				CMSEditor.ui.showModal('/cms/editor/edit.html'+$(this).attr('data-sling-cms-path'));
			});
			$(".Sling-CMS__edit-button[data-sling-cms-action=moveup]").click(function(){
				CMSEditor.ui.showModal('/cms/editor/moveup.html'+$(this).attr('data-sling-cms-path'));
			});
			$(".Sling-CMS__edit-button[data-sling-cms-action=movedown]").click(function(){
				CMSEditor.ui.showModal('/cms/editor/movedown.html'+$(this).attr('data-sling-cms-path'));
			});
			
			// closing the modal
			$(".Sling-CMS__modal-close").click(function(){
				CMSEditor.ui.hideModal();
			});
			$(".Sling-CMS__modal-background").click(function(){
				CMSEditor.ui.hideModal();
			});
			$(document).keypress(function(e){
				if(e.keyCode==27 && CMSEditor.ui.modalDisplayed===true){
					CMSEditor.ui.hideModal();
				} 
			});  
		},
		ui: {
			modalDisplayed: false,
			hideModal: function() {
				if(CMSEditor.ui.modalDisplayed) {
					$(".Sling-CMS__modal-background").hide();
					$(".Sling-CMS__modal-box").hide();
					CMSEditor.ui.modalDisplayed = false;
				}
			},
			showModal: function(url){
				if(CMSEditor.ui.modalDisplayed) {
					CMSEditor.ui.hideModal();
				}
				
				$(".Sling-CMS__modal-box iframe").attr('src',url);
				$(".Sling-CMS__modal-background").show();
				$(".Sling-CMS__modal-box").show();
				
				CMSEditor.ui.modalDisplayed = true;
			}
		}
	}
	
	if (document.readyState === 'complete') {
		CMSEditor.init();
	} else {
		document.addEventListener('DOMContentLoaded',CMSEditor.init,false);
	}
}