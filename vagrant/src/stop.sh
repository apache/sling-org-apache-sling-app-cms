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
# This script stops the (running) application
#

START_OPTS='stop -c .'
SCRIPTPATH="$( cd "$(dirname "$0")" ; pwd -P )"
JARFILE=`ls ${SCRIPTPATH}/*cms*.jar | head -1`

java -jar $JARFILE $START_OPTS
STOP_CODE=$?
if [ "${STOP_CODE}" == "0" ]; then
	echo "Application not running"
else
	echo "Stop command returned ${STOP_CODE}. Trying to kill the process..."
	PID=$(cat ${SCRIPTPATH}/app.pid 2>/dev/null)
	if [ "$PID" ]; then
		if ps -p $PID > /dev/null 2>&1; then
			kill $PID
			STOP_CODE=$?
			echo "process ${PID} was killed"
		else
       		echo "process ${PID} not running"
	       	STOP_CODE=4
	    fi
	else
		echo "app.pid not found"
		STOP_CODE=4
	fi
    rm -f ${SCRIPTPATH}/app.pid
fi
exit ${STOP_CODE}