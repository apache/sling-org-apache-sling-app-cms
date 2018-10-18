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
package org.apache.sling.cms.core.readability.impl;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * An interface for configuring the %readability services for a particular
 * language
 */
@ObjectClassDefinition(name = "%readability.config.name", description = "%readability.config.description", localization = "OSGI-INF/l10n/bundle")
public @interface ReadabilityConfig {

    @AttributeDefinition(name = "%readability.param.locale.name", description = "%readability.param.locale.description")
    String locale();

    @AttributeDefinition(name = "%readability.param.vowelexp.name", description = "%readability.param.vowelexp.description")
    String vowelExpression();

    @AttributeDefinition(name = "%readability.param.extravowelexp.name", description = "%readability.param.extravowelexp.description")
    String extraVowelExpression();

    @AttributeDefinition(name = "%readability.param.wordstems.name", description = "%readability.param.wordstems.description")
    String[] wordstems();

    @AttributeDefinition(name = "%readability.param.complexitymin.name", description = "%readability.param.complexitymin.description")
    int complexityMin();

    @AttributeDefinition(name = "%readability.param.iswordexp.name", description = "%readability.param.iswordexp.description")
    String isWordExpression();
}
