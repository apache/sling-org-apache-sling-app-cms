[Apache Sling](https://sling.apache.org) > [Sling CMS](https://github.com/apache/sling-org-apache-sling-app-cms) > [Developers](developers.md) > Project Intro

# Project Intro

This page will give you an introduction of the structure of the Sling CMS project and some idea of how all of this comes together. If you aren't planning on extending or contributing to Sling CMS, this probably isn't necessary fot you.

## Project Structure

The Sling CMS project has three main modules:

 - builder - this is a Sling Starer builder, it contains the repoinit text files which define the dependencies and requirements to build Sling CMS. This will ultimately build the Sling CMS Jar.
 - core - this is the Java code behind Sling CMS. This includes the Sling Models, Filters, Servlets, Post Operations and Rewriter code.
 - reference - this is a reference application for developers to use to extend Sling CMS, this includes both Java code and content loaded by the Sling Content Loader
 - ui - this is a bulk of the content and scripts for the Sling CMS. Most of this is located under */libs/sling-cms* although there are some other directories for configurations
 
## Front End Code

Sling CMS uses Gulp to build the front end code which is them packaged by Maven into the overall project build. This can be a bit chatty over the network if you run `mvn clean install` with every build, so unless you are updating the front end code, it's often better to just run `mvn install`

## Important Directories

Most of the scripts are installed under the directory */libs/sling-cms/components* and the contet is under */libs/sling-cms/content* 

## CMS Content

The Sling CMS uses Sling Resource Merge to allow developers to overlay content provided in the base CMS. THis means that although the default content is stored in */libs/sling-cms/content* it is actually used from */mnt/overlay/sling-cms/content* a default Resource Rsolver Factory configuration them maps this path to */cms* for shorter URLs.