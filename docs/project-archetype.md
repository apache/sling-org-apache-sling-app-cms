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
[Apache Sling](https://sling.apache.org) > [Sling CMS](https://github.com/apache/sling-org-apache-sling-app-cms) > [Developers](developers.md) > Extending Sling CMS

# Project Archetype

The Apache Sling CMS Project Archetype creates a simple project structure with common dependencies for you to use to create custom applications on Apache Sling CMS.

## Creating a project with the Archetype

To create a project with the archetype, run the following command:

    mvn archetype:generate -DarchetypeGroupId=org.apache.sling \
      -DarchetypeArtifactId=org.apache.sling.cms.archetype \
      -DarchetypeVersion=0.14.0

You should then enter the following values:

- groupId - the Maven Group Id for your project
- artifactId - the Maven Artifact Id for your project
- version - the initial working version of your project, e.g. 1.0-SNAPSHOT
- package - the Java package for the project (based on groupId)
- appName - the system name of your application. Should be lower case, with dash separators

## Installing the New Project

Install the new project into an Apache Sling CMS instance running at localhost:8080
by running the following command from the project root:

    mvn clean install -P autoInstallBundle

Once the project is installed you should see the configuration here:
http://localhost:8080/cms/config/configs.html/conf

And be able to create a page under /content/[appName] (once you create the site).
