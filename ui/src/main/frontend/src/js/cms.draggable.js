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

nomnom.decorate(".is-draggable", {

    data: {
            mouseX : 0,
            mouseY : 0,
            mouseDown : false,
            elementX : 0,
            elementY : 0
    },

    moveComplete: function(data){
      data.mouseDown = false;
      data.elementX = parseInt(this.style.left) || 0;
      data.elementY = parseInt(this.style.top) || 0;
      return false;
    },

    events : {
        mousedown:function(event, data) {
            if(event.target.matches('.modal-card-body *')){
                return;
            }
            data.mouseX = event.clientX;
            data.mouseY = event.clientY;
            data.mouseDown = true;
        },
        document : {
            mouseup : function(event,data){
                if (data.mouseDown === true){
                    this.moveComplete(data);
                  }
            },
            mousemove: function(event, data) {
                if (data.mouseDown === false) {
                    return false;
                 }
                 var deltaX = event.clientX - data.mouseX;
                 var deltaY = event.clientY - data.mouseY;
                 this.style.left = data.elementX + deltaX + 'px';
                 this.style.top = data.elementY + deltaY + 'px';
                 return false;
              }
        }
    }
});