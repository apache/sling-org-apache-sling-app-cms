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
/* global wysihtml, wysihtmlParserRules */

rava.bind('.file', {
  callbacks: {
    created() {
      const field = this;
      const close = field.closest('form').querySelector('a.close');

      function setProgress(m, progress) {
        const meter = m;
        meter.innerText = `${Math.round(progress)}%`;
        meter.value = Math.round(progress);
      }
      function uploadFile(meter, action, file) {
        const formData = new FormData();

        formData.append('*', file);
        formData.append('*@TypeHint', 'sling:File');

        const xhr = new XMLHttpRequest();
        xhr.upload.addEventListener('loadstart', () => {
          setProgress(meter, 0);
        }, false);
        xhr.upload.addEventListener('progress', (event) => {
          const percent = (event.loaded / event.total) * 100;
          setProgress(meter, percent);
        }, false);
        xhr.upload.addEventListener('load', () => {
          meter.classList.add('is-info');
        }, false);
        xhr.addEventListener('readystatechange', (event) => {
          let status; let text; let
            readyState;
          try {
            readyState = event.target.readyState;
            text = event.target.responseText;
            status = event.target.status;
          } catch (e) {
            meter.classList.add('is-danger');
          }
          if (readyState === 4) {
            meter.classList.remove('is-info');
            if (status === 200 && text) {
              meter.classList.add('is-success');
            } else {
              meter.classList.add('is-danger');
              console.warn('Failed to upload %s, recieved message %s', file.name, text); // eslint-disable-line no-console
            }
          }
        }, false);
        xhr.open('POST', action, true);
        xhr.send(formData);
      }
      function handleFile(scope, file) {
        const it = document.createElement('div');
        const ctr = scope.closest('.control').querySelector('.file-item-container');
        let meter = null;
        it.innerHTML = document.querySelector('.file-item-template').innerHTML;
        meter = it.querySelector('.progress');
        it.querySelector('.file-item-name').innerText = file.name;
        ctr.classList.remove('is-hidden');
        ctr.appendChild(it);
        uploadFile(meter, scope.closest('form').action, file);
      }

      field.addEventListener('dragover', (event) => {
        event.preventDefault();
      }, false);
      field.addEventListener('dragenter', (event) => {
        event.preventDefault();
        field.classList.add('is-primary');
      }, false);
      field.addEventListener('dragleave', (event) => {
        event.preventDefault();
        field.classList.remove('is-primary');
      }, false);
      field.addEventListener('drop', (event) => {
        event.preventDefault();
        field.classList.remove('is-primary');
        if (event.dataTransfer.items) {
          const { items } = event.dataTransfer;
          for (let i = 0; i < items.length; i++) { // eslint-disable-line no-plusplus
            if (items[i].kind === 'file') {
              handleFile(field, items[i].getAsFile());
            }
          }
        } else {
          const { files } = event.dataTransfer;
          for (let i = 0; i < files.length; i++) { // eslint-disable-line no-plusplus
            handleFile(field, files[i]);
          }
        }
      }, false);
      field.closest('form').querySelector('button[type=submit]').remove();
      close.innerText = 'Done';
      close.addEventListener('click', () => {
        window.Sling.CMS.ui.reloadContext();
      });
      field.querySelector('input').addEventListener('change', (event) => {
        const { files } = event.target;
        for (let i = 0; i < files.length; i++) { // eslint-disable-line no-plusplus
          handleFile(field, files[i]);
        }
      });
    },
  },
});

/* Support for updating the namehint when creating a component */
rava.bind('.namehint', {
  callbacks: {
    created() {
      const field = this;
      this.closest('.Form-Ajax').querySelector('select[name="sling:resourceType"]').addEventListener('change', (evt) => {
        const resourceType = evt.target.value.split('/');
        field.value = resourceType[resourceType.length - 1];
      });
    },
  },
});

/* Support for repeating form fields */
rava.bind('.repeating', {
  callbacks: {
    created() {
      const ctr = this;
      this.querySelectorAll('.repeating__add').forEach((el) => {
        el.addEventListener('click', (event) => {
          event.stopPropagation();
          event.preventDefault();
          const node = ctr.querySelector('.repeating__template > .repeating__item').cloneNode(true);
          ctr.querySelector('.repeating__container').appendChild(node);
        });
      });
    },
  },
});
rava.bind('.repeating__item', {
  events: {
    ':scope .repeating__remove': {
      click(event) {
        event.stopPropagation();
        event.preventDefault();
        this.remove();
      },
    },
  },
});

rava.bind('.rte', {
  callbacks: {
    created() {
      new wysihtml.Editor(this.querySelector('.rte-editor'), { // eslint-disable-line no-new, new-cap
        toolbar: this.querySelector('.rte-toolbar'),
        parserRules: wysihtmlParserRules,
      });
    },
  },
});
