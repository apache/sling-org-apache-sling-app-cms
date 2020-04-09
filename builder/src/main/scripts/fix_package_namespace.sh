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
# This script fixes Jackrabbit content packages created on Sling CMS < 0.16.2 
# which have an incorrect URL for the Sling namespace
#

PACKAGE=$1
unzip $PACKAGE -d out
IFS=$'\n'
for CONTENT_XML in $(find out -name .content.xml) 
do
    echo "Replacing Sling Namespace in ${CONTENT_XML}"
    sed -i "" "s|http\://www.sling.apache.org/sling/1.0|http://sling.apache.org/jcr/sling/1.0|g" "$CONTENT_XML"
done
for CND in $(find . -name *.cnd) 
do
    echo "Replacing Sling Namespace in ${CND}"
    sed -i "" "s|http\://www.sling.apache.org/sling/1.0|http://sling.apache.org/jcr/sling/1.0|g" "$CND"
done

cd out
zip -r "../replaced-$PACKAGE" * 
cd ..
rm -rf out
echo "Sling Namespace replaced successfully in replaced-$PACKAGE"