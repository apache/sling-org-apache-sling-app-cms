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
[Apache Sling](https://sling.apache.org) > [Sling CMS](https://github.com/apache/sling-org-apache-sling-app-cms) > [Developers](developers.md) > Creating Plugins

# Creating Plugins

The Apache Sling CMS has a robust extension model for [extending the core application](extending.md).
This guide will help you to create a structure for plugins to ensure upgrade readiness.

## Application Path

Unlike custom applications, plugin code should be provided in the /libs path under names folders.
The recommended folder naming is:

    /libs
      /groupId
        /pluginId

This structure ensures that plugins from different providers do not conflict.

## Configuration

The simplest configuration option is to leverage component configuration. If you need a shared configuration, you can create a configuration console by creating a component with the component type 'SlingCMS-Config'

## Extending the UI

To extend the Apache Sling CMS UI, the recommended approach is to use Sling Resource Merger to merge your custom content under */apps/sling-cms/content* into the default content at */libs/sling-cms/content*.

All content should be exposed under the path /cms and keep in mind the naming conventions to ensure your content does not conflict with other plugins. Be as specific as possible when defining paths to avoid overriding other plugins.
