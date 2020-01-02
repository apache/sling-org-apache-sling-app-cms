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
# ${appName} Apache Sling CMS Project

An OSGi Bundle project created for the Apache Sling CMS. This project includes a
 simple client library using Bootstrap, a site configuration, a simple app with
 a page template and a HelloWorld Sling Model.

## Building

This project requires Apache Maven 3 and Java JDK8. To build the project run:

`mvn clean install`

To install the bundle into a local Sling CMS instance running on port 8080, run:

`mvn clean install -P autoInstallBundle`
