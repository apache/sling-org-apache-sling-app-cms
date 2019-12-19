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


window.Sling = window.Sling || {};
const {
  Sling,
} = window;
window.Sling.CMS = {
  handlers: {
    handledelete(res, msg) {
      if (window.location.pathname.indexOf(res.path) !== -1) {
        window.top.Sling.CMS.ui.confirmMessage(msg, res.title, () => {
          window.location = '/cms';
        });
      } else {
        Sling.CMS.ui.confirmReload(res, msg);
      }
    },
    handlemove(res, msg) {
      const changes = res.changes[0];
      if (changes.type === 'moved' && window.location.pathname.indexOf(changes.argument[0]) !== -1) {
        window.top.Sling.CMS.ui.confirmMessage(msg, res.title, () => {
          window.location = window.location.href.replace(changes.argument[0], changes.argument[1]);
        });
      } else {
        Sling.CMS.ui.confirmReload(res, msg);
      }
    },
    handleugc(res, msg) {
      Sling.CMS.ui.confirmMessage(msg, res.title, () => {
        window.location = `/cms/usergenerated/content.html${res.parentLocation}`;
      });
    },
  },
  ui: {
    confirmMessage(title, message, complete) {
      const modal = document.createElement('div');
      modal.innerHTML = `<div class="modal-background"></div><div class="is-draggable modal-content"><div class="box"><h3 class="modal-title">${title}</h3><p>${message}</p><br/><button type="button" class="close-modal button is-primary">OK</button></div></div><button class="modal-close is-large" aria-label="close"></button>`;
      document.body.appendChild(modal);
      modal.classList.add('modal');
      modal.classList.add('is-active');
      modal.querySelectorAll('.modal-close,.close-modal,.modal-background').forEach((closer) => {
        closer.addEventListener('click', () => {
          modal.remove();
          complete();
        });
      });
      modal.querySelector('.delete,.close-modal').focus();
      return modal;
    },
    confirmReload(res, msg) {
      if (window.self !== window.top) {
        window.top.Sling.CMS.ui.confirmMessage(msg, res.title, () => {
          window.top.location.reload();
        });
      } else {
        Sling.CMS.ui.confirmMessage(msg, res ? res.title : '', () => {
          Sling.CMS.ui.reloadContext();
        });
      }
    },
    confirmReloadComponent(res, msg, path) {
      window.top.Sling.CMS.ui.confirmMessage(msg, res.title, () => {
        const modal = window.top.Sling.CMS.ui.loaderModal('Refreshing...');
        window.parent.window.CMSEditor.ui.reloadComponent(path, () => {
          modal.remove();
        });
      });
    },
    loaderModal(message) {
      const modal = document.createElement('div');
      modal.classList.add('modal');
      modal.innerHTML = `<div class="modal-background"></div><div class="modal-content"><div class="box"><h3>${message || 'Loading...'}</h3><div class="loader is-loading"></div></div></div>`;
      document.querySelector('body').appendChild(modal);
      modal.classList.add('is-active');
      return modal;
    },
    reloadContext() {
      // close all existing modals
      document.querySelectorAll('.modal').forEach((modal) => {
        modal.remove();
      });
      // reset the actions
      document.querySelectorAll('.actions-target *').forEach((child) => {
        child.remove();
      });
      const containers = document.querySelectorAll('.reload-container');
      const modal = Sling.CMS.ui.loaderModal('Refreshing...');
      let count = containers.length;
      if (count !== 0) {
        containers.forEach(async (container) => {
          let link = container.dataset.path;
          if (link.indexOf('?') === -1) {
            link += `?tstamp=${Date.now()}`;
          } else {
            link += `&tstamp=${Date.now()}`;
          }
          const response = await fetch(link);
          if (Sling.CMS.utils.ok(response)) {
            const responseText = await response.text();
            const tmp = document.createElement('div');
            tmp.innerHTML = responseText;
            container.replaceWith(tmp.querySelector('.reload-container'));
            tmp.remove();
            count -= 1;
            if (count === 0) {
              modal.remove();
            }
          }
        });
      } else {
        window.location.reload();
      }
    },
  },
  utils: {
    form2Obj(form) {
      const data = {};
      form.querySelectorAll('input,textarea,select').forEach((f) => {
        if (!f.disabled && f.name) {
          data[f.name] = f.value;
        }
      });
      return data;
    },
    ok(response) {
      let { CMS } = Sling;
      if (window.self !== window.top) {
        CMS = window.top.Sling.CMS;
      }
      if (response.redirected && response.url.indexOf('/system/sling/form/login?resource=') !== -1) {
        CMS.ui.confirmMessage('301', 'Not logged in, please login again', () => {
          window.location = `/system/sling/form/login?resource=${encodeURIComponent(window.location.pathname)}`;
        });
        return false;
      }
      if (!response.ok) {
        CMS.ui.confirmMessage(response.status, response.statusText, () => {
          window.location = response.url;
        });
        return false;
      }
      return true;
    },
  },
};

if (window.performance
  && window.performance.navigation.type
  === window.performance.navigation.TYPE_BACK_FORWARD) {
  Sling.CMS.ui.reloadContext();
}

window.onbeforeunload = () => {
  if (document.querySelector('.modal form')) {
    return 'Are you sure you want to leave this page?';
  }
  return null;
};

if (document.querySelector('.breadcrumb .is-active') !== null) {
  const itemTitle = document.querySelector('.breadcrumb .is-active').textContent.trim();
  if (itemTitle.length > 0) {
    document.title = `${itemTitle} :: ${document.title}`;
  }
}

rava.bind('.sling-cms-include-config', {
  callbacks: {
    created() {
      const container = this;
      async function loadContent() {
        const srcEl = document.querySelector(container.dataset.source);
        const {
          config,
        } = srcEl.selectedOptions[0].dataset;
        if (config) {
          const response = await fetch(config + container.closest('form').attributes.action.value);
          if (Sling.CMS.utils.ok(response)) {
            const markup = await response.text();
            container.innerHTML = markup;
          }
        }
      }
      document.querySelector(container.dataset.source).addEventListener('change', loadContent);
      loadContent();
    },
  },
});
