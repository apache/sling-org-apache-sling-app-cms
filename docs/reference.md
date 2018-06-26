[Apache Sling](https://sling.apache.org) > [Sling CMS](https://github.com/apache/sling-org-apache-sling-app-cms) > [Developers](developers.md) > Reference Project

# Reference Project

The reference project provides a number of example components to use, develop on or extend.

These components are:

 - base page - a basic page using the Apache Sling website styling
 - breadcrumb - breadcrumb based on the page hierarchy
 - cta - a CTA link
 - codeblock - displays a block of code in a `<pre>` tag
 - columncontrol - allows authors to create columns of content
 - iframe - allows for adding an iframe				stylewrapper.json
 - image - a block level image allowing linked and styled images
 - list	- a base component for creating dynamic lists
 - search - a simple search component using JCR Queries to search page content	
 - sitemap - renders an XML sitemap
 - stylewrapper - wraps in a div with a defineable set of selectable styles
 - suffixheader - displays a header based on the jcr:title of the resource in the Sling Suffix
 - rss - renders an RSS feed backed on a query
 - tags - displays the taxonomy tags on a page
 
In addition to these reference components, there are two general use components in the main ui project. These are found at /libs/sling-cms/components/general and are:

 - container - a container into which other components can be aded. This is the most foundational component in Sling CMS allowing for Component-drive content development.
 - rte - a HTML based rich text editor using Summernote as the RTE