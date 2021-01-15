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
package org.apache.sling.cms.reference.forms.impl.actions;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.cms.reference.forms.FormAction;
import org.apache.sling.cms.reference.forms.FormActionResult;
import org.apache.sling.cms.reference.forms.FormConstants;
import org.apache.sling.cms.reference.forms.FormException;
import org.apache.sling.cms.reference.forms.FormRequest;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = FormAction.class, immediate = true)
public class UpdateResourceAction implements FormAction {

    private static final Logger log = LoggerFactory.getLogger(UpdateResourceAction.class);

    private ResourceResolverFactory factory;

    @Activate
    public UpdateResourceAction(@Reference ResourceResolverFactory factory) {
        this.factory = factory;
    }

    @Override
    public FormActionResult handleForm(Resource actionResource, FormRequest request) throws FormException {
        log.trace("handleForm");

        ValueMap properties = actionResource.getValueMap();

        String[] allowedProperties = properties.get(FormConstants.PN_ALLOWED_PROPERTIES, String[].class);
        ResourceResolver resolver = null;
        try {
            resolver = getResourceResolver(actionResource);
            StringSubstitutor sub = new StringSubstitutor(request.getFormData());

            String path = sub.replace(properties.get("path", String.class));
            log.debug("Upserting resource at path: {}", path);
            Resource resource = ResourceUtil.getOrCreateResource(resolver, path,
                    Collections.singletonMap(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED),
                    JcrConstants.NT_UNSTRUCTURED, false);

            ModifiableValueMap mvm = resource.adaptTo(ModifiableValueMap.class);
            Map<String, Object> formData = request.getFormData();
            if (allowedProperties != null && allowedProperties.length > 0) {
                Arrays.stream(allowedProperties).filter(formData::containsKey).forEach(p -> {
                    log.debug("Setting property {}", p);
                    mvm.put(p, formData.get(p));
                });
            } else {
                mvm.putAll(formData);
            }
            resolver.commit();
            log.debug("Successfully persisted resource");
            return FormActionResult.success("Updated resource");
        } catch (LoginException | PersistenceException e) {
            throw new FormException("Failed to update resource", e);
        } finally {
            if (StringUtils.isNotBlank(properties.get(FormConstants.PN_SERVICE_USER, String.class)) && resolver != null) {
                resolver.close();
            }
        }
    }

    private ResourceResolver getResourceResolver(Resource actionResource) throws LoginException {
        String serviceUser = actionResource.getValueMap().get(FormConstants.PN_SERVICE_USER, String.class);
        if (StringUtils.isNotBlank(serviceUser)) {
            return factory.getServiceResourceResolver(
                    Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, serviceUser));
        } else {
            return actionResource.getResourceResolver();
        }

    }

    @Override
    public boolean handles(Resource actionResource) {
        return "reference/components/forms/actions/updateresource".equals(actionResource.getResourceType());
    }

}
