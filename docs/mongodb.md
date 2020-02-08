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
[Apache Sling](https://sling.apache.org) > [Sling CMS](https://github.com/apache/sling-org-apache-sling-app-cms) > [Administration](administration.md) > MongoDB

# MongoDB

By default, Sling CMS stores it's content in a local Tar-backed Apache Oak repository.

When you setup your Sling CMS instance, you can configure Sling CMS to use a local MongoDB instance by adding the following parameter to the start script:

    -Dsling.run.modes=oak_mongo

This will connect to a MongoDB instance running on localhost:27017 with the database name sling.

If you want to specify a different port or database name, you can also provide the following properties:

    -Doak.mongo.uri={URI} -Doak.mongo.db={DB_NAME}

See Sling's [Configuration documentation](https://sling.apache.org/documentation/configuration.html) for more details on how to provide these configuration values.

## Reference Docker Configuration

The [reference Docker configuration](https://github.com/apache/sling-org-apache-sling-app-cms/tree/master/docker) demonstrates how Sling CMS could be deployed using MongoDB in a containerized environment.
