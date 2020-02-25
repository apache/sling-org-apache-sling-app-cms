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
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.NameFilter;
import org.apache.sling.cms.reference.forms.FormAction;
import org.apache.sling.cms.reference.forms.FormActionResult;
import org.apache.sling.cms.reference.forms.FormException;
import org.apache.sling.cms.reference.forms.FormRequest;
import org.apache.sling.cms.usergenerated.UGCBucketConfig;
import org.apache.sling.cms.usergenerated.UserGeneratedContentService;
import org.apache.sling.cms.usergenerated.UserGeneratedContentService.APPROVE_ACTION;
import org.apache.sling.cms.usergenerated.UserGeneratedContentService.CONTENT_TYPE;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = FormAction.class, immediate = true)
public class UserGeneratedContentAction implements FormAction {

    private static final Logger log = LoggerFactory.getLogger(UserGeneratedContentAction.class);

    @Reference
    private NameFilter filter;

    @Reference
    private UserGeneratedContentService ugcService;

    @Override
    public FormActionResult handleForm(Resource actionResource, FormRequest request) throws FormException {
        log.trace("handleForm");
        ValueMap properties = actionResource.getValueMap();

        try {
            UGCBucketConfig bucketConfig = new UGCBucketConfig();
            bucketConfig.setAction(
                    APPROVE_ACTION.valueOf(properties.get("approveAction", APPROVE_ACTION.PUBLISH.toString())));
            bucketConfig.setBucket(properties.get("bucket", String.class));
            bucketConfig
                    .setContentType(CONTENT_TYPE.valueOf(properties.get("contentType", CONTENT_TYPE.OTHER.toString())));
            bucketConfig.setPathDepth(properties.get("pathDepth", 0));

            log.debug("Creating UGC at with configuration:  {}", bucketConfig);
            StringSubstitutor sub = new StringSubstitutor(request.getFormData());

            Map<String, Object> contentProperties = new HashMap<>();
            contentProperties.put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED);
            Arrays.stream(properties.get("additionalProperties", new String[0])).map(v -> {
                if (v.contains("=")) {
                    String[] vs = v.split("\\=");
                    return new ImmutablePair<String, String>(vs[0], vs[1]);
                } else {
                    log.warn("Invalid value: {}", v);
                    return null;
                }
            }).forEach(v -> {
                log.debug("Adding additional property: {}", v);
                contentProperties.put(v.getLeft(), v.getRight());
            });
            contentProperties.putAll(request.getFormData());
            log.debug("Persisting properties: {}", contentProperties);

            Resource container = ugcService.createUGCContainer(request.getOriginalRequest(), bucketConfig,
                    sub.replace(properties.get("preview", "")), properties.get("targetPath", ""));
            log.debug("Using container: {}", container);
            ResourceResolver resolver = container.getResourceResolver();

            String name = filter.filter(sub.replace(properties.get("name", "")));
            log.debug("Using name {}", name);

            if (properties.get("wrapPage", false)) {
                log.debug("Wrapping with page");
                Resource page = container.getResourceResolver().create(container, name,
                        Collections.singletonMap(JcrConstants.JCR_PRIMARYTYPE, CMSConstants.NT_PAGE));
                resolver.create(page, JcrConstants.JCR_CONTENT, contentProperties);
            } else {
                log.debug("Creating as direct child");
                resolver.create(container, name, contentProperties);
            }

            resolver.commit();
            log.debug("Successfully persisted UGC");
            return FormActionResult.success("Created UGC Item");
        } catch (PersistenceException e) {
            throw new FormException("Failed to create UGC Content", e);
        }
    }

    @Override
    public boolean handles(Resource actionResource) {
        return "reference/components/forms/actions/usergeneratedcontent".equals(actionResource.getResourceType());
    }

}
