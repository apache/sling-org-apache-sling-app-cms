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

const { expect } = require("chai");
const { login } = require("../utils");

describe("Authorization Tests", () => {
  it("validate 403", () => {
    cy.request({ url: "/", failOnStatusCode: false }).should((response) => {
      expect(response.status).to.equal(403);
    });
  });
  it("recieve error page", () => {
    cy.visit("/", { failOnStatusCode: false });
    cy.pa11y();
  });
  it("can click to login", () => {
    cy.visit("/", { failOnStatusCode: false });
    cy.get("a").click();
    cy.url().should("contain", "/system/sling/form/login");
    cy.pa11y();
    cy.get("input[name=j_username]").invoke("attr", "value", "admin");
    cy.get("input[name=j_password]").invoke("attr", "value", "admin");
    cy.get("form").submit();
    cy.url().should("contain", "/cms/start.html");
    cy.pa11y();
  });
  it("can logout", () => {
    login();
    cy.visit("/cms/start.html");
    cy.get(".navbar-burger").click();
    cy.get(".navbar-menu").should("be.visible");
    cy.get('a[href="/system/sling/logout"]').click();
    cy.pa11y();
  });
});
