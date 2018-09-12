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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import javax.jcr.query.Query;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.osgi.annotation.versioning.ProviderType;

/**
 * A model retrieving all of the keys for a i18n dictionary
 */
@ProviderType
@Model(adaptables = Resource.class)
public class i18nHelper {

    private Resource resource;

    private Set<String> keys = new HashSet<>();

    private Random rand = new Random();

    public i18nHelper(Resource resource) {
        this.resource = resource;
    }

    public Set<String> getKeys() {
        if (keys.isEmpty()) {
            Iterator<Resource> messageEntries = resource.getResourceResolver().findResources(
                    "SELECT * FROM [sling:MessageEntry] AS s WHERE ISDESCENDANTNODE([" + resource.getPath() + "])",
                    Query.JCR_SQL2);
            while (messageEntries.hasNext()) {
                Resource entry = messageEntries.next();
                keys.add(entry.getValueMap().get("sling:key", String.class));
            }
        }
        return keys;
    }

    public String getRandom() {
        return String.valueOf(rand.nextInt());
    }

}
