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
[Apache Sling](https://sling.apache.org) > [Sling CMS](https://github.com/apache/sling-org-apache-sling-app-cms) > [Developers](developers.md) > Project Intro

# Project Intro

This page will give you an introduction of the structure of the Sling CMS project and some idea of how all of this comes together. If you aren't planning on extending or contributing to Sling CMS, this probably isn't necessary for you.

## Project Structure

The Sling CMS project has five main modules:

 - builder - this is a Sling Starer builder, it contains the repoinit text files which define the dependencies and requirements to build Sling CMS. This will ultimately build the Sling CMS Jar.
 - api - this is your API for interacting with Sling CMS
 - core - this is the Java code behind Sling CMS. This includes the Sling Models, Filters, Servlets, Post Operations and Rewriter code.
 - reference - this is a reference application for developers to use to extend Sling CMS, this includes both Java code and content loaded by the Sling Content Loader
 - ui - this is a bulk of the content and scripts for the Sling CMS. Most of this is located under `/libs/sling-cms` although there are some other directories for configurations

## Using the API

The API can be imported into your Maven project with the following dependency include in your pom.xml in the `dependencies` element:

		<dependency>
			<groupId>org.apache.sling</groupId>
			<artifactId>org.apache.sling.cms.api</artifactId>
			<version>CURRENT_VERSION</version>
			<scope>provided</scope>
		</dependency>


## Front End Code

Sling CMS uses Gulp to build the front end code which is them packaged by Maven into the overall project build. This can be a bit chatty over the network if you run `mvn clean install` with every build, so unless you are updating the front end code, it's often better to just run `mvn install`

## Important Directories

Most of the scripts are installed under the directory `/libs/sling-cms/components` and the content is under `/libs/sling-cms/content`

## CMS Content

The Sling CMS uses [Sling Resource Merger](https://sling.apache.org/documentation/bundles/resource-merger.html) to allow developers to overlay content provided in the base CMS. This means that although the default content is stored in `/libs/sling-cms/content` it is actually referenced under `/mnt/overlay/sling-cms/content` which is merged with `/apps/sling-cms/content` using Sling Resource Merger, finally a default Resource Resolver Factory configuration them maps this path to `/cms` for shorter URLs.

## Project archetype

The quickest way to get started building an application on Sling CMS is to use the [project archetype](project-archetype.md).
