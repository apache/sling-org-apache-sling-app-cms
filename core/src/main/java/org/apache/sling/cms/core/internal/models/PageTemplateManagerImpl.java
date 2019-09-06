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
package org.apache.sling.cms.core.internal.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.resource.ConfigurationResourceResolver;
import org.apache.sling.cms.CMSUtils;
import org.apache.sling.cms.PageTemplate;
import org.apache.sling.cms.PageTemplateManager;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A model for retrieving the available templates to create a page under the
 * specified resource.
 */
@Model(adaptables = Resource.class, adapters = PageTemplateManager.class)
public class PageTemplateManagerImpl implements PageTemplateManager {

    private static final Logger log = LoggerFactory.getLogger(PageTemplateManagerImpl.class);

    private Resource resource;

    private List<PageTemplate> siteTemplates;

    @OSGiService
    private ConfigurationResourceResolver configurationResourceResolver;

    public PageTemplateManagerImpl(Resource resource) {
        this.resource = resource;
    }

    @PostConstruct
    public void init() {
        siteTemplates = CMSUtils.adaptResources(
                configurationResourceResolver.getResourceCollection(resource, "site", "templates"), PageTemplate.class);
    }

    @Override
    public List<PageTemplate> getAvailableTemplates() {
        String path = resource.getPath();
        List<PageTemplate> availableTemplates = new ArrayList<>();

        for (PageTemplate template : siteTemplates) {
            log.debug("Checking to see if template {} is available for path {}", template.getResource().getPath(),
                    path);
            for (String allowedPath : template.getAllowedPaths()) {
                log.trace("Checking to see if path {} matches regex {}", path, allowedPath);
                if (path.matches(allowedPath)) {
                    availableTemplates.add(template);
                    log.debug("Template {} is available for path {}", template.getResource().getPath(), path);
                    break;
                }
            }
        }
        return availableTemplates;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PageTemplateManager [siteTemplates=" + siteTemplates + ", resource=" + resource + "]";
    }
}
