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

describe("Reference Component Tests", () => {
  beforeEach(() => {
    login();
  });
  describe("home", () => {
    it("basic", () => {
      cy.visit("/cms/start.html");
      cy.get(
        'form[action="/mnt/overlay/sling-cms/content/start/jcr:content/container/scrollcontainer/recent.search.html"]'
      ).should("be.visible");
      cy.get('nav[aria-label="Manage"]').should("be.visible");
      cy.get('nav[aria-label="Tools"]').should("be.visible");
      cy.pa11y();
    });

    it("profile", () => {
      cy.visit("/cms/start.html");
      cy.get(".navbar-burger").click();
      cy.get('.navbar a[data-title="User Profile"]').click();
      doneLoading();
      cy.get(".modal").should("be.visible");
    });
  });
  describe("Tools", () => {
    it("Bulk Replace", () => {
      cy.visit("/cms/admin/bulkreplace.html");
      cy.pa11y();

      cy.get("input[name='suffix'").type("/content");
      cy.get("button[type=submit]").click();

      cy.get('input[name="updateProperties"]').type("haystack");
      cy.get('input[name="find"]').type("noodle");
      cy.get('input[name="find"]').type("needle");
      cy.get("button[type=submit]").click();
      doneLoading();
    });

    it("Config", () => {
      cy.visit("/cms/config/configs.html/conf");
      cy.get('a[data-title="Add Config Context"]').should("be.visible");

      cy.visit("/cms/config/buckets.html/conf/global");
      cy.get('a[data-title="Add Bucket"]').should("be.visible");

      cy.visit("/cms/config/bucket.html/conf/global/files");
      cy.get('a[data-title="Add Config"]').should("be.visible");

      cy.visit("/cms/config/edit.html/conf/global/files/editors");
      cy.get("a.action-button").should("be.visible");
      cy.pa11y();

      cy.visit("/cms/config/edit.html/conf/global/files/transformations");
      cy.get('a[data-title="Add Transformation"]').should("be.visible");

      cy.visit(
        "/cms/transformations/edit.html/conf/global/files/transformations/sling-cms-thumbnail"
      );
      cy.get("a.action-button").should("be.visible");
      cy.pa11y();

      cy.visit("/cms/config/bucket.html/conf/global/site");
      cy.get('a[data-title="Add Config"]').should("be.visible");

      cy.visit("/cms/config/edit.html/conf/global/site/policies");
      cy.get("a.action-button").should("be.visible");
      cy.pa11y();

      cy.visit("/cms/config/edit.html/conf/global/site/rewrite");
      cy.get("a.action-button").should("be.visible");
      cy.pa11y();

      cy.visit("/cms/config/edit.html/conf/global/site/settings");
      cy.get("a.action-button").should("be.visible");
      cy.pa11y();

      cy.visit("/cms/config/edit.html/conf/global/site/templates");
      cy.get('a[data-title="Add Template"]').should("be.visible");

      cy.visit("/cms/template/edit.html/conf/global/site/templates/fragment");
      cy.get("a.action-button").should("be.visible");
      cy.pa11y();
    });

    it("i18n", () => {
      cy.visit("/cms/i18n/dictionaries.html/apps/reference");
      cy.get('a[data-title="Add i18n Dictionary"]').should("be.visible");
      cy.get("main td[data-value='/apps/reference/i18n'] a").click();

      cy.get('a[data-title="Add Language"]').should("be.visible");
      cy.get('a[data-title="Add Entry"]').should("be.visible");
      cy.get("button.is-primary").should("be.visible");
    });

    it("Jobs", () => {
      cy.visit("/cms/jobs/list.html");
      cy.get('a[data-title="Add Start Job"]').click();
      cy.get("select[name='_job']").select("Bulk Publication");

      cy.get(".repeating__add").click();
      cy.get(".repeating__container input[name=paths]").type(
        "/content/not/a/path"
      );
      cy.get("button[type=submit]").click();
      doneLoading();
      cy.wait(2000);
      cy.get(".modal .close-modal.is-primary").click();
      cy.get(".Main-Content tbody tr:first-child td:nth-child(2) a").click();
      cy.get("dl").should("be.visible");
      cy.pa11y();
    });

    it("Load Content", () => {
      cy.visit("/cms/admin/loadcontent.html");
      cy.pa11y();

      cy.get("input[name='suffix'").type("/content/it");
      cy.get("button[type=submit]").click();

      const text = JSON.stringify({ "jcr:primaryType": "sling:Folder" });
      cy.get("textarea[name=':content']").type(text, {
        parseSpecialCharSequences: false,
      });
      cy.get("button[type=submit]").click();
      doneLoading();
      cy.pa11y();
    });

    it("Publication", () => {
      cy.visit("/cms/publication/home.html");
      cy.pa11y();
    });

    it("Query Debugger", () => {
      cy.visit("/cms/admin/querydebug.html");

      cy.get('textarea[name="statement"]').type("SELECT * FROM [sling:Page]");
      cy.get("button[type=submit]").click();

      doneLoading();
      cy.get("#query-debug dl").should("be.visible");
    });

    it("Static", () => {
      cy.visit("/cms/static/content.html/static/clientlibs/sling-cms");
      cy.get('a[data-title="Add File"]').should("be.visible");
      cy.get('a[data-title="Add Folder"]').should("be.visible");
    });

    it("Users/Groups", () => {
      cy.visit("/cms/auth/list.html/home/users");

      cy.get('a[data-title="Add User"]').click();
      cy.get(".modal").should("be.visible");

      cy.visit("/cms/auth/list.html/home/groups");

      cy.get('a[data-title="Add Group"]').click();
      cy.get(".modal").should("be.visible");
    });
  });
});
