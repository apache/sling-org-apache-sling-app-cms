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

rava.bind('a.Fetch-Modal', {
  events: {
    click(event) {
      event.preventDefault();
      this.disabled = true;
      this.getModal(this.getAttribute('data-title'), this.getAttribute('href'), this);
    },
  },
  methods: {
    getModal(title, link, button) {
      const modal = Sling.CMS.ui.loaderModal();
      async function loadModal() {
        const controller = new AbortController();
        const request = await fetch(link, {
          signal: controller.signal,
        });
        if (Sling.CMS.utils.ok(request)) {
          const responseText = await request.text();
          modal.innerHTML = responseText;
        }
        modal.querySelector('.modal-background').addEventListener('click', () => {
          controller.abort();
          button.removeAttribute('disabled');
        });
      }
      if (window.self !== window.top && window.parent.parent) {
        window.parent.parent.postMessage({
          action: 'slingcms.openmodal',
          url: link,
          title,
        }, window.location.origin);
        button.removeAttribute('disabled');
        modal.remove();
      } else {
        loadModal();
      }
    },
  },
});

rava.bind('.close,.modal-close,.close-modal,.modal-background', {
  events: {
    click(evt) {
      const modal = this.closest('.modal');
      if (modal) {
        evt.preventDefault();
        evt.stopPropagation();
        modal.remove();
      }
    },
  },
});

window.addEventListener('message', async (event) => {
  if (event.data.action === 'slingcms.openmodal') {
    Sling.CMS.pathfield = event.source;
    const modal = Sling.CMS.ui.loaderModal();
    const controller = new AbortController();
    const request = await fetch(event.data.url, {
      signal: controller.signal,
    });
    if (Sling.CMS.utils.ok(request)) {
      const responseText = await request.text();
      modal.innerHTML = responseText;
    }
    modal.querySelector('.modal-background').addEventListener('click', () => {
      controller.abort();
    });
  }
}, false);
