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

const { login } = require("../util/test-helper");
const { doneLoading } = require("../utils");

describe("Taxonomy Tests", () => {
  beforeEach(() => {
    login();
  });
  it("view taxonomy list", () => {
    cy.visit("/cms/start.html");
    cy.get(".toggle-Manage").click();
    cy.get(".nav-link-Taxonomy").click();
    cy.url().should("contain", "/cms/taxonomy/list.html/etc/taxonomy");
    cy.pa11y();
  });
  it("show select taxonomy item", () => {
    cy.visit("/cms/taxonomy/list.html/etc/taxonomy");

    cy.get('tr[data-resource="/etc/taxonomy/reference"]').click();
    cy.get('.has-addons>a[title="Edit Taxonomy Item"]').should("be.visible");
    cy.get('.has-addons>a[title="Move / Copy Taxonomy Item"]').should(
      "be.visible"
    );
    cy.get('.has-addons>a[title="Delete Taxonomy Item"]').should("be.visible");
  });
  it("should show allow for editing", () => {
    cy.visit("/cms/taxonomy/list.html/etc/taxonomy");
    cy.get('tr[data-resource="/etc/taxonomy/reference"]').click();
    cy.get('.has-addons>a[title="Edit Taxonomy Item"]').click();
    cy.get('input[name="jcr:title"]').should("be.visible");
    cy.pa11y();

    cy.get('input[name="jcr:title"]').invoke("attr", "value", "Reference2");
    cy.get(".modal .Form-Ajax").submit();

    cy.get(".close-modal.is-primary").should("be.visible");
    cy.pa11y();
  });
  it("should show allow for adding new taxonomy item", () => {
    cy.visit("/cms/taxonomy/list.html/etc/taxonomy");
    cy.get('a[data-title="Add Taxonomy Item"]').click();
    cy.get('input[name="jcr:title"]').should("be.visible");

    cy.get('input[name="jcr:title"]').invoke(
      "attr",
      "value",
      "A New Reference"
    );
    cy.get('input[name=":name"]').invoke("attr", "value", "new-reference");
    cy.get(".modal .Form-Ajax").submit();

    cy.get(".close-modal.is-primary").should("be.visible");
    cy.get(".close-modal.is-primary").click();
    cy.pa11y();
  });
  it("should show allow for adding deleting a taxonomy item", () => {
    cy.visit("/cms/taxonomy/list.html/etc/taxonomy");
    cy.get('tr[data-resource="/etc/taxonomy/new-reference"]').click();
    cy.get('.has-addons>a[title="Delete Taxonomy Item"]').click();

    doneLoading();
    cy.get(".modal").should("be.visible");
    cy.get(".modal .Form-Ajax").submit();
    cy.get(".close-modal.is-primary").should("be.visible");
    cy.get(".close-modal.is-primary").click();
    doneLoading();
    cy.pa11y();
  });
});
