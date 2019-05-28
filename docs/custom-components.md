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
[Apache Sling](https://sling.apache.org) > [Sling CMS](https://github.com/apache/sling-org-apache-sling-app-cms) > [Developers](developers.md) > Custom Components

# Custom Components

Components in Sling CMS are used to render data stored in the Sling Repository to users. Each component is denoted by the `jcr:primaryType` of `sling:Component`. 

Components are expected to have the following structure:

    /component
        jcr:primaryType=sling:Component
        jcr:title=My Component
        componentType=[Component_Type]
        /edit
            jcr:primaryType=nt:unstructured
            sling:resourceType=sling-cms/components/editor/slingform
            [...]
        /config
            jcr:primaryType=nt:unstructured
            sling:resourceType=sling-cms/components/editor/slingform
            [...]
        /component.jsp
            jcr:primaryType=nt:file
            
## Component Type

The `componentType` attribute defines the component type groups the component belongs to. This is used to group the components when adding a new component and to control what components can be added within a container. 

A component can belong to one or more groups. If a component belongs to no groups it will not be addable to any containers.

## Editor

The editor is defined by the edit sub-node and is required for a component to be addable to a container.

This node structure will be used to define the form for editing an instance of the component. This node should have the resource type `sling-cms/components/editor/slingform` and should have a button attribute.

See the [editor field types](editor-field-types.md) for a list of the default field types.

## Config

The config is defined by the config sub-node and is not required. 

This node structure will be used to define the form for editing a shared configuration for that component for every page instance using the same template. This node should have the resource type `sling-cms/components/editor/slingform` and should have a button attribute.

This config form can be accessed when adding a component configuration for this component to a template.

See the [editor field types](editor-field-types.md) for a list of the default field types.

## Scripts

Any number of scripts can be used to render the component to user. See [Sling Resource Resolution](https://sling.apache.org/documentation/the-sling-engine/url-to-script-resolution.html) for more details on how the scripts are resolved. Sling CMS supports JSP, HTL and ECMAScript.