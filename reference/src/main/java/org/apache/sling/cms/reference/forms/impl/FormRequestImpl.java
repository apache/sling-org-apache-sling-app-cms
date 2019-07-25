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
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = { SlingHttpServletRequest.class, Resource.class }, adapters = FormRequest.class)
public class FormRequestImpl implements FormRequest {

    private static final Logger log = LoggerFactory.getLogger(FormRequestImpl.class);

    @OSGiService(injectionStrategy = InjectionStrategy.OPTIONAL)
    private List<FieldHandler> fieldHandlers;

    private Map<String, Object> formData = new HashMap<>();

    @OSGiService(injectionStrategy = InjectionStrategy.OPTIONAL)
    private List<FormValueProvider> formValueProvider;

    private final boolean loadProviders;

    private SlingHttpServletRequest request;

    public FormRequestImpl(SlingHttpServletRequest request) throws FormException {
        this.request = request;
        this.loadProviders = true;
    }

    public FormRequestImpl(SlingHttpServletRequest request, boolean loadProviders) throws FormException {
        this.request = request;
        this.loadProviders = loadProviders;
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

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init() throws FormException {
        if (request.getSession().getAttribute(this.getSessionId()) != null) {
            formData.putAll(((Map<String, Object>) request.getSession().getAttribute(this.getSessionId())));
        }
        if (this.loadProviders && getFormResource().getChild("providers") != null) {
            List<Resource> providers = ResourceTree.stream(getFormResource().getChild("providers"))
                    .map(ResourceTree::getResource).collect(Collectors.toList());
            for (Resource provider : providers) {
                log.debug("Looking for handler for: {}", provider);
                if (formValueProvider != null) {
                    for (FormValueProvider fvp : formValueProvider) {
                        if (fvp.handles(provider)) {
                            log.debug("Invoking field value provider: {}", fvp.getClass());
                            fvp.loadValues(provider, formData);
                            break;
                        }
                    }
                }
            }
        }
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
        return "errorval-" + this.getOriginalRequest().getResource().getPath();
    }

}
