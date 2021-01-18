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

import java.lang.annotation.Annotation;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.reference.forms.FieldHandler;
import org.apache.sling.cms.reference.forms.FormException;
import org.apache.sling.cms.reference.forms.FormUtils;
import org.apache.sling.cms.reference.forms.impl.fields.TextareaHandler.Config;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = FieldHandler.class)
@Designate(ocd = Config.class)
public class TextareaHandler implements FieldHandler {

    public static final String DEFAULT_RESOURCE_TYPE = "reference/components/forms/fields/textarea";

    private static final Logger log = LoggerFactory.getLogger(TextareaHandler.class);

    private Config config;

    @ObjectClassDefinition(name = "%cms.reference.textarea.name", description = "%cms.reference.textarea.description", localization = "OSGI-INF/l10n/bundle")
    public @interface Config {

        @AttributeDefinition(name = "%cms.reference.supportedTypes.name", description = "%cms.reference.supportedTypes.description", defaultValue = {
                DEFAULT_RESOURCE_TYPE })
        String[] supportedTypes() default { DEFAULT_RESOURCE_TYPE };
    }

    @Activate
    public TextareaHandler(Config config) {
        this.config = config;
    }

    @Override
    public boolean handles(Resource fieldResource) {
        return FormUtils.handles(config.supportedTypes(), fieldResource);
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
            log.debug("Setting value for: {}", name);
            formData.put(name, value);
        }
    }

}
