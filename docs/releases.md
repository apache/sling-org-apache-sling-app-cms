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
[Apache Sling](https://sling.apache.org) > [Sling CMS](https://github.com/apache/sling-org-apache-sling-app-cms) > Releases

# Releases


## 0.16.2 - CURRENT VERSION

Bug and UI fixes.

**Note -** Breaking change in correcting Sling namespace URL. To load a package from previous versions of Sling CMS into 0.16.2+, run the script found here:

https://github.com/apache/sling-org-apache-sling-app-cms/blob/master/builder/src/main/scripts/fix_package_namespace.sh

e.g.:

`/bin/sh fix_package_namespace.sh [package.zip]`


 * [View Release](https://github.com/apache/sling-org-apache-sling-app-cms/releases/org.apache.sling.cms-0.16.2)
 * [API JavaDoc](http://javadoc.io/doc/org.apache.sling/org.apache.sling.cms.api/0.16.2)

## 0.16.0

Added support for LDAP authentication, UI tweaks and bug fixes.

#### [CVE Advisory: CVE-2020-1949 - Improper Neutralization of Input During Web Page Generation](https://s.apache.org/CVE-2020-1949)

Scripts in Sling CMS do not property escape the Sling Selector from URLs when generating navigational elements for the administrative consoles and are vulnerable to reflected XSS attacks.

#### Remediation

Upgrade to Sling CMS 0.16.0.

 * [View Release](https://github.com/apache/sling-org-apache-sling-app-cms/releases/org.apache.sling.cms-0.16.0)
 * [API JavaDoc](http://javadoc.io/doc/org.apache.sling/org.apache.sling.cms.api/0.16.0)
 * [Documentation](https://github.com/apache/sling-org-apache-sling-app-cms/tree/5877660e1f5ad8626b1974df90f8d0a2c1da3d97)

## 0.14.0

Focus on continued improvements to user experience, including significant improvments to the look and feel of the sites screens. Cleaned up inconsistencies in the breadcrumbs. New drag and drop re-ordering and added basic editable forms and significantly improved image transformations.

 * [View Release](https://github.com/apache/sling-org-apache-sling-app-cms/releases/org.apache.sling.cms-0.14.0)
 * [API JavaDoc](http://javadoc.io/doc/org.apache.sling/org.apache.sling.cms.api/0.14.0)
 * [Documentation](https://github.com/apache/sling-org-apache-sling-app-cms/tree/c45c70c207924d40b5f2cdca9b65374428d2ec3d)

## 0.12.0

Focus on continued improvements to user experience, including significant optimizations to front end page performance and accessibility improvements. HTML5 support in rewriter pipelines. Improvements around file support including metadata extraction and thumbnail support. 

 * [View Release](https://github.com/apache/sling-org-apache-sling-app-cms/releases/org.apache.sling.cms-0.12.0)
 * [API JavaDoc](http://javadoc.io/doc/org.apache.sling/org.apache.sling.cms.api/0.12.0)
 * [Documentation](https://github.com/apache/sling-org-apache-sling-app-cms/tree/5d6043e23efcfee80dbac4ceca84247c7e296381)

## 0.11.2

Fixes issues in 0.11.0

 * [View Release](https://github.com/apache/sling-org-apache-sling-app-cms/releases/org.apache.sling.cms-0.11.2)
 * [API JavaDoc](http://javadoc.io/doc/org.apache.sling/org.apache.sling.cms.api/0.11.2)
 * [Documentation](https://github.com/apache/sling-org-apache-sling-app-cms/tree/d4bdcc313f78e882234d5ac9d9af64712c46c726)

## 0.11.0

Adding API for content insights. Switching to a more flexible WYSIWYG library. Significant enhancements and bug fixes to the overall user experience.

 * [View Release](https://github.com/apache/sling-org-apache-sling-app-cms/releases/tag/org.apache.sling.cms-0.11.0)
 * [API JavaDoc](http://javadoc.io/doc/org.apache.sling/org.apache.sling.cms.api/0.11.0)
 * [Documentation](https://github.com/apache/sling-org-apache-sling-app-cms/tree/f2c7c37bf99401aef192db1ac1467c799b3a3b95)
 
## 0.10.0

Major UI update to use Bulma framework. Significant enhancements and updates. Update to separate out API from core code.

 * [View Release](https://github.com/apache/sling-org-apache-sling-app-cms/releases/tag/org.apache.sling.cms-0.10.0)
 * [JavaDoc](http://javadoc.io/doc/org.apache.sling/org.apache.sling.cms.api/0.10.0)
 * [Documentation](https://github.com/apache/sling-org-apache-sling-app-cms/tree/96879b7e912d9831f2f4e27811bb51bdd7bf4ff0)

## 0.9.0

Initial release of Sling CMS, focus on core functionality.

 * [Download](https://search.maven.org/remotecontent?filepath=org/apache/sling/org.apache.sling.cms.builder/0.10.0/org.apache.sling.cms.builder-0.9.0.jar)
 * [JavaDoc](http://javadoc.io/doc/org.apache.sling/org.apache.sling.cms.core/0.9.0)
 * [Documentation](https://github.com/apache/sling-org-apache-sling-app-cms/tree/1afa5da54257cad8a5bf4b28d76b88d13838433b)
