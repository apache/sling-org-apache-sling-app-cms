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
package org.apache.sling.cms.core.usergenerated.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.usergenerated.UGCBucketConfig;
import org.apache.sling.cms.usergenerated.UserGeneratedContentService;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = UserGeneratedContentService.class)
@Designate(ocd = UserGeneratedContentConfig.class)
public class UserGeneratedContentServiceImpl implements UserGeneratedContentService {

    private static final Logger log = LoggerFactory.getLogger(UserGeneratedContentServiceImpl.class);

    @Reference
    private ResourceResolverFactory factory;
    private UserGeneratedContentConfig config;

    private ResourceResolver serviceResolver;

    @Activate
    public void activate(UserGeneratedContentConfig config) throws LoginException {
        this.config = config;

        log.debug("Connecting with service user");
        Map<String, Object> serviceParams = new HashMap<>();
        serviceParams.put(ResourceResolverFactory.SUBSERVICE, "sling-cms-ugc");
        serviceResolver = factory.getServiceResourceResolver(serviceParams);
    }

    @Deactivate
    public void deactivate() {
        if (serviceResolver != null) {
            serviceResolver.close();
        }
    }

    @Override
    public Resource createUGCContainer(SlingHttpServletRequest request, UGCBucketConfig bucketConfig, String preview,
            String targetPath) throws PersistenceException {

        serviceResolver.refresh();

        Resource resource = null;

        log.debug("Creating content of type {} in bucket {}", bucketConfig.getContentType(), bucketConfig.getBucket());
        Map<String, Object> resourceProperties = new HashMap<>();
        resourceProperties.put(JcrConstants.JCR_PRIMARYTYPE, CMSConstants.NT_UGC);
        resourceProperties.put("approveaction", bucketConfig.getAction().toString());
        resourceProperties.put("contenttype", bucketConfig.getContentType().toString());
        resourceProperties.put("preview", preview);
        resourceProperties.put("published", false);
        resourceProperties.put("referrer", request.getHeader("referer"));
        if (StringUtils.isNotBlank(targetPath)) {
            resourceProperties.put("targetpath", targetPath);
        }
        resourceProperties.put("user", request.getResourceResolver().getUserID());
        resourceProperties.put("useragent", request.getHeader("User-Agent"));
        resourceProperties.put("userip", request.getRemoteAddr());

        String contentPath = generatePath(bucketConfig);
        log.debug("Creating article contents {}", contentPath);
        resource = ResourceUtil.getOrCreateResource(serviceResolver, contentPath, resourceProperties,
                JcrResourceConstants.NT_SLING_ORDERED_FOLDER, true);

        return resource;
    }

    private String generatePath(UGCBucketConfig bucketConfig) {
        String uuid = UUID.randomUUID().toString();
        int depth = bucketConfig.getPathDepth();
        if (depth == -1) {
            depth = config.defaultPathDepth();
        }
        String[] pathSegments = new String[depth];
        for (int i = 0; i < pathSegments.length; i++) {
            pathSegments[i] = String.valueOf(uuid.charAt(i));
        }
        if (pathSegments.length > 0) {
            return config.ugcRoot() + "/" + bucketConfig.getBucket() + "/" + StringUtils.join(pathSegments, "/") + "/"
                    + uuid;
        } else {
            return config.ugcRoot() + "/" + bucketConfig.getBucket() + "/" + uuid;
        }
    }

}
