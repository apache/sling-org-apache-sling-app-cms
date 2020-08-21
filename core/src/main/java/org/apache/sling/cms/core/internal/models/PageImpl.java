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

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.CMSUtils;
import org.apache.sling.cms.Page;
import org.apache.sling.cms.PageTemplate;
import org.apache.sling.cms.Site;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

/**
 * A model representing a page.
 */
@Model(adaptables = Resource.class, adapters = Page.class)
public class PageImpl extends PublishableResourceImpl implements Page {

    public static final Page getContainingPage(Resource resource) {
        Resource pageRsrc = CMSUtils.findParentResourceofType(resource, CMSConstants.NT_PAGE);
        Page page = null;
        if (pageRsrc != null) {
            page = pageRsrc.adaptTo(Page.class);
        }
        return page;
    }

    private final String[] taxonomy;

    private final String template;

    private final String title;

    @Inject
    public PageImpl(@Self Resource resource) {
        super(resource);
        if (this.getContentResource() != null) {
            ValueMap properties = this.getContentResource().getValueMap();
            taxonomy = properties.get(CMSConstants.PN_TAXONOMY, String[].class);
            template = properties.get(CMSConstants.PN_TEMPLATE, String.class);
            title = properties.get(CMSConstants.PN_TITLE, String.class);
        } else {
            this.taxonomy = new String[0];
            this.template = null;
            this.title = null;
        }
    }

    @Override
    public String[] getKeywords() {
        List<String> keywords = new ArrayList<>();
        if (taxonomy != null) {
            for (String item : taxonomy) {
                Resource rsrc = this.resource.getResourceResolver().getResource(item);
                if (rsrc != null) {
                    keywords.add(rsrc.getValueMap().get(CMSConstants.PN_TITLE, String.class));
                }
            }
        }
        return keywords.toArray(new String[keywords.size()]);
    }

    @Override
    public String getPublishedPath() {
        Site site = getSite();
        if (site != null) {
            return resource.getPath().replace(site.getPath(), "") + ".html";
        } else {
            return resource.getPath() + ".html";
        }
    }

    @Override
    public PageTemplate getTemplate() {
        Resource templateResource = this.resource.getResourceResolver().getResource(template);
        if (templateResource != null) {
            return templateResource.adaptTo(PageTemplate.class);
        } else {
            return null;
        }
    }

    @Override
    public String getTemplatePath() {
        return this.template;
    }

    @Override
    public String getTitle() {
        if (StringUtils.isNotEmpty(title)) {
            return title;
        } else {
            return resource.getName();
        }
    }
}
