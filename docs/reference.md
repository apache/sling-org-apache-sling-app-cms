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
[Apache Sling](https://sling.apache.org) > [Sling CMS](https://github.com/apache/sling-org-apache-sling-app-cms) > [Developers](developers.md) > Reference Project

# Reference Project

The reference project provides a number of example components to use, develop on or extend.

These components are:

## General

 - base page - a basic page using the Apache Sling website styling
 - breadcrumb - breadcrumb based on the page hierarchy
 - cta - a call to action link / button
 - codeblock - displays a block of code in a `<pre>` tag
 - columncontrol - allows authors to create columns of content
 - form - adds a form to a page
 - iframe - allows for adding an iframe
 - image - a block level image allowing linked and styled images
 - list	- a base component for creating dynamic lists
 - search - a simple search component using JCR Queries to search page content	
 - sitemap - renders an XML sitemap
 - stylewrapper - wraps in a div with a defineable set of selectable styles
 - suffixheader - displays a header based on the jcr:title of the resource in the Sling Suffix
 - rss - renders an RSS feed backed on a query
 - tags - displays the taxonomy tags on a page
 
 
In addition to these reference components, there are two general use components in the main ui project. These are found at `/libs/sling-cms/components/general` and are:

 - container - a container into which other components can be added. This is the most foundational component in Sling CMS allowing for component-driven content development
 - rte - a HTML based rich text editor using [wysihtml](http://wysihtml.com/) as the RTE
 
## Form Components

There are a number of components specifically for configuring the form.

**Form Value Provider**

 - userprofile - Loads form field values from the current user's profile subnode
 
**Form Field**

 - fieldset - displays a HTML Fieldset for grouping fields, with an optional legend
 - honeypot - adds a spam-blocking honeypot to the form, should be hidden from view, any submission with this field filled out will be blocked
 - selection - allows for the users to select from options defined in tags, can be rendered as radio, checkboxes or a dropdown
 - textfield - basic text field with support for HTML5 types
 - textarea - basic text area