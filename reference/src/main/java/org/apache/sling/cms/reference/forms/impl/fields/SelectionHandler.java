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
package org.apache.sling.cms.reference.forms.impl.fields;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.reference.forms.FieldHandler;
import org.apache.sling.cms.reference.forms.FormException;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = FieldHandler.class)
public class SelectionHandler implements FieldHandler {

    private static final Logger log = LoggerFactory.getLogger(SelectionHandler.class);

    @Override
    public boolean handles(Resource fieldResource) {
        return "reference/components/forms/fields/selection".equals(fieldResource.getResourceType());
    }

    @Override
    public void handleField(SlingHttpServletRequest request, Resource fieldResource, Map<String, Object> formData)
            throws FormException {
        log.trace("handleField");
        String name = FieldHandler.getName(fieldResource);

        if (isMultiple(fieldResource)) {
            String[] value = stripBlank(request.getParameterValues(name));
            if (value.length == 0) {
                if (FieldHandler.isRequired(fieldResource)) {
                    throw new FormException("Field " + name + " not set and is required");
                } else {
                    log.debug("Ignoring unset value: {}", name);
                }
            } else {
                log.debug("Setting value for: {}", name);
                formData.put(name, value);
            }
        } else {
            String value = request.getParameter(name);
            if (StringUtils.isBlank(value)) {
                if (FieldHandler.isRequired(fieldResource)) {
                    throw new FormException("Field " + name + " not set and is required");
                } else {
                    log.debug("Ignoring unset value: {}", name);
                }
            } else {
                log.debug("Setting value for: {}", name);
                formData.put(name, value);
            }
        }
    }

    private String[] stripBlank(String[] parameterValues) {
        return Optional.ofNullable(parameterValues).map(v -> {
            List<String> values = Arrays.stream(v).filter(StringUtils::isNotBlank).collect(Collectors.toList());
            return values.toArray(new String[values.size()]);
        }).orElse(new String[0]);
    }

    private boolean isMultiple(Resource fieldResource) {
        return fieldResource.getValueMap().get("multiple", false);
    }

}
