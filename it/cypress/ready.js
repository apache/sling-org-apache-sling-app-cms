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
const fetch = require("node-fetch-commonjs");

const url = process.env.CYPRESS_BASE_URL;
const auth = Buffer.from("admin:admin").toString("base64");

async function sleep(ms) {
  return new Promise((resolve) => {
    setTimeout(resolve, ms);
  });
}

async function checkSystemReady() {
  const res = await fetch(`${url}/system/health?tags=systemalive&format=txt`, {
    headers: { Authorization: `Basic ${auth}` },
  });
  return res.ok;
}

async function checkLoginContent() {
  const res = await fetch(`${url}/system/sling/login`);
  if (res.ok) {
    const body = await res.text();
    if (body.indexOf("Welcome to Apache Sling CMS") !== -1) {
      return true;
    }
  }
  return false;
}

async function checkHomeStatus() {
  const res = await fetch(`${url}/`);
  return res.status === 403;
}

async function main() {
  console.log(`Waiting for Sling to start on: ${url}`);
  for (let i = 0; i < 240; i++) {
    try {
      const systemReadyOk = await checkSystemReady();
      const homeStatusOk = await checkHomeStatus();
      const loginContentOk = await checkLoginContent();

      if (systemReadyOk && homeStatusOk && loginContentOk) {
        console.log(`Sling started on: ${url}`);
        await sleep(2000);
        return;
      }
    } catch (e) {
      console.warn("Caught error checking status", e);
    }
    await sleep(5000);
  }
  console.log("Sling failed to start in the allotted time, failing...");
  throw Error(`Sling did not successfully start on URL: ${url}`);
}
main();
