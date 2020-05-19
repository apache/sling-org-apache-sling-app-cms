#
#		 Licensed to the Apache Software Foundation (ASF) under one or more contributor license
#        agreements. See the NOTICE file distributed with this work for additional information
#        regarding copyright ownership. The ASF licenses this file to you under the Apache License,
#        Version 2.0 (the "License"); you may not use this file except in compliance with the
#        License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
#        Unless required by applicable law or agreed to in writing, software distributed under the
#        License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
#        either express or implied. See the License for the specific language governing permissions
#        and limitations under the License.
#
# Apache Sling CMS Web Cache Dockerfile

# Pull base image.
FROM ubuntu:latest
MAINTAINER dev@sling.apache.org

# Avoid interaction with tzdata
ARG DEBIAN_FRONTEND=noninteractive

# Install Apache
RUN apt-get update
RUN apt-get install apache2 -y

# Configure mod_rewrite
RUN a2enmod rewrite

# Configure mod_proxy
RUN a2enmod proxy
RUN a2enmod proxy_http

# Configure mod_cache and mod_expire
RUN a2enmod cache
RUN a2enmod cache_disk
RUN a2enmod expires
RUN a2enmod headers

# Configure sites
RUN mkdir -p /var/www/vhosts/sling-cms
RUN mkdir -p /var/www/vhosts/sling
COPY cms.conf /etc/apache2/sites-enabled
COPY site.conf /etc/apache2/sites-enabled

# Expose endpoints
EXPOSE 80

# Start Apache httpd
RUN service apache-htcacheclean start
CMD ["/usr/sbin/apache2ctl", "-DFOREGROUND"]
