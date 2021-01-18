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

import java.util.Collections;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.reference.forms.FormAction;
import org.apache.sling.cms.reference.forms.FormActionResult;
import org.apache.sling.cms.reference.forms.FormException;
import org.apache.sling.cms.reference.forms.FormRequest;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = FormAction.class, immediate = true)
public class UpdateUserGeneratedContentAction implements FormAction {

    private static final Logger log = LoggerFactory.getLogger(UpdateUserGeneratedContentAction.class);

    private ResourceResolverFactory factory;

    @Activate
    public UpdateUserGeneratedContentAction(@Reference ResourceResolverFactory factory) {
        this.factory = factory;
    }

    @Override
    public FormActionResult handleForm(Resource actionResource, FormRequest request) throws FormException {
        log.trace("handleForm");

        ValueMap properties = actionResource.getValueMap();

        try (ResourceResolver resolver = getResourceResolver()) {
            StringSubstitutor sub = new StringSubstitutor(request.getFormData());

            String path = sub.replace(properties.get("path", String.class));
            log.debug("Updating UGC resource at path: {}", path);

            Resource resource = resolver.getResource(path);
            Resource ugcParent = findUgcParent(resource);

            if (!request.getOriginalRequest().getResourceResolver().getUserID()
                    .equals(ugcParent.getValueMap().get("user", String.class))) {
                throw new FormException("Cannot modify content not created by the current user");
            }

            ModifiableValueMap mvm = resource.adaptTo(ModifiableValueMap.class);
            Map<String, Object> formData = request.getFormData();
            mvm.putAll(formData);
            resolver.commit();
            log.debug("Successfully persisted resource");
            return FormActionResult.success("Updated resource");
        } catch (LoginException | PersistenceException e) {
            throw new FormException("Failed to update resource", e);
        }
    }

    private Resource findUgcParent(Resource resource) throws FormException {
        if (CMSConstants.NT_UGC.equals(resource.getResourceType())) {
            return resource;
        } else if (resource.getParent() != null) {
            return findUgcParent(resource.getParent());
        } else {
            throw new FormException("Failed to find UGC Parent");
        }
    }

    private ResourceResolver getResourceResolver() throws LoginException {
        return factory.getServiceResourceResolver(
                Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, "sling-cms-ugc"));
    }

    @Override
    public boolean handles(Resource actionResource) {
        return "reference/components/forms/actions/updateugc".equals(actionResource.getResourceType());
    }

}
