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
[Apache Sling](https://sling.apache.org) > [Sling CMS](https://github.com/apache/sling-org-apache-sling-app-cms) > [Administration](administration.md) > Sling CMS Apache Web Server Setup

# Sling CMS Apache Web Server Setup

To set up Apache Web Server to proxy requests to Sling CMS, you can add a configuration similar to the one below:

    <VirtualHost *:80>
       ServerName cms.sling.apache.org
       DocumentRoot /var/www/vhosts/sling-cms

       ProxyPass /.well-known !
       ProxyPass / http://localhost:8080/
       ProxyPassReverse / http://localhost:8080/
    </VirtualHost>

This allows you to put a layer in front of Sling CMS to enable additional security checks, provide caching and use a friendly URL.