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

const { doneLoading } = require("../utils");
const { login, sendPost } = require("../util/test-helper");

function setupBasicTest(componentName, properties, subpath = "") {
  const body = {
    "jcr:primaryType": "nt:unstructured",
    "sling:resourceType": "sling-cms/components/editor/slingform",
    button: "Save",
    "fields/jcr:primaryType": "nt:unstructured",
    "fields/sling:resourceType": "sling-cms/components/general/container",
    "fields/field/jcr:primaryType": "nt:unstructured",
    "fields/field/sling:resourceType": `sling-cms/components/editor/fields/${subpath}${componentName}`,
  };

  Object.keys(properties).forEach((k) => {
    body[`fields/field/${k}`] = properties[k];
  });
  sendPost(`/apps/it/${componentName}-basic`, body);

  cy.visit(
    `/cms/editor/edit.html/content/apache/sling-apache-org/it?editor=/apps/it/${componentName}-basic`
  );
}

describe("Editor", () => {
  before(() => {
    login();
    sendPost("/apps/it", {
      ":operation": "delete",
    });
    sendPost("/apps/it", {
      "jcr:primaryType": "sling:Folder",
    });
    sendPost("/content/apache/sling-apache-org/it", {
      ":operation": "delete",
    });
    sendPost("/content/apache/sling-apache-org/it", {
      "jcr:primaryType": "sling:Folder",
      "jcr:content/jcr:primaryType": "nt:unstructured",
    });
  });
  beforeEach(() => {
    login();
  });
  describe("Fields", () => {
    it("members", () => {
      setupBasicTest("members", { name: "test" }, "auth/");
      cy.get("input[name=test]").should("not.be.undefined");
    });
    it("membership", () => {
      setupBasicTest("membership", { name: "test" }, "auth/");
      cy.get("input[name=test]").should("not.be.undefined");
    });
    it("combobox", () => {
      setupBasicTest("combobox", { label: "Test", name: "test" });
      cy.get("input[name=test]").should("not.be.undefined");
      cy.get("label[for=test]").should("not.be.undefined");
    });
    it("path", () => {
      setupBasicTest("path", {
        label: "Test",
        name: "test",
        basePath: "/content",
        type: "page",
      });
      cy.get("input[name=test]").should("not.be.undefined");
      cy.get("label[for=test]").should("not.be.undefined");
      cy.get(".search-button").click();
      doneLoading();
      cy.get(".modal input[name=term]").type("apache");
      cy.get(".modal select[name=type]").select("All");
      cy.get(".modal button[type=submit]").click();
      doneLoading();
      cy.get(".modal .tile").should("not.be.undefined");
    });
    it("repeating", () => {
      setupBasicTest("repeating", {
        label: "Test",
        name: "test",
      });
      cy.get("input[name=test]").should("not.be.undefined");
      cy.get("label[for=test]").should("not.be.undefined");
      cy.get("button.repeating__add").click();
      cy.get(".repeating__container button.repeating__remove").click();
    });
    it("taxonomy", () => {
      setupBasicTest("taxonomy", {
        label: "Test",
        name: "test",
        basePath: "/etc/taxonomy",
      });
      cy.get("label[for=test]").should("not.be.undefined");
      cy.get(".labelfield__field input[type=text]").type(
        "/etc/taxonomy/reference"
      );
      cy.get(".labelfield__add").click();
      cy.get('a[title="/etc/taxonomy/reference"]').should("not.be.undefined");
      cy.get('a[title="/etc/taxonomy/reference"] button.delete').click();
    });
    it("text", () => {
      setupBasicTest("text", {
        label: "Test",
        name: "test",
      });
      cy.get("label[for=test]").should("not.be.undefined");
      cy.get("input[name=test]").should("not.be.undefined");
    });
    it("well", () => {
      setupBasicTest("well", {
        title: "Test",
        collapse: "true",
        "collapse@TypeHint": "Boolean",
      });
      cy.get(".card-content").should("not.be.visible");
      cy.get(".card-header").click();
      cy.get(".card-content").should("be.visible");
    });
  });
});
