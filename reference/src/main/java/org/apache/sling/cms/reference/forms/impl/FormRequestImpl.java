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
package org.apache.sling.cms.reference.forms.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.cms.ResourceTree;
import org.apache.sling.cms.reference.forms.FieldHandler;
import org.apache.sling.cms.reference.forms.FormException;
import org.apache.sling.cms.reference.forms.FormRequest;
import org.apache.sling.cms.reference.forms.FormValueProvider;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of a Form Request
 */
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class }, adapters = FormRequest.class)
public class FormRequestImpl implements FormRequest {

    private static final Logger log = LoggerFactory.getLogger(FormRequestImpl.class);

    private final List<FieldHandler> fieldHandlers;

    private final Map<String, Object> formData = new HashMap<>();

    private final SlingHttpServletRequest request;

    @Inject
    @SuppressWarnings("unchecked")
    public FormRequestImpl(@Self SlingHttpServletRequest request,
            @OSGiService(injectionStrategy = InjectionStrategy.OPTIONAL) List<FormValueProvider> formValueProvider,
            @OSGiService(injectionStrategy = InjectionStrategy.OPTIONAL) List<FieldHandler> fieldHandlers) {
        this.request = request;
        this.fieldHandlers = fieldHandlers;
        if (request.getSession().getAttribute(this.getSessionId()) != null) {
            formData.putAll(((Map<String, Object>) request.getSession().getAttribute(this.getSessionId())));
        }
        if (getFormResource() != null && getFormResource().getChild("providers") != null) {
            loadProviders(formValueProvider);
        }
    }

    private void loadProviders(List<FormValueProvider> formValueProvider) {
        List<Resource> providers = ResourceTree.stream(getFormResource().getChild("providers"))
                .map(ResourceTree::getResource).collect(Collectors.toList());
        for (Resource provider : providers) {
            log.debug("Looking for handler for: {}", provider);
            if (formValueProvider != null) {
                for (FormValueProvider fvp : formValueProvider) {
                    if (fvp.handles(provider)) {
                        log.debug("Invoking field value provider: {}", fvp.getClass());
                        fvp.loadValues(request, provider, formData);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public ValueMap getFormData() {
        return new ValueMapDecorator(formData);
    }

    @Override
    public Resource getFormResource() {
        return request.getResource();
    }

    @Override
    public SlingHttpServletRequest getOriginalRequest() {
        return request;
    }

    public void initFields() throws FormException {
        List<Resource> fields = ResourceTree.stream(getFormResource().getChild("fields")).map(ResourceTree::getResource)
                .collect(Collectors.toList());
        for (Resource field : fields) {
            log.debug("Looking for handler for: {}", field);
            for (FieldHandler fieldHandler : fieldHandlers) {
                if (fieldHandler.handles(field)) {
                    log.debug("Invoking field handler: {}", fieldHandler.getClass());
                    fieldHandler.handleField(request, field, formData);
                    break;
                }
            }
        }
    }

    @Override
    public String getSessionId() {
        return "errorval-" + Optional.ofNullable(getFormResource()).map(Resource::getPath).orElse("null");
    }

}
