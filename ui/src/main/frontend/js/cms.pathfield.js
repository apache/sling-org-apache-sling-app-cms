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
/* global autoComplete */
Sling.CMS.pathfield = null;

rava.bind('input.pathfield', {
  callbacks: {
    created() {
      const { type } = this.dataset;
      const { base } = this.dataset;

      new autoComplete({ // eslint-disable-line no-new, new-cap
        minChars: 1,
        selector: this,
        source(t, response) {
          let term = t;
          if (term === '/') {
            term = base;
          }
          fetch(
            `/bin/cms/paths?path=${encodeURIComponent(
              term,
            )}&type=${encodeURIComponent(type)}`,
          )
            .then((resp) => resp.json())
            .then((data) => {
              response(data);
            });
        },
      });
    },
  },
});

rava.bind('.search-button', {
  events: {
    click() {
      Sling.CMS.pathfield = this.closest('.field').querySelector('.pathfield');
    },
  },
});

rava.bind('.search-select-button', {
  events: {
    click() {
      const { path } = this.dataset;
      if (Sling.CMS.pathfield instanceof HTMLInputElement) {
        Sling.CMS.pathfield.value = this.dataset.path;
      } else {
        Sling.CMS.pathfield.postMessage(
          {
            action: 'slingcms.setpath',
            path,
          },
          window.location.origin,
        );
      }
      this.closest('.modal').remove();
    },
  },
});

window.addEventListener(
  'message',
  (event) => {
    if (event.data.action === 'slingcms.setpath') {
      Sling.CMS.pathfield.value = event.data.path;
    }
  },
  false,
);
