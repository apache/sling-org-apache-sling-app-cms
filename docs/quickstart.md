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
[Apache Sling](https://sling.apache.org) > [Sling CMS](https://github.com/apache/sling-org-apache-sling-app-cms) > Getting Started

# Quickstart

This guide will give you a quick introduction to getting started with Sling CMS. There are two options for getting started:

 1. Running Locally
 2. Installing on a VM with Vagrant

## Running Locally

This installation is the simplest option for developers or those looking to quickly set up a test instance Sling CMS. To run the Sling CMS on a local envionment:

 1. Download the [Sling CMS JAR](https://search.maven.org/#search) to a directory on your computer
 2. Run the command `java -jar org.apache.sling.app.cms-[version],jar` to start Sling CMS
 3. Open a browser and navigate to [http://localhost:8080/](http://localhost:8080/). The default credentials are *admin*/*admin*.

## Installing on a VM with Vagrant

This option allows you to install Sling CMS and Apache web server, which allows for a much closer reproduction of a real environment. To install with this option:

 1. Clone the Sling CMS Git repo with: `git clone https://github.com/apache/sling-org-apache-sling-app-cms.git`
 2. Change directory into the *sling-org-apache-sling-app-cms/vagrant* directory
 3. Run the command `vagrant up`