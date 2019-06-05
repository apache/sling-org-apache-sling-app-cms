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
# Apache Sling CMS Dockerfile


# Pull base image.
FROM openjdk:8-jre
MAINTAINER dev@sling.apache.org

# Configure directories
WORKDIR /opt/sling
VOLUME /opt/sling/sling

# Setup Sling CMS
RUN mkdir -p /opt/sling
RUN wget -O org.apache.sling.cms.jar https://search.maven.org/remotecontent?filepath=org/apache/sling/org.apache.sling.cms.builder/0.12.0/org.apache.sling.cms.builder-0.12.0.jar
ENV JAVA_OPTS -Xmx512m
ENV SLING_OPTS ''

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
EXPOSE 80 443

# Background Apache httpd and start Sling CMS
CMD exec service apache2 start & service apache-htcacheclean start & java $JAVA_OPTS -jar org.apache.sling.cms.jar $SLING_OPTS
