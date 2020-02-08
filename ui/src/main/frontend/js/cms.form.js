/*
w * Licensed to the Apache Software Foundation (ASF) under one
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


rava.bind('.Form-Ajax', {
  events: {
    ':scope .close': {
      click() {
        if (window.parent && window.parent.window && window.parent.window.CMSEditor) {
          window.parent.window.CMSEditor.ui.hideModal();
        }
      },
    },
    submit(event) {
      event.preventDefault();
      const form = this;
      async function submitForm() {
        let jcrcontent = false;
        const { callback } = form.dataset;
        const formData = new FormData(form);
        form.querySelectorAll('input,select,textarea').forEach((el) => {
          if (el.name.indexOf('jcr:content') !== -1) {
            jcrcontent = true;
          }
        });
        if (form.dataset.addDate && !form.querySelector('input[name="jcr:content/jcr:lastModified"]')) {
          const dateContainer = document.createElement('div');
          if (jcrcontent) {
            dateContainer.innerHTML = '<input type="hidden" name="jcr:content/jcr:lastModified" />'
                              + '<input type="hidden" name="jcr:content/jcr:lastModifiedBy" />'
                              + '<input type="hidden" name="jcr:content/jcr:created" />'
                              + '<input type="hidden" name="jcr:content/jcr:createdBy" />';
          } else {
            dateContainer.innerHTML = '<input type="hidden" name="jcr:lastModified" />'
                              + '<input type="hidden" name="jcr:lastModifiedBy" />'
                              + '<input type="hidden" name="jcr:created" />'
                              + '<input type="hidden" name="jcr:createdBy" />';
          }
          form.appendChild(dateContainer);
        }
        const response = await fetch(form.action, {
          method: 'POST',
          body: formData,
          cache: 'no-cache',
          headers: {
            Accept: 'application/json',
          },
        });
        if (Sling.CMS.utils.ok(response)) {
          const res = await response.json();
          form.querySelector('.form-wrapper').disabled = false;
          if (callback && Sling.CMS.handlers[callback]) {
            Sling.CMS.handlers[callback](res, 'success');
          } else if (window.parent.window.CMSEditor) {
            let reloadParent = false;
            let { path } = res;
            res.changes.forEach((change) => {
              if (change.type !== 'modified') {
                reloadParent = true;
              }
            });
            if (reloadParent) {
              const pathArr = path.split('/');
              pathArr.pop();
              path = pathArr.join('/');
            }
            Sling.CMS.ui.confirmReloadComponent(res, 'success', path);
          } else {
            Sling.CMS.ui.confirmReload(res, 'success');
          }
        } else {
          form.querySelector('.form-wrapper').disabled = false;
        }
      }
      submitForm();
      form.querySelector('.form-wrapper').disabled = true;
    },
  },
});

rava.bind('.get-form', {
  events: {
    submit(event) {
      event.preventDefault();
      event.stopPropagation();
      const modal = Sling.CMS.ui.loaderModal('Loading...');
      const form = this;
      const wrapper = form.querySelector('.form-wrapper');
      async function doGet(url) {
        const request = await fetch(url);
        if (Sling.CMS.utils.ok(request)) {
          const tmp = document.createElement('div');
          tmp.innerHTML = await request.text();
          const target = document.querySelector(form.dataset.target);
          target.innerHTML = tmp.querySelector(form.dataset.load).innerHTML;
          tmp.remove();
          if (wrapper) {
            wrapper.disabled = false;
          }
          modal.remove();
        } else {
          modal.remove();
        }
      }
      const url = `${form.action}?${new URLSearchParams(new FormData(form)).toString()}`;
      if (wrapper) {
        wrapper.disabled = true;
      }
      doGet(url);
    },
  },
});

rava.bind('.suffix-form', {
  events: {
    submit(event) {
      event.preventDefault();
      const suffix = this.querySelector('input[name=suffix]').value;
      const path = this.action;
      window.location = path + suffix;
      return false;
    },
  },
});
