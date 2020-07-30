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

ARG FM_GROUP_ID=org.apache.sling
ARG FM_ARTIFACT_ID=org.apache.sling.cms.feature
ARG FM_VERSION=0.16.3-SNAPSHOT
ARG FM_SEED_CLASSIFIER=slingcms-composite-seed
ARG FM_RUNTIME_CLASSIFIER=slingcms-composite-seed
ENV FM_ARTIFACT_ID=${FM_ARTIFACT_ID}
ENV FM_RUNTIME_CLASSIFIER=${FM_RUNTIME_CLASSIFIER}

# Configure directories
WORKDIR /opt/slingcms

# Install Apache Maven
RUN apt-get update && apt-get install maven -y
COPY /settings.xml /root/.m2/

# Setup Sling CMS
COPY download-dependencies.sh /opt/slingcms/setup/
RUN /bin/bash /opt/slingcms/setup/download-dependencies.sh
COPY setup-composite.sh /opt/slingcms/setup/
RUN /bin/bash /opt/slingcms/setup/setup-composite.sh

# Expose endpoints
EXPOSE 8080

# Start Sling CMS
CMD exec java -jar org.apache.sling.feature.launcher.jar \
    -f ${FM_ARTIFACT_ID}-${FM_RUNTIME_CLASSIFIER}.slingosgifeature
