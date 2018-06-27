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
[Apache Sling](https://sling.apache.org) > [Sling CMS](https://github.com/apache/sling-org-apache-sling-app-cms) > [Administration](administration.md) > Administrative Tools

# Administrative Tools

There are a number of administrative tools to help make administering Sling CMS easier, they include:

## Bulk Replace

![Bulk Replace](img/bulk-replace.png)

This tool can be accessed at [http://localhost:8080/cms/admin/bulkreplace.html{SUFFIX}](http://localhost:8080/cms/admin/bulkreplace.html{SUFFIX}), with the *{SUFFIX}* being the path under which you want the replacement to take place. This will find and replace the target term with the replacement term in all matching properties.

## Content Packages

![Content Packages](img/content-packages.png)

This is accessible from *Tools > Content Packages* or at [http://localhost:8080/bin/packages.html/](http://localhost:8080/bin/packages.html/) and allows administrators to create, upload and download ZIP based packages of content using the Jackrabbit VLT packaging tool.

## Load Content

![Load Content](img/load-content.png)

This tool can be accessed at [http://localhost:8080/cms/admin/loadcontent.html{SUFFIX}](http://localhost:8080/cms/admin/loadcontent.html{SUFFIX}), with the *{SUFFIX}* being the path under which you want load the content. This will load a JSON string of content into the Sling CMS repository.

## Internationalization

![i18n console](img/internationalization.png)

This console is accessible from *Tools > Internationalization* or at [http://localhost:8080/cms/i18n/dictionaries.html/etc/i18n](http://localhost:8080/cms/i18n/dictionaries.html/etc/i18n) This allows you to specify i18n dictionaries for translating text to multiple languages. 

## Mappings

![Mappings Console](img/mappings.png)

This console is accessible from *Tools > Mappings* or at [http://localhost:8080/cms/mappings/list.html/etc/map](http://localhost:8080/cms/mappings/list.html/etc/map). Allows you to create and manage Sling Mappings which are used to map content based on the requested URL to the Sling repository.

## Node Browser

![Node Browser](img/node-browser.png)

This tool is accessible from *Tools > Node Browser* or at [http://localhost:8080/bin/browser.html](http://localhost:8080/bin/browser.html). Allows you to view and modify the underling node structure of the Sling repository.

## System Console

![OSGi Console](img/osgi-console.png)

This console is accessbile from *Tools > System Console* or at [http://localhost:8080/system/console](http://localhost:8080/system/console). The OSGi Console contains several administrative tools allowing administrators to install and manage bundles, configure logging, configure services and many other tasks.

# Users & Groups

![users and Groups](img/users-groups.png)

This tool is accessbile from *Tools > Users & Groups* or at [http://localhost:8080/bin/users.html](http://localhost:8080/bin/users.html). It allows administrators to create and manage users and groups within Sling CMS. Permissions are managed in the Node Browser