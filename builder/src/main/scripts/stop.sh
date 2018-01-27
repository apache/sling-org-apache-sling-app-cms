#!/bin/bash
#
# This script stops the (running) application
#

START_OPTS='stop -c .'
JARFILE=`ls *cms*.jar | head -1`

java -jar $JARFILE $START_OPTS
STOP_CODE=$?
if [ "${STOP_CODE}" == "0" ]; then
	echo "Application not running"
else
	echo "Stop command returned ${STOP_CODE}. Trying to kill the process..."
	PID=$(cat app.pid 2>/dev/null)
	rm -f app.pid
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
fi
exit ${STOP_CODE}