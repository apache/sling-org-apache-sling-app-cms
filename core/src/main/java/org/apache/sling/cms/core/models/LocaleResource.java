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
package org.apache.sling.cms.core.models;

import java.util.Locale;

import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.osgi.annotation.versioning.ProviderType;

/**
 * Model for retrieving the locale information from a resource with the
 * jcr:language property.
 */
@ProviderType
@Model(adaptables = Resource.class)
public class LocaleResource {

    private Locale locale;

    public LocaleResource(Resource resource) {
        this.locale = Locale
                .forLanguageTag(resource.getValueMap().get(JcrConstants.JCR_LANGUAGE, "").replace("_", "-"));
    }

    public Locale getLocale() {
        return locale;
    }
}
