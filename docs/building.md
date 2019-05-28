<!-- Licensed to the Apache Software Foundation (ASF) under one or more contributor 
	license agreements. See the NOTICE file distributed with this work for additional 
	information regarding copyright ownership. The ASF licenses this file to 
	you under the Apache License, Version 2.0 (the "License"); you may not use 
	this file except in compliance with the License. You may obtain a copy of 
	the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required 
	by applicable law or agreed to in writing, software distributed under the 
	License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS 
	OF ANY KIND, either express or implied. See the License for the specific 
	language governing permissions and limitations under the License. -->
[Apache Sling](https://sling.apache.org) > [Sling CMS](https://github.com/apache/sling-org-apache-sling-app-cms) > [Developers](developers.md) > Building Sling CMS

# Building Sling CMS

To build an instance with of the Sling CMS, you'll need Apache Maven 3.0+ and Git.

First clone the code:

    git clone https://github.com/apache/sling-org-apache-sling-app-cms.git
    
Next build the code with:

    mvn clean install
    
The JAR will be located under: `builder/target/org.apache.sling.cms-{VERSION].jar`

## Running

To run the Sling CMS, build the code and copy the files `builder/src/main/scripts/start.sh` `builder/src/main/scripts/stop.sh` and `builder/target/org.apache.sling.cms-{VERSION].jar` to a directory. Execute the script `./start.sh` to start Sling CMS.

## Login

Navigate to [http://localhost:8080/](http://localhost:8080/). The default credentials are *admin*/*admin*.
