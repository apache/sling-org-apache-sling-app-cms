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
nomnom.decorate("a.Fetch-Modal", class {
    
    "click::handler"() {
        this.setAttribute("disabled",'disabled');
        this.getModal(this.getAttribute('data-title'), encodeURI(this.getAttribute('href')), this.getAttribute('data-path'));
    }
  
    getModal(title, link, path, complete) {
        var button = this;
        var $modal = $('<div class="modal"><div class="modal-background"></div><div class="modal-card is-draggable"><header class="modal-card-head"><p class="modal-card-title">'+title+'</p><button class="delete" aria-label="close"></button></header><section class="modal-card-body"></section><footer class="modal-card-foot"></footer></div>');
        $('body').append($modal);
            $modal.find('.modal-card-body').load(link + " " +path,function(){
            $modal.addClass('is-active');
            $modal.find('.delete,.close-modal').click(function(){
                $modal.css('display','none').remove();
                return false;
            });
            button.removeAttribute("disabled");
        });
        return $modal;
    }
});
