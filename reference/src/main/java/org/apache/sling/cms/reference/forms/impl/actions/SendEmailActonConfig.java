/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sling.cms.reference.forms.impl.actions;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "%cms.reference.sendemail.name", description = "%cms.reference.sendemail.description", localization = "OSGI-INF/l10n/bundle")
public @interface SendEmailActonConfig {
    
    @AttributeDefinition(name = "%cms.reference.sendemail.hostName.name", description = "%cms.reference.sendemail.hostName.description")
    String hostName();

    @AttributeDefinition(name = "%cms.reference.sendemail.smtpPort.name", description = "%cms.reference.sendemail.smtpPort.description")
    int smtpPort();

    @AttributeDefinition(name = "%cms.reference.sendemail.tlsEnabled.name", description = "%cms.reference.sendemail.tlsEnabled.description")
    boolean tlsEnabled();

    @AttributeDefinition(name = "%cms.reference.sendemail.username.name", description = "%cms.reference.sendemail.username.description")
    String username();

    @AttributeDefinition(name = "%cms.reference.sendemail.password.name", description = "%cms.reference.sendemail.password.description", type=AttributeType.PASSWORD)
    String password();
}
