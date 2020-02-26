<!-- Licensed to the Apache Software Foundation (ASF) under one or more contributor 
	license agreements. See the NOTICE file distributed with this work for additional 
	information regarding copyright ownership. The ASF licenses this file to 
	you under the Apache License, Version 2.0 (the "License"); you may not use 
	this file except in compliance with the License. You may obtain a copy of 
	the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required 
	by applicable law or agreed to in writing, software distributed under the 
	License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS 
	OF ANY KIND, either express or implied. See the License for the specific 
	language governing permissions and limitations under the License. -->
[Apache Sling](https://sling.apache.org) > [Sling CMS](https://github.com/apache/sling-org-apache-sling-app-cms) > [Developers](developers.md) > Email Configuration

# Email Configuration

Apache Sling CMS uses [Apache Sling Commons Messing Mail](https://github.com/apache/sling-org-apache-sling-commons-messaging-mail) for sending emails.

## Crypto Configuration

The Reference project includes base configurations for [Apache Sling Commons Crypto](https://sling.apache.org/documentation/bundles/commons-crypto.html), however you must still configure the [Apache Sling Commons Crypto “File Password Provider”](http://localhost:8080/system/console/configMgr/org.apache.sling.commons.crypto.internal.FilePasswordProvider).

The _Names_ for the configuration should be _default_ and the _Path_ should be the filesystem path to a file containing the password.

## SMTP Configuration

Once Apache Sling Commons Crypto is configured, you can configure Apache Sling Commons Messaging Mail. 

Use the [Crypto Web Console](http://localhost:8080/system/console/sling-commons-crypto-encrypt) to encrypt the password for the SMTP server. 

Use the password from the Crypto Web Console password to configure the [Apache Sling Commons Messaging Mail “Simple Mail Service”](http://localhost:8080/system/console/configMgr/org.apache.sling.commons.messaging.mail.internal.SimpleMailService)