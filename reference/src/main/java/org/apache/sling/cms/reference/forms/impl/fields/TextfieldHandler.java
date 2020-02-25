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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.reference.forms.FieldHandler;
import org.apache.sling.cms.reference.forms.FormException;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = FieldHandler.class)
public class TextfieldHandler implements FieldHandler {

    private static final Logger log = LoggerFactory.getLogger(TextfieldHandler.class);
    private static final Map<String, String> typePatterns = new HashMap<>();
    static {
        typePatterns.put("date", "\\d{4}-\\d{2}-\\d{2}");
        typePatterns.put("datetime-local", "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}");
        typePatterns.put("email", ".+@.+");
        typePatterns.put("time", "\\d{2}:\\d{2}(:\\d{2})?");
    }

    private static final Map<String, String> dateFormats = new HashMap<>();
    static {
        dateFormats.put("date", "yyyy-MM-dd");
        dateFormats.put("datetime-local", "yyyy-MM-ddThh:mm");
    }

    @Override
    public boolean handles(Resource fieldResource) {
        String resourceType = fieldResource.getResourceType();
        return "reference/components/forms/fields/textfield".equals(resourceType);
    }

    @Override
    public void handleField(SlingHttpServletRequest request, Resource fieldResource, Map<String, Object> formData)
            throws FormException {
        log.trace("handleField");
        String name = FieldHandler.getName(fieldResource);
        String value = request.getParameter(name);
        if (StringUtils.isBlank(value)) {
            if (FieldHandler.isRequired(fieldResource)) {
                throw new FormException("Field " + name + " not set and is required");
            } else {
                log.debug("Ignoring unset value: {}", name);
            }
        } else {
            validateValue(fieldResource, value);
            log.debug("Setting value for: {}", name);

            String saveAs = fieldResource.getValueMap().get("saveAs", "string");

            if ("date".equals(saveAs)) {

                String type = fieldResource.getValueMap().get("type", String.class);
                if (!dateFormats.containsKey(type)) {
                    throw new FormException("Field " + name + " is not a date type");
                }
                try {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new SimpleDateFormat(dateFormats.get(type)).parse(value));
                    formData.put(name, cal);
                } catch (ParseException e) {
                    throw new FormException("Failed to parse date from " + value);
                }
            } else if ("double".equals(saveAs)) {
                try {
                    formData.put(name, Double.parseDouble(value));
                } catch (NumberFormatException nfe) {
                    throw new FormException("Failed to parse double from " + value);
                }
            } else if ("integer".equals(saveAs)) {
                try {
                    formData.put(name, Integer.parseInt(value, 10));
                } catch (NumberFormatException nfe) {
                    throw new FormException("Failed to parse integer from " + value);
                }
            } else {
                formData.put(name, value);
            }
        }

    }

    protected void validateValue(Resource fieldResource, String value) throws FormException {
        String pattern = fieldResource.getValueMap().get("pattern", String.class);
        if (StringUtils.isNotBlank(pattern) && !value.matches(pattern)) {
            throw new FormException(
                    "Field " + FieldHandler.getName(fieldResource) + " does not match pattern " + pattern);
        }
        String type = fieldResource.getValueMap().get("type", String.class);
        if (typePatterns.containsKey(type) && !value.matches(typePatterns.get(type))) {
            throw new FormException("Field " + FieldHandler.getName(fieldResource) + " is not a valid " + type);
        }

        if ("number".equals(type) && !NumberUtils.isCreatable(value)) {
            throw new FormException("Field " + FieldHandler.getName(fieldResource) + " is not a number");
        }
    }

}
