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

describe("Authoring Tests", () => {
  before(() => {
    login();
    sendPost("/content/apache/sling-apache-org/it", {
      ":operation": "delete",
    });
  });
  beforeEach(() => {
    login();
  });
  it("validate editor", () => {
    cy.visit("/cms/page/edit.html/content/apache/sling-apache-org/index");
    cy.get("iframe").should("not.be.undefined");
  });

  it("validate site view", () => {
    cy.visit("/cms/site/sites.html/content");
    cy.pa11y();
  });

  it("validate contents view", () => {
    cy.visit("/cms/site/sites.html/content/apache");
    cy.pa11y();
  });

  it("validate add page", () => {
    cy.visit("/cms/site/content.html/content/apache/sling-apache-org");

    cy.get('.level .buttons a[data-title="Add Page"]').click();

    doneLoading();
    cy.get("select[name=pageTemplate]").select("Base Page");

    cy.get("input[name=title]").type("Integration Test");
    cy.get('input[name=":name"]').type("it");

    cy.get(".modal .is-primary").click();
    doneLoading();

    cy.get(".modal .close-modal.is-primary").click();
    doneLoading();
    cy.get('.card[data-value="/content/apache/sling-apache-org/it"]').should(
      "not.be.undefined"
    );

    cy.visit("/cms/site/content.html/content/apache/sling-apache-org");
    cy.get('.card[data-value="/content/apache/sling-apache-org/it"]').click();
    cy.get('.level a[data-title="Delete the specified page"]').click();
    doneLoading();
    cy.get(".modal .is-primary").click();
    doneLoading();
    cy.get(".modal .close-modal.is-primary").click();
  });

  it("can view page references", () => {
    cy.visit("/cms/site/content.html/content/apache/sling-apache-org");
    cy.get(
      '.card[data-value="/content/apache/sling-apache-org/index"]'
    ).click();
    cy.get('.level .buttons a[data-title="References"]').click();
    doneLoading();
    cy.get(".modal .modal-title").should("not.be.undefined");
  });

  it("can manage page versions", () => {
    cy.visit("/cms/site/content.html/content/apache/sling-apache-org");
    cy.get(
      '.card[data-value="/content/apache/sling-apache-org/index"]'
    ).click();
    cy.get('.level .buttons a[data-title="Manage Versions"]').click();
    doneLoading();
    cy.get(".modal .modal-title").should("not.be.undefined");
    cy.get(".modal .versionmanager>form button[type=submit]").click();
    doneLoading();
    cy.get(".modal .close-modal.is-primary").click();
    cy.get(
      '.card[data-value="/content/apache/sling-apache-org/index"]'
    ).click();
    cy.get('.level .buttons a[data-title="Manage Versions"]').click();
    cy.get(".modal .modal-title").should("not.be.undefined");
  });

  it("can view move/copy", () => {
    cy.visit("/cms/site/content.html/content/apache/sling-apache-org");
    cy.get(
      '.card[data-value="/content/apache/sling-apache-org/index"]'
    ).click();
    cy.get('.level .buttons a[data-title="Move / Copy Page"]').click();
    doneLoading();
    cy.get(".modal .modal-title").should("not.be.undefined");
    cy.get("input[name=':dest']").should("not.be.undefined");
    cy.get("input[name=':operation']").should("not.be.undefined");
  });
});
