/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
const fetch = require("node-fetch");
const fs = require("fs");

const languages = require("./languages");

const mappings = {
  "&": "and",
  "/": " or ",
  "(": " ",
  ")": " ",
  "  ": " ",
};

if (!fs.existsSync("i18n/")) {
  fs.mkdirSync("i18n/");
}

function cleanSymbols(message = "") {
  for (const key in mappings) {
    message = message.replace(key, mappings[key]);
  }
  return message;
}

function cleanupMessages() {
  const messages = require("./messages");
  messages.sort();
  fs.writeFileSync(
    "src/messages.json",
    JSON.stringify([...new Set(messages)], null, 2)
  );
}

async function main() {
  cleanupMessages();

  await translateMessages();

  writeToSlingCms();
}

async function translateMessage(message = "", targetLanguage = "en") {
  const res = await fetch("http://localhost:5000/translate", {
    method: "POST",
    body: JSON.stringify({
      q: cleanSymbols(message),
      source: "en",
      target: targetLanguage,
    }),
    headers: { "Content-Type": "application/json" },
  });

  const json = await res.json();
  return json.translatedText;
}

async function translateMessages() {
  for (const language of languages) {
    console.log(`Translating messages to: ${language}...`);
    let translated = {};
    if (fs.existsSync(`i18n/${language}.json`)) {
      console.log("Loading existing dictionary...");
      translated = require(`../i18n/${language}`);
    }

    const messages = require("./messages");
    for (const message of messages) {
      if (!translated[message]) {
        console.log(`Translating message: ${message}`);
        translated[message] = await translateMessage(message, language);
      } else {
        console.log(`Using existing message for: ${message}`);
      }
    }
    fs.writeFileSync(
      `i18n/${language}.json`,
      JSON.stringify(translated, null, 2)
    );
  }
}

function writeToSlingCms() {
  const i18n = {
    "jcr:primaryType": "sling:OrderedFolder",
    "jcr:content": {
      "jcr:title": "Sling CMS",
    },
  };

  for (const language of languages) {
    console.log(`Converting language ${language}...`);
    const translated = require(`../i18n/${language}`);
    const langJson = {
      "jcr:primaryType": "sling:Folder",
      "jcr:mixinTypes": ["mix:language"],
      "jcr:language": language,
      "sling:resourceType": "sling-cms/components/cms/blank",
    };
    let i = 0;
    for (const key of Object.keys(translated)) {
      langJson[`entry-${i}`] = {
        "jcr:primaryType": "sling:MessageEntry",
        "sling:message": translated[key],
        "sling:key": key,
      };
      i++;
    }
    i18n[language] = langJson;
  }
  console.log("Writing to i18n...");
  fs.writeFileSync(
    "../ui/src/main/resources/jcr_root/libs/sling-cms/i18n.json",
    JSON.stringify(i18n, null, 2)
  );
}

// actually run
main().catch((err) => {
  console.error(err);
  process.exit(1);
});
