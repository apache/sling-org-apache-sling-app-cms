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
const { login, sendPost } = require("../../util/test-helper");

describe("Reference Form Component Tests", () => {
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

  describe("Actions", () => {
    describe("sendemail", () => {
      it("basic", () => {
        setupBasicTest("sendemail", "forms/actions/sendemail");
      });
      it("xss", () => {
        xssTest("sendemail", "forms/actions/sendemail");
      });
    });

    describe("updateprofile", () => {
      it("basic", () => {
        setupBasicTest("updateprofile", "forms/actions/updateprofile");
      });
      it("xss", () => {
        xssTest("updateprofile", "forms/actions/updateprofile");
      });
    });

    describe("usergeneratedcontent", () => {
      it("basic", () => {
        setupBasicTest(
          "usergeneratedcontent",
          "forms/actions/usergeneratedcontent"
        );
      });
      it("xss", () => {
        xssTest(
          "honeypousergeneratedcontent",
          "forms/actions/usergeneratedcontent"
        );
      });
    });
  });

  describe("Fields", () => {
    describe("honeypot", () => {
      it("basic", () => {
        setupBasicTest("honeypot", "forms/fields/honeypot");
        cy.get(".main .honeypot-basic textarea");
      });
      it("xss", () => {
        xssTest("honeypot", "forms/fields/honeypot");
      });
    });

    describe("selection", () => {
      it("basic", () => {
        setupBasicTest("selection", "forms/fields/selection");
        cy.get(".main select[name=selection-basic]");
      });
      it("xss", () => {
        xssTest("selection", "forms/fields/selection");
      });
    });

    describe("textarea", () => {
      it("basic", () => {
        setupBasicTest("textarea", "forms/fields/textarea");
        cy.get(".main textarea[name=textarea-basic]");
      });
      it("xss", () => {
        xssTest("textarea", "forms/fields/textarea");
      });
    });

    describe("textfield", () => {
      it("basic", () => {
        setupBasicTest("textfield", "forms/fields/textfield");
        cy.get(".main input[name=textfield-basic]");
      });
      it("xss", () => {
        xssTest("textfield", "forms/fields/textfield");
      });
    });
  });

  describe("Providers", () => {
    describe("userprofile", () => {
      it("basic", () => {
        setupBasicTest("profile", "forms/providers/userprofile");
      });
      it("xss", () => {
        xssTest("profile", "forms/providers/userprofile");
      });
    });
  });

  describe("fieldset", () => {
    it("basic", () => {
      setupBasicTest("fieldset", "forms/fieldset");
      cy.get(".main fieldset");
    });
    it("xss", () => {
      xssTest("fieldset", "forms/fieldset");
    });
  });
  describe("form", () => {
    it("basic", () => {
      setupBasicTest("login", "forms/form");
      cy.get(".main form[data-analytics-id]");
    });
    it("xss", () => {
      xssTest("form", "forms/form");
    });
  });
  describe("login", () => {
    it("basic", () => {
      setupBasicTest("login", "forms/login");
      cy.get(".main form[data-analytics-id='Login Form']");
    });
    it("xss", () => {
      xssTest("login", "forms/login");
    });
  });
});
