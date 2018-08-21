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
			$(".Sling-CMS__edit-button[data-sling-cms-action=delete]").click(function(){
				CMSEditor.ui.showModal('/cms/editor/delete.html'+$(this).attr('data-sling-cms-path'));
			});
			$(".Sling-CMS__edit-button[data-sling-cms-action=edit]").click(function(){
				CMSEditor.ui.showModal(
					'/cms/editor/edit.html'+$(this).attr('data-sling-cms-path')+'?editor='+$(this).attr('data-sling-cms-edit'),
					$(this).closest('.Sling-CMS__component').attr('data-sling-cms-title'));
			});
			$(".Sling-CMS__edit-button[data-sling-cms-action=reorder]").click(function(){
				CMSEditor.ui.showModal('/cms/editor/reorder.html'+$(this).attr('data-sling-cms-path'));
			});
			
			// closing the modal
			$(".Sling-CMS__modal-close").click(function(){
				CMSEditor.ui.hideModal();
			});
			window.addEventListener('keypress',function(e){
				if(e.keyCode==27 && CMSEditor.ui.modalDisplayed === true){
					CMSEditor.ui.hideModal();
				}
			});

			var mouseX;
			var mouseY;
			
			function draggable(element) {
				var mouseDown = false;
				
				var elementX = 0;
				var elementY = 0;

				  // mouse button down over the element
				element.addEventListener('mousedown', function(evt){
					console.log('mousedown');
					mouseX = evt.clientX;
					mouseY = evt.clientY;
					mouseDown = true;
				});
				
				var moveComplete = function(evt){
					console.log('mouseup');
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
					console.log('mousemove');
				    var deltaX = event.clientX - mouseX;
				    var deltaY = event.clientY - mouseY;
				    element.style.left = elementX + deltaX + 'px';
				    element.style.top = elementY + deltaY + 'px';
				    return false;
				});
			}
			draggable($(".Sling-CMS__modal-box")[0]);
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
			showModal: function(url, title){
				title = title || '';
				if(CMSEditor.ui.modalDisplayed) {
					CMSEditor.ui.hideModal();
				}
				
				mouseX = 0;
				mouseY = 0;
				
				$(".Sling-CMS__modal-frame-title").text(title);
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