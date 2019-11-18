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
package org.apache.sling.cms.core.internal.filters;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Configuration for the CMSSecurityFilter
 */
@ObjectClassDefinition(name = "%cms.security.filter.name", description = "%cms.security.filter.description", localization = "OSGI-INF/l10n/bundle")
public @interface CMSSecurityFilterConfig {

    @AttributeDefinition(name = "%hostDomains.name", description = "%hostDomains.description")
    String[] hostDomains() default "localhost";

    @AttributeDefinition(name = "%allowedPatterns.name", description = "%allowedPatterns.description")
    String[] allowedPatterns() default { "^\\/content\\/starter/.*$", "^\\/static/.*$",
            "^\\/system\\/sling\\/form\\/login$" };

    @AttributeDefinition(name = "%group.name", description = "%group.description")
    String group();

}
