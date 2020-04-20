#!/bin/bash
#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
#
# This script configures the start information for this server.
#
# The following variables may be used to override the defaults.
#

yum update -y
yum install -y java-11-openjdk
echo "Dependencies installed..."

adduser sling
echo "Created Sling user..."

mkdir -p /opt/slingcms
curl https://search.maven.org/remotecontent?filepath=org/apache/sling/org.apache.sling.cms.builder/0.16.2/org.apache.sling.cms.builder-0.16.2.jar --output /opt/slingcms/org.apache.sling.cms.jar
cp /vagrant_data/start.sh /vagrant_data/stop.sh /opt/slingcms
chmod +x /opt/slingcms/*.sh
chown -R sling:sling /opt/slingcms
echo "Sling CMS installed..."

cp  /vagrant_data/init.sh /etc/rc.d/init.d/slingcms
chmod u+rwx /etc/rc.d/init.d/slingcms
systemctl enable slingcms.service
systemctl start slingcms.service
echo "Sling CMS service created / started..."

yum clean all
echo "Sling CMS installation complete!"
