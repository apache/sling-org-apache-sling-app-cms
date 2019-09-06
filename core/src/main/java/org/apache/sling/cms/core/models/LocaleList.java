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

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.osgi.annotation.versioning.ProviderType;

/**
 * A model for representing a site.
 */
@ProviderType
@Model(adaptables = SlingHttpServletRequest.class)
public class LocaleList {

    public List<Locale> getLocales() {
        List<Locale> locales = Arrays.asList(DateFormat.getAvailableLocales());
        Collections.sort(locales, (o1,o2) ->  o1.toString().compareTo(o2.toString()));
        return locales;
    }

}
