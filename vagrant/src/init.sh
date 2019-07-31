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
#
# chkconfig: 35 85 15
# description: This service manages the Sling CMS java process.
# processname: slingcms
# pidfile: ${SLING_ROOT}/app.pid
 
# Source function library.
. /etc/rc.d/init.d/functions
 
SCRIPT_NAME=`basename $0`
SLING_ROOT=/opt/slingcms
SLING_USER=sling
 
########
START=${SLING_ROOT}/start.sh
STOP=${SLING_ROOT}/stop.sh
 
case "$1" in
start)
echo -n "Starting Sling CMS services: "
su - ${SLING_USER} ${START}
touch /var/lock/subsys/${SCRIPT_NAME}
;;
stop)
echo -n "Shutting down Sling CMS services: "
su - ${SLING_USER} ${STOP}
rm -f /var/lock/subsys/${SCRIPT_NAME}
;;
restart)
su - ${SLING_USER} ${STOP}
su - ${SLING_USER} ${START}
;;
reload)
;;
*)
echo "Usage: ${SCRIPT_NAME} {start|stop|reload}"
exit 1
;;
esac