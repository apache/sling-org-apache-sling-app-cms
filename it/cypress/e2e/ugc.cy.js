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
const { createComponentPage } = require("../util/component-test-helper");
const { login, sendPost } = require("../util/test-helper");

describe("UGC Tests", () => {
  before(() => {
    login();
    sendPost("/content/apache/sling-apache-org/it", {
      ":operation": "delete",
    });

    sendPost("/content/apache/sling-apache-org/it", {
      "jcr:primaryType": "sling:Folder",
    });
  });
  beforeEach(() => {
    login();
    sendPost("/etc/usergenerated/it", {
      ":operation": "delete",
    });
  });

  it("Admin UI", () => {
    cy.visit("/cms/usergenerated/content.html/etc/usergenerated");
    cy.get('a[data-title="Add Bucket"]').click();

    cy.get("input[name='jcr:content/jcr:title']").type("it");
    cy.get("input[name=':name']").type("it");
    cy.get(".modal button[type=submit]").click();

    doneLoading();
    cy.get(".modal .close-modal.is-primary").click();
  });

  it("Submission", () => {
    const page = createComponentPage("ugc", {
      "jcr:primaryType": "nt:unstructured",
      submitText: "Submit",
      successAction: "redirect",
      successMessage: "Great Success!",
      "sling:resourceType": "reference/components/forms/form",
      fields: {
        "jcr:primaryType": "nt:unstructured",
        textfield: {
          "jcr:primaryType": "nt:unstructured",
          saveAs: "string",
          required: true,
          name: "name",
          type: "text",
          label: "Name",
          "sling:resourceType": "reference/components/forms/fields/textfield",
        },
      },
      actions: {
        "jcr:primaryType": "nt:unstructured",
        usergeneratedcontent: {
          "jcr:primaryType": "nt:unstructured",
          wrapPage: true,
          bucket: "it",
          pathDepth: "0",
          contentType: "OTHER",
          preview: "Preview!!",
          name: "coolname",
          targetPath: "/content",
          approveAction: "MOVE",
          "sling:resourceType":
            "reference/components/forms/actions/usergeneratedcontent",
        },
      },
    });

    cy.visit(`${page}.html`);
    cy.get('input[name="name"]').type("Hello World");
    cy.get("button[type=submit]").click();
    cy.get(".alert").should("be.visible");

    cy.visit("/cms/usergenerated/content.html/etc/usergenerated/it");
    cy.get("tbody tr:first-child").click();
    cy.get(".buttons a[title='Review User Generated Content']")
      .should("have.attr", "href")
      .then((href) => {
        cy.visit(href);
      });

    cy.get("form[data-callback=handleugc]").should("be.visible");
  });
});
