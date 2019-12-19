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

rava.bind('.toggle-hidden', {
  events: {
    click() {
      const target = document.querySelectorAll(this.dataset.target);
      target.forEach((el) => {
        el.classList.toggle('is-hidden');
      });
    },
  },
});

rava.bind('.toggle-value', {
  callbacks: {
    created() {
      const source = this.getAttribute('data-toggle-source');
      const selector = `input[name="${source}"], select[name="${source}"]`;
      const toggle = this;
      const sourceEl = document.querySelector(selector);
      function handleChange() {
        if (this.value !== toggle.dataset.toggleValue) {
          toggle.classList.add('is-hidden');
        } else {
          toggle.classList.remove('is-hidden');
        }
      }
      if (sourceEl) {
        sourceEl.addEventListener('change', handleChange);
      }
    },
  },
});
