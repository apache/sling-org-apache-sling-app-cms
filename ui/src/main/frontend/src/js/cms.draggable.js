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

nomnom.decorate(".is-draggable", class {
  
    initCallback() {
        this.dragData = {
            mouseX : 0,
            mouseY : 0,
            mouseDown : false,
            elementX : 0,
            elementY : 0
          };
    }
    
    
    moveComplete(){
      this.dragData.mouseDown = false;
      this.dragData.elementX = parseInt(this.style.left) || 0;
      this.dragData.elementY = parseInt(this.style.top) || 0;
      return false;
    }
    
  
  "mousedown::"(event) {
    if(event.target.matches('.modal-card-body *')){
      return;
    }
    this.dragData.mouseX = event.clientX;
    this.dragData.mouseY = event.clientY;
    this.dragData.mouseDown = true;
  }
  
  "mouseup::document"() {
    if (this.dragData.mouseDown === true){
      this.moveComplete();
    }
  }

  "mousemove::document"(event){
      if (this.dragData.mouseDown === false) {
         return false;
      }
      var deltaX = event.clientX - this.dragData.mouseX;
      var deltaY = event.clientY - this.dragData.mouseY;
      this.style.left = this.dragData.elementX + deltaX + 'px';
      this.style.top = this.dragData.elementY + deltaY + 'px';
      return false;
  }
  
});