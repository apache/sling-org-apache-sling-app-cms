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
[Apache Sling](https://sling.apache.org) > [Sling CMS](https://github.com/apache/sling-org-apache-sling-app-cms) > [Administration](administration.md) > Error Pages

# Error Pages

When a user encounters an error on a Sling CMS site, Sling CMS will attempt to provide a relevant error page by performing the following steps in order:

 - Check for a `sling:Page` at */content/[SITE_GROUP]/[SITE_NAME]/errors/[STATUS_CODE]*
 - Check for a `sling:Page` at */content/[SITE_GROUP]/[SITE_NAME]/errors/default*
 - Check for a `sling:Page` at */content/sling-cms/errorhandling/[STATUS_CODE]*
 - Check for a `sling:Page` at */content/sling-cms/errorhandling/default*
 
This allows you to customize the response to an error while not providing too much information to your users. For example, if you provide the following structure:

        /content
            /sites
                /mysite
                    /error
                        /403
                        /500
                        /default

A user who encounters an application error (status code 500) would see the content of 500, an unauthorized user would see 403 and all other users who encounter errors would see default.

To prevent the Sling CMS error handling page to be displayed on your site, you should specify at least a default error page for every site.

## Sling CMS Server Down Error Pages

There are various reasons that a Sling CMS application server could become unavailable, and you do not want to leave users unsure what is going on. 

To alleviate this issue you should provide error pages for HTTP Status codes 502-504. These pages cannot be derived from Sling CMS as they will be requested only when Sling CMS is unavailable or not responding as expected. 

Instead, you can configure these pages to be ignored by mod_proxy and served directly from the Apache Web Server.

This takes a few steps:

1. Add the following directive into your mod_proxy configuration:
    
        ProxyPass /ERROR !
    
This instructs mod_proxy to ignore the directory /ERROR
    
2. Add the following directive into your conf file:

        # Configure Error Documents if Down
        ErrorDocument 502 /ERROR/502.html
        ErrorDocument 503 /ERROR/503.html
        ErrorDocument 504 /ERROR/504.html
        Alias /ERROR /var/www/vhosts/site
    
3. Create the relevant pages under /var/www/vhosts/site

The error pages should be static HTML, ideally with all CSS, images, etc inlined. You don't necessarily need to provide separate error pages for each status code.