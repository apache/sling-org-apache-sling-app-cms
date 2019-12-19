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
const mousepos = {
  mouseX: 0,
  mouseY: 0,
  mouseDown: false,
  elementX: 0,
  elementY: 0,
};
rava.bind('.is-draggable', {
  methods: {
    moveComplete() {
      mousepos.mouseDown = false;
      mousepos.elementX = parseInt(this.style.left, 10) || 0;
      mousepos.elementY = parseInt(this.style.top, 10) || 0;
      return false;
    },
  },
  events: {
    ':root': {
      mouseup() {
        if (mousepos.mouseDown === true) {
          this.moveComplete();
        }
      },
      mousemove(event) {
        if (mousepos.mouseDown === false) {
          return false;
        }
        const deltaX = event.clientX - mousepos.mouseX;
        const deltaY = event.clientY - mousepos.mouseY;
        this.style.left = `${mousepos.elementX + deltaX}px`;
        this.style.top = `${mousepos.elementY + deltaY}px`;
        return false;
      },
    },
    mousedown(event) {
      if (!event.target.matches('.modal-title, .modal-title *')) {
        return;
      }
      mousepos.mouseX = event.clientX;
      mousepos.mouseY = event.clientY;
      mousepos.mouseDown = true;
    },
  },
});
