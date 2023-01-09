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


/**
 * Sends a post to the specified URL
 * @param {string} url the URL to which to send the post
 * @param {Record<string, string>} body the body of the post to send
 */
export function sendPost(url, body) {
  cy.request({
    method: "POST",
    url,
    form: true,
    body,
    headers: {
      Referer: process.env.CYPRESS_BASE_URL || "http://localhost:8080",
    },
  });
}
/**
 * Login directly by posting to the login endpoint
 */
export function login() {
  sendPost("/j_security_check", {
    j_username: "admin",
    j_password: "admin",
  });
}
