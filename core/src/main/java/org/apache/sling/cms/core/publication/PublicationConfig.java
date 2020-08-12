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
package org.apache.sling.cms.core.publication;

import org.apache.sling.cms.publication.PUBLICATION_MODE;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "%publication.config.name", description = "%publication.config.description", localization = "OSGI-INF/l10n/bundle")
public @interface PublicationConfig {

    @AttributeDefinition(name = "%publication.param.mode.name", description = "%publication.param.mode.description")
    PUBLICATION_MODE publicationMode() default PUBLICATION_MODE.STANDALONE;

    @AttributeDefinition(name = "%publication.param.agents.name", description = "%publication.param.agents.description")
    String[] agents() default {};
}
