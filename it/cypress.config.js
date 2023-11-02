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
const { defineConfig } = require("cypress");
const { pa11y, prepareAudit } = require("@cypress-audit/pa11y");

module.exports = defineConfig({
  e2e: {
    reporter: 'junit',
    reporterOptions: {
      mochaFile: 'target/surefire-reports/TEST-[suiteName].xml',
      toConsole: true,
    },
    baseUrl: "http://localhost:8080",
    viewportWidth: 1000,
    viewportHeight: 660,
    excludeSpecPattern: ["**/__snapshots__/*", "**/__image_snapshots__/*"],
    setupNodeEvents(on, _config) {
      on("before:browser:launch", (_browser = {}, launchOptions) => {
        prepareAudit(launchOptions);
      });

      on("task", {
        pa11y: pa11y(console.log.bind(console)),
      });
    },
  },
});
