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
package org.apache.sling.cms.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.core.internal.ResourceEditorAssociation;
import org.apache.sling.cms.core.internal.ResourceEditorAssociationProvider;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.osgi.annotation.versioning.ProviderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Model for constructing the content breadcrumb.
 */
@ProviderType
@Model(adaptables = SlingHttpServletRequest.class)
public class ContentBreadcrumb {

    private static final Logger log = LoggerFactory.getLogger(ContentBreadcrumb.class);

    private long depth;

    private List<Pair<String, String>> parents;

    @OSGiService
    private ResourceEditorAssociationProvider provider;

    private Resource resource;

    private String rootTitle;

    public ContentBreadcrumb(SlingHttpServletRequest request) {
        this.resource = request.getRequestPathInfo().getSuffixResource();

        log.debug("Loading configuration from {}", request.getResource().getValueMap());
        depth = request.getResource().getValueMap().get("depth", 0L);
        rootTitle = request.getResource().getValueMap().get("rootTitle", String.class);

    }

    public String getCurrentItem() {
        if ((parents == null || parents.isEmpty()) && StringUtils.isNotBlank(rootTitle)) {
            return rootTitle;
        }
        return getTitle(resource);
    }

    private String getLink(Resource resource) {
        log.debug("Getting link for {} from {}", resource, provider.getAssociations());
        return provider.getAssociations().stream().filter(a -> a.matches(resource)).findFirst()
                .map(ResourceEditorAssociation::getEditor).orElse("/bin/browser.html") + resource.getPath();
    }

    public List<Pair<String, String>> getParents() {
        return parents;
    }

    private String getTitle(Resource resource) {
        String title = resource.getValueMap().get(CMSConstants.PN_TITLE, String.class);
        if (StringUtils.isNotBlank(title)) {
            return title;
        }
        title = resource.getValueMap().get(JcrConstants.JCR_CONTENT + "/" + CMSConstants.PN_TITLE, String.class);
        if (StringUtils.isNotBlank(title)) {
            return title;
        }
        return resource.getName();
    }

    @PostConstruct
    public void init() {
        List<Resource> ps = new ArrayList<>();

        Resource current = resource;
        while (true) {
            Resource parent = current.getParent();
            if (parent != null) {
                ps.add(parent);
                current = parent;
            } else {
                break;
            }
        }
        Collections.reverse(ps);

        if (depth <= ps.size()) {
            ps = ps.subList((int) depth, ps.size());
        } else {
            ps.clear();
        }

        parents = ps.stream().map(p -> new ImmutablePair<>(getLink(p), getTitle(p))).collect(Collectors.toList());
        if (!parents.isEmpty() && StringUtils.isNotBlank(rootTitle)) {
            parents.set(0, new ImmutablePair<>(parents.get(0).getLeft(), rootTitle));
        }
    }
}
