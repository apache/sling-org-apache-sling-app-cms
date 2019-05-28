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

# Extending Sling CMS

Developers can  extend or even override any portion of the Sling CMS application to suit their needs. 

Depending on what you need to extend or override, there are two different options. If you want to override the content in Sling CMS, you can use [Sling Resource Merger](https://sling.apache.org/documentation/bundles/resource-merger.html) to customize any of the content in Sling CMS. If you need to overlay one of the scripts, you can overlay the scripts using the Sling script overlays.

## Determining the Correct Approach

To determine the correct approach, first determine what you are looking to overlay. Sling CMS uses Sling Mappings to map the short path */cms* to the Sling repository path */libs/sling-cms/content*, so can use this same pattern to determine the path of the content serving a CMS page, For example the resource serving */cms/start.html* would be found at */libs/sling-cms/content/start*.

In most cases, you can then use [Sling Resource Merger](https://sling.apache.org/documentation/bundles/resource-merger.html) to provide overriding content. If you cannot achieve your goals by providing different content, you can instead override the rending script by providing an alternative script at /apps[sling:resourceType]