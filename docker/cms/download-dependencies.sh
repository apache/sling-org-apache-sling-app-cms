#!/bin/bash
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

mkdir -p /opt/slingcms/features
cd /opt/slingcms

echo "Downloading Feature Launcher..."
mvn -q org.apache.maven.plugins:maven-dependency-plugin:copy \
    -Dartifact=org.apache.sling:org.apache.sling.feature.launcher:${LAUNCHER_VERSION}:jar \
    -DoutputDirectory=/opt/slingcms \
    -Dmdep.stripVersion=true \
    || { echo 'Failed to download Feature Launcher' ; exit 1; } 
 
echo "Downloading Feature Models..."
mvn -q org.apache.maven.plugins:maven-dependency-plugin:copy \
    -Dartifact=${CMS_GROUP_ID}:${CMS_ARTIFACT_ID}:${CMS_VERSION}:slingosgifeature:${FM_SEED_CLASSIFIER} \
    -DoutputDirectory=/opt/slingcms/setup \
    -Dmdep.stripVersion=true \
    || { echo 'Failed to download composite seed' ; exit 1; }
mvn -q org.apache.maven.plugins:maven-dependency-plugin:copy \
    -Dartifact=${CMS_GROUP_ID}:${CMS_ARTIFACT_ID}:${CMS_VERSION}:slingosgifeature:${FM_RUNTIME_CLASSIFIER} \
    -DoutputDirectory=/opt/slingcms \
    -Dmdep.stripVersion=true \
    || { echo 'Failed to download composite runtime' ; exit 1; }
mvn -q org.apache.maven.plugins:maven-dependency-plugin:copy \
    -Dartifact=${CMS_GROUP_ID}:${CMS_ARTIFACT_ID}:${CMS_VERSION}:slingosgifeature:${FM_RUNMODE_CLASSIFIER} \
    -DoutputDirectory=/opt/slingcms/features \
    -Dmdep.stripVersion=true \
    || { echo 'Failed to download author feature' ; exit 1; }

if [[ ! -z ${ADDITIONAL_FEATURE_COORDINATE} ]]; then
    echo "Downloading Additional Feature ${ADDITIONAL_FEATURE_COORDINATE}"
    mvn -q org.apache.maven.plugins:maven-dependency-plugin:copy \
        -Dartifact=${ADDITIONAL_FEATURE_COORDINATE} \
        -DoutputDirectory=/opt/slingcms/features \
        -Dmdep.stripVersion=true \
        || { echo "Failed to download feature ${ADDITIONAL_FEATURE_COORDINATE}" ; exit 1; }
fi