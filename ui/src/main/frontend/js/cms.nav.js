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


rava.bind('.navbar-burger', {
  events: {
    click() {
      const target = document.querySelector(this.dataset.target);
      target.classList.toggle('is-active');
      this.classList.toggle('is-active');
    },
  },
});

rava.bind('.layout-switch select', {
  events: {
    change() {
      window.location = this.value;
    },
  },
});

rava.bind('.contentnav', {
  callbacks: {
    created() {
      const urlParams = new URLSearchParams(window.location.search);
      const resourceParam = urlParams.get('resource');
      const searchParam = urlParams.get('search');
      const cnav = this;
      const search = document.querySelector('.contentnav-search input[name=search]');
      function attrContains(ctx, attr) {
        let matches = false;
        const value = search.value.toLowerCase();
        ctx.querySelectorAll(`*[${attr}]`).forEach((it) => {
          if (it.getAttribute(attr).indexOf(value) !== -1) {
            matches = true;
          }
        });
        return matches;
      }
      function filter(event) {
        if (event) {
          event.stopPropagation();
          event.preventDefault();
        }
        const value = search.value.toLowerCase();
        cnav.querySelectorAll('.contentnav__item').forEach((item) => {
          if (item.innerText.toLowerCase().indexOf(value) === -1
            && !attrContains(item, 'title')
            && !attrContains(item, 'data-value')) {
            item.classList.add('is-hidden');
          } else {
            item.classList.remove('is-hidden');
          }
        });
      }
      if (search) {
        search.addEventListener('keyup', filter);
        search.addEventListener('change', filter);
      }
      if (resourceParam) {
        cnav.querySelectorAll('.contentnav__item').forEach((item) => {
          if (item.querySelector(`*[data-value="${resourceParam}"]`)) {
            item.classList.remove('is-hidden');
            item.click();
          } else {
            item.classList.add('is-hidden');
          }
        });
        document.querySelector('.contentnav-search input[name=search]').value = resourceParam;
      } else if (searchParam) {
        document.querySelector('.contentnav-search input[name=search]').value = searchParam;
        filter();
      }
    },
  },
});
rava.bind('.contentnav .contentnav__item', {
  events: {
    click() {
      this.closest('.contentnav').querySelectorAll('.contentnav__item.is-selected').forEach((tr) => {
        tr.classList.remove('is-selected');
      });
      this.classList.add('is-selected');
      document.querySelector('.actions-target').innerHTML = this.querySelector('.cell-actions').innerHTML;
    },
    dblclick() {
      if (this.querySelector('.item-link')) {
        window.location = this.querySelector('.item-link').href;
      }
    },
  },
});
