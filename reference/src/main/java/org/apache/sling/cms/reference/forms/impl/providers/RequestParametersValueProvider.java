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

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.reference.forms.FormValueProvider;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = FormValueProvider.class)
public class RequestParametersValueProvider implements FormValueProvider {

    private static final Logger log = LoggerFactory.getLogger(RequestParametersValueProvider.class);

    @Override
    public void loadValues(SlingHttpServletRequest request, Resource providerResource, Map<String, Object> formData) {
        log.trace("loadFormData");
        String[] parameters = providerResource.getValueMap().get("allowedParameters", String[].class);
        if (parameters != null) {
            Arrays.stream(parameters).forEach(p -> {
                if (request.getParameter(p) != null) {
                    if (request.getParameterValues(p).length > 1) {
                        formData.put(p, request.getParameterValues(p));
                    } else {
                        formData.put(p, request.getParameter(p));
                    }
                }
            });
        }

    }

    @Override
    public boolean handles(Resource valueProviderResource) {
        return "reference/components/forms/providers/requestparameters".equals(valueProviderResource.getResourceType());
    }
}
