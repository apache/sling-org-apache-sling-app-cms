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

rava.bind('.page-properties-container', {
  callbacks: {
    created() {
      const container = this;
      const wrapper = container.closest('.form-wrapper');

      function removeProperty(obj, propertyToDelete) {
        Object.keys(obj).forEach((key) => {
          if (key === propertyToDelete) {
            delete obj[key]; // eslint-disable-line no-param-reassign
          } else if (typeof obj[key] === 'object') {
            removeProperty(obj[key], propertyToDelete);
          }
        });
      }

      async function getPageTemplate(pageConfig, cb) {
        const response = await fetch(`${pageConfig}.infinity.json`);
        if (Sling.CMS.utils.ok(response)) {
          const result = await response.json();
          let template;
          if (typeof result.template === 'string') {
            // String template
            template = result.template;
          } else if (typeof result.template === 'object') {
            // Json template
            removeProperty(result.template, 'jcr:created');
            removeProperty(result.template, 'jcr:createdBy');
            template = JSON.stringify(result.template);
          }
          cb(template);
        }
      }
      async function handleChange() {
        const sourceSelect = this;
        const config = this.value;
        sourceSelect.disabled = true;
        container.innerHTML = '';
        const response = await fetch(container.dataset.path + config);
        if (Sling.CMS.utils.ok(response)) {
          const formHtml = await response.text();
          getPageTemplate(config, (source) => {
            const template = Handlebars.compile(source);
            function updateContent() {
              if (!wrapper.disabled) {
                const data = Sling.CMS.utils.form2Obj(container.closest('form'));
                document.querySelector('input[name=":content"]').value = template(data);
              }
            }
            container.innerHTML = formHtml;
            document.querySelectorAll('input,textarea,select').forEach((el) => {
              el.addEventListener('change', updateContent);
            });
            container.closest('form').addEventListener('submit', updateContent);
            sourceSelect.disabled = false;
          });
        }
      }
      document.querySelector(container.dataset.source).addEventListener('change', handleChange);
    },
  },
});
