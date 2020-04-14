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

rava.bind('.labelfield', {
  events: {
    '.labelfield__add, .labelfield__add *': {
      click(event) {
        event.preventDefault();
        event.stopPropagation();
        const context = this;
        const tmp = document.createElement('span');
        const val = context.querySelector('.labelfield__field input').value;
        let found = false;
        const title = context.querySelector(`option[value="${val}"]`).innerText;
        tmp.innerHTML = context.querySelector('.labelfield__template').innerHTML;
        context.querySelectorAll('.labelfield__item input').forEach((el) => {
          if (el.value === val) {
            found = true;
          }
        });
        if (found) {
          return;
        }
        tmp.querySelector('input').value = val;
        tmp.querySelector('.labelfield__item').title = val;

        if (title !== '') {
          tmp.querySelector('.labelfield__title').innerText = title;
          tmp.childNodes.forEach(c => {
            const child = c.cloneNode(true);
            this.closest('.labelfield').querySelector('.labelfield__container').appendChild(child);
          });
          tmp.remove();
          context.querySelector('.labelfield__field input').value = '';
        }
      },
    },
  },
});

rava.bind('.labelfield__item, .labelfield__item *', {
  events: {
    click(event) {
      event.preventDefault();
      event.stopPropagation();
      this.closest('.labelfield__item').remove();
    },
  },
});
