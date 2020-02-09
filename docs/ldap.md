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
[Apache Sling](https://sling.apache.org) > [Sling CMS](https://github.com/apache/sling-org-apache-sling-app-cms) > [Administration](administration.md) > LDAP Authentication

# LDAP Authentication

LDAP Authentication is provided via the [Apache Jackrabbit Oak LDAP Integration](https://jackrabbit.apache.org/oak/docs/security/authentication/ldap.html). There are three steps to configure the integration:

1. Create an [Apache Jackrabbit Oak LDAP Identity Provider configuration](https://jackrabbit.apache.org/oak/docs/security/authentication/ldap.html#LDAP_Configuration)
2. Create a [Default Sync Handler](https://jackrabbit.apache.org/oak/docs/security/authentication/external/defaultusersync.html)
3. Create an [External Login Module](https://jackrabbit.apache.org/oak/docs/security/authentication/externalloginmodule.html#Configuration_Parameters)

## Large Numbers of Groups and users

For implementations with large numbers of users and groups, [Dynamic Group Membership](https://jackrabbit.apache.org/oak/docs/security/authentication/external/dynamic.html) can help ensure performance by essentially inverting the authentication paradigm to store the user's group membership on a protected property `rep:externalPrincipalNames`.

## Example Configuration

The following example configuration shows how to setup LDAP Authentication.

1. Setup the Docker image [rroemhild/test-openldap](https://github.com/rroemhild/docker-test-openldap)
2. Create the following configurations:

org.apache.jackrabbit.oak.security.authentication.ldap.impl.LdapIdentityProvider.[id].config

      userPool.maxActive=L"8"
      searchTimeout="60s"
      host.name="localhost"
      customattributes=[""]
      adminPool.maxActive=L"8"
      group.makeDnPath=B"false"
      user.baseDN="dc\=planetexpress,dc\=com"
      group.objectclass=["Group"]
      user.objectclass=["person"]
      userPool.lookupOnValidate=B"true"
      host.noCertCheck=B"false"
      user.makeDnPath=B"false"
      bind.dn="cn\=admin,dc\=planetexpress,dc\=com"
      group.baseDN="dc\=planetexpress,dc\=com"
      group.extraFilter=""
      user.extraFilter=""
      host.port=I"389"
      bind.password="GoodNewsEveryone"
      adminPool.lookupOnValidate=B"true"
      useUidForExtId=B"false"
      group.nameAttribute="cn"
      provider.name="ldap"
      host.ssl=B"false"
      host.tls=B"false"
      user.idAttribute="uid"
      group.memberAttribute="uniquemember"

org.apache.jackrabbit.oak.spi.security.authentication.external.impl.DefaultSyncHandler.[id].config

      group.pathPrefix=""
      user.dynamicMembership=B"false"
      group.expirationTime="1d"
      user.membershipExpTime="1h"
      user.pathPrefix=""
      user.propertyMapping=["rep:fullname\=cn"]
      handler.name="default"
      enableRFC7613UsercaseMappedProfile=B"false"
      user.autoMembership=["administrators"]
      user.expirationTime="1h"
      group.propertyMapping=[""]
      group.autoMembership=[""]
      user.disableMissing=B"false"
      user.membershipNestingDepth=I"1"

org.apache.jackrabbit.oak.spi.security.authentication.external.impl.ExternalLoginModuleFactory.[id].config

      jaas.controlFlag="SUFFICIENT"
      jaas.ranking=I"99999"
      sync.handlerName="default"
      jaas.realmName=""
      idp.name="ldap"

3. You should now be able to login with the credentials: professor/professor
