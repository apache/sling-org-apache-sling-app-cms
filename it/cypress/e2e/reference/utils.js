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

const {
  createComponentPage,
  configureComponentFromDialog,
  COMPONENT_SUBPATH,
  XSS_PAYLOAD,
} = require("../../util/component-test-helper");

export function assertNoAlert() {
  let spy = cy.spy(window, "alert");
  cy.wait(500).then(() => {
    expect(spy).to.haveOwnProperty("callCount");
    expect(spy).to.not.be.called;
  });
}

/**
 * Sets up a basic test by creating a page with the single component on it
 * @param {string} componentName the name of the component
 * @param {string} subpath the path of the component under reference/components/
 * @param {Record<string,string> | undefined} additionalProperties any additional properties to pass into the configuration step
 */
export function setupBasicTest(componentName, subpath, additionalProperties) {
  const page = createComponentPage(`${componentName}-basic`, {
    "jcr:primaryType": "nt:unstructured",
    "sling:resourceType": `reference/components/${subpath}`,
  });
  configureComponentFromDialog(
    `${page}${COMPONENT_SUBPATH}`,
    `/apps/reference/components/${subpath}/edit`,
    "reference",
    `${componentName}-basic`,
    additionalProperties
  );
  cy.visit(`${page}.html`);
}

/**
 * Sets up a basic test using the XSS payload and verifies no alerts appear
 * @param {string} componentName the name of the component
 * @param {string} subpath the path of the component under reference/components/
 * @param {Record<string,string> | undefined} additionalProperties any additional properties to pass into the configuration step
 */
export function xssTest(componentName, subpath, additionalProperties) {
  const page = createComponentPage(`${componentName}-xss`, {
    "jcr:primaryType": "nt:unstructured",
    "sling:resourceType": `reference/components/${subpath}`,
  });
  configureComponentFromDialog(
    `${page}${COMPONENT_SUBPATH}`,
    `/apps/reference/components/${subpath}/edit`,
    "reference",
    XSS_PAYLOAD,
    additionalProperties
  );
  cy.visit(`${page}.html`);
  assertNoAlert();
}
