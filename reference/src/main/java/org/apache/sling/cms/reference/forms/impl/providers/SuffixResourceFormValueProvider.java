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
package org.apache.sling.cms.reference.forms.impl.providers;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.cms.reference.forms.FormConstants;
import org.apache.sling.cms.reference.forms.FormValueProvider;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = FormValueProvider.class)
public class SuffixResourceFormValueProvider implements FormValueProvider {

    private static final Logger log = LoggerFactory.getLogger(SuffixResourceFormValueProvider.class);

    @Override
    public void loadValues(SlingHttpServletRequest request, Resource providerResource, Map<String, Object> formData) {
        log.trace("loadValues");
        Resource suffixResource = request.getRequestPathInfo().getSuffixResource();

        ValueMap providerProperties = providerResource.getValueMap();
        StringSubstitutor sub = new StringSubstitutor(formData);
        String basePath = sub.replace(providerProperties.get("basePath", String.class));
        String[] allowedProperties = providerProperties.get(FormConstants.PN_ALLOWED_PROPERTIES, String[].class);
        if (suffixResource != null && suffixResource.getPath().startsWith(basePath)) {
            ValueMap suffixProperties = suffixResource.getValueMap();
            if (allowedProperties != null && allowedProperties.length > 0) {
                Arrays.stream(allowedProperties).filter(suffixProperties::containsKey)
                        .forEach(p -> formData.put(p, suffixProperties.get(p)));
            } else {
                formData.putAll(suffixProperties);
            }
            formData.put("suffixResource", suffixResource.getPath());
        }

    }

    @Override
    public boolean handles(Resource valueProviderResource) {
        return "reference/components/forms/providers/suffixresource".equals(valueProviderResource.getResourceType());
    }
}
