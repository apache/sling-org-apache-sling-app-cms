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
yum install -y httpd
echo "Dependencies installed..."

setenforce 0
cp /vagrant_data/selinux /etc/sysconfig
echo "SELinux Disabled..."

cp /vagrant_data/cms.conf /etc/httpd/conf.d
cp /vagrant_data/site.conf /etc/httpd/conf.d
mkdir -p /var/www/vhosts/sling-cms
mkdir -p /var/www/vhosts/sling
mkdir -p /var/cache/httpd/sling
chown apache:apache -R /var/www/vhosts
chown -R apache:apache /var/cache/httpd/sling
service httpd start
echo "Apache Web Server installed and started..."

yum clean all
echo "Apache installation complete!"