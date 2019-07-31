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

# port used for accessing the app
if [ -z "$APP_PORT" ]; then
	APP_PORT=8080
fi

# default JVM options
if [ -z "$APP_JVM_OPTS" ]; then
	APP_JVM_OPTS='-server -Xmx1024m -XX:MaxPermSize=256M -Djava.awt.headless=true'
fi

# debugging support
if [ -n "${APP_DEBUG_PORT}" ]; then
	APP_JVM_OPTS="${APP_JVM_OPTS} -agentlib:jdwp=transport=dt_socket,server=y,address=${APP_DEBUG_PORT},suspend=n"
fi

# ------------------------------------------------------------------------------
# do not configure below this point
# ------------------------------------------------------------------------------

if [ $APP_PORT ]; then
	START_OPTS="${START_OPTS} -p ${APP_PORT}"
fi
START_OPTS="${START_OPTS}"

SCRIPTPATH="$( cd "$(dirname "$0")" ; pwd -P )"
JARFILE=`ls ${SCRIPTPATH}/*cms*.jar | head -1`
cd ${SCRIPTPATH}
mkdir -p sling/logs
(
  (
    java $APP_JVM_OPTS -jar $JARFILE $START_OPTS &
    echo $! > app.pid
  ) >> sling/logs/stdout.log 2>&1
) &
echo "Application started on port ${APP_PORT}!"