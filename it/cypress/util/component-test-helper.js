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

const { v4: uuid } = require("uuid");

const { sendPost } = require("./test-helper");

export const COMPONENT_SUBPATH = "/jcr:content/container/component";

export const XSS_PAYLOAD = '"/><\x3Cscript>javascript:alert(1)</script>';

/**
 * Creates a page with a single component in it
 * @param {string} prefix the test prefix
 * @param {*} componentContent the JSON content for the component
 * @returns the path to the page
 */
export function createComponentPage(prefix, componentContent) {
  const id = `${prefix}-${uuid()}`;
  const content = {
    "jcr:primaryType": "sling:Page",
    "jcr:content": {
      "jcr:primaryType": "nt:unstructured",
      "jcr:title": `IT - ${prefix}`,
      "sling:template": "/conf/asf/site/templates/base-page",
      "sling:resourceType": "reference/components/pages/base",
      published: true,
      container: {
        "jcr:primaryType": "nt:unstructured",
        component: componentContent,
      },
    },
  };
  const url = `/content/apache/sling-apache-org/it/${id}`;
  sendPost(url, {
    ":operation": "import",
    ":content": JSON.stringify(content),
    ":contentType": "json",
  });
  sendPost(url, {
    "jcr:primaryType": "sling:Page",
  });
  return url;
}

/**
 * Configures a component using the dialog values
 *
 * @param {string} path the path to the resource to edit
 * @param {string} editor the editor to read
 * @param {'ui'|'reference'} project the project to read the editor configuration from
 * @param {string} defaultValue a value to use for populating fields
 * @param {Record<string,string>} values a map of values
 */
export function configureComponentFromDialog(
  path,
  editor,
  project,
  defaultValue,
  values = {}
) {
  cy.readFile(`../${project}/src/main/resources/jcr_root/${editor}.json`).then(
    (dialog) => {
      cy.visit(`/cms/editor/edit.html${path}?editor=${editor}`);
      Object.keys(dialog.fields)
        .filter((k) => !k.includes(":"))
        .forEach((k) => {
          const field = dialog.fields[k];
          switch (field["sling:resourceType"]) {
            case "sling-cms/components/editor/fields/textarea":
            case "sling-cms/components/editor/fields/text":
              cy.get(`*[name=${field.name}]`).type(
                values[field.name] || defaultValue
              );
              break;
            case "sling-cms/components/editor/fields/select":
              if (field.options) {
                const value =
                  values[field.name] ||
                  field.options[Object.keys(field.options)[0]].value.toString();
                cy.get(`select[name=${field.name}]`).select(value);
              }
              break;
            case "sling-cms/components/editor/fields/path":
              cy.get(`*[name=${field.name}]`).type(
                values[field.name] ||
                  `${field.basePath || "/content"}//${defaultValue}`
              );
              break;
            case "sling-cms/components/editor/fields/hidden":
              // do nothing for hidden fields
              break;
            default:
              console.warn(
                `Unsupported resource type ${field["sling:resourceType"]}.`
              );
          }
        });
      cy.get(".Form-Ajax").submit();
    }
  );
}
