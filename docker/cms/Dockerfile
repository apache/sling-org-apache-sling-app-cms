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
FROM adoptopenjdk:11-jre-openj9
MAINTAINER dev@sling.apache.org

# Configure directories
WORKDIR /opt/sling
VOLUME /opt/sling/sling

# Setup Sling CMS
RUN mkdir -p /opt/sling
RUN curl -L https://search.maven.org/remotecontent?filepath=org/apache/sling/org.apache.sling.cms.builder/0.16.2/org.apache.sling.cms.builder-0.16.2.jar --output org.apache.sling.cms.jar

# Expose endpoints
EXPOSE 8080

# Start Sling CMS
CMD exec java -Dsling.run.modes=oak_mongo -Doak.mongo.uri=mongodb://mongo:27017 -Doak.mongo.db=sling -jar org.apache.sling.cms.jar
