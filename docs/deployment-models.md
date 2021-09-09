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
[Apache Sling](https://sling.apache.org) > [Sling CMS](https://github.com/apache/sling-org-apache-sling-app-cms) > [Administration](administration.md) > Deployment Models

# Deployment Models

Apache Sling CMS provides a number of deployment models for your solution needs. 

## Instance Types

The project creates a numer of existing models for different instance types.

### Standalone

An instance that is not part of a cluster of instances. In a standalone instance, the instance handled both authoring and rendering web content. Publishing content in a standalone instance is done by updating the `sling:published` property.

Standalone instances are published with the following feature: 

	org.apache.sling:org.apache.sling.cms.feature:slingosgifeature:slingcms-standalone:[VERSION]

### Author

An author instance is used to author the content of the site. It is not responsible for rendering the content. Content is published from the author instance to the rendering instance using Sling Content Distribution.

Author instances are published with the following feature: 

	org.apache.sling:org.apache.sling.cms.feature:slingosgifeature:slingcms-author:[VERSION]

### Renderer

A renderer instance is used to render the content of the site. It is not responsible for rendering the content. Content should not be authored in the renderer and instead should be published from the author instance to the rendering instance using Sling Content Distribution.

Renderer instances are published with the following feature: 

	org.apache.sling:org.apache.sling.cms.feature:slingosgifeature:slingcms-renderer:[VERSION]

## Sample Deployments

There are a number of samples to help you understand how to deploy Sling CMS:

### VM Installation

The [vagrant](../vagrant) directory contains a project to start Sling CMS in [standalone](#Standalone) mode in two CentOS 7 VMs one to run the CMS instance and one to run Apache Web Server.

### Docker Compose

The [docker](../docker) directory contains a project to start Sling CMS with an author-renderer pair with a separate container running Apache Web Server.

### Docker End to End Build

The [klcodanr/com.danklco.sample.infra](https://github.com/klcodanr/com.danklco.sample.infra) project contains a project to build a standalone composite node store Sling CMS instance.