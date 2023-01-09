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

const { setupBasicTest, xssTest } = require("./utils");
const {
  createComponentPage,
  configureComponentFromDialog,
  COMPONENT_SUBPATH,
} = require("../../util/component-test-helper");
const { login, sendPost } = require("../../util/test-helper");

describe("Reference General Component Tests", () => {
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
  });

  describe("columncontrol", () => {
    it("basic", () => {
      setupBasicTest("columncontrol", "general/columncontrol");
    });
    it("xss", () => {
      xssTest("columncontrol", "general/columncontrol");
    });
  });

  describe("list", () => {
    it("basic", () => {
      setupBasicTest("list", "general/list", {
        query: "SELECT * FROM [sling:Page]",
        limit: "10",
        tag: "div",
      });
    });
    it("xss", () => {
      xssTest("list", "general/list", {
        query: "SELECT * FROM [sling:Page]",
        limit: "10",
      });
    });
  });

  describe("RSS", () => {
    it("basic", () => {
      sendPost("/content/apache/sling-apache-org/it/feed-basic", {
        "jcr:primaryType": "sling:Folder",
        "sling:resourceType": "reference/components/general/rss",
      });
      cy.request("/content/apache/sling-apache-org/it/feed-basic.rss.xml").then(
        (response) => {
          const parser = new DOMParser();
          parser.parseFromString(response.body, "text/xml");
        }
      );
    });
  });

  describe("Search", () => {
    it("basic", () => {
      const page = createComponentPage(`search-basic`, {
        "jcr:primaryType": "nt:unstructured",
        "sling:resourceType": `reference/components/general/search`,
      });
      configureComponentFromDialog(
        `${page}${COMPONENT_SUBPATH}`,
        `/apps/reference/components/general/search/edit`,
        "reference",
        `search-basic`,
        { limit: "2", basePath: "/content" }
      );
      cy.visit(`${page}.html?q=apache`);
    });
  });
});
