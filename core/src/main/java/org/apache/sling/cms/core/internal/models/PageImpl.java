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
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.CMSUtils;
import org.apache.sling.cms.Page;
import org.apache.sling.cms.PageTemplate;
import org.apache.sling.cms.Site;
import org.apache.sling.cms.SiteManager;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

/**
 * A model representing a page.
 */
@Model(adaptables = Resource.class, adapters = Page.class)
public class PageImpl implements Page {

    public static final Page getContainingPage(Resource resource) {
        Resource pageRsrc = CMSUtils.findParentResourceofType(resource, CMSConstants.NT_PAGE);
        Page page = null;
        if (pageRsrc != null) {
            page = pageRsrc.adaptTo(Page.class);
        }
        return page;
    }

    @Inject
    @Optional
    @Named("jcr:content")
    private Resource contentResource;

    @Inject
    @Optional
    @Named("jcr:content/jcr:created")
    private Calendar created;

    @Inject
    @Optional
    @Named("jcr:content/jcr:createdBy")
    private String createdBy;

    @Inject
    @Optional
    @Named("jcr:content/jcr:lastModified")
    private Calendar lastModified;

    @Inject
    @Optional
    @Named("jcr:content/jcr:lastModifiedBy")
    private String lastModifiedBy;

    @Inject
    @Named("jcr:content/published")
    @Default(booleanValues = false)
    private boolean published;

    protected Resource resource;

    @Inject
    @Optional
    @Named(JcrConstants.JCR_CONTENT + "/" + CMSConstants.PN_TAXONOMY)
    private String[] taxonomy;

    @Inject
    @Optional
    @Named("jcr:content/sling:template")
    private String template;

    @Inject
    @Named("jcr:content/jcr:title")
    @Optional
    private String title;

    @Inject
    @Named("jcr:primaryType")
    private String type;

    public PageImpl(Resource resource) {
        this.resource = resource;
    }

    @Override
    public Resource getContentResource() {
        return contentResource;
    }

    @Override
    public Calendar getCreated() {
        return created;
    }

    @Override
    public String getCreatedBy() {
        return createdBy;
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
    public Calendar getLastModified() {
        return lastModified != null ? lastModified : created;
    }

    @Override
    public String getLastModifiedBy() {
        return lastModifiedBy != null ? lastModifiedBy : createdBy;
    }

    @Override
    public String getName() {
        return resource.getName();
    }

    @Override
    public Resource getParent() {
        return resource.getParent();
    }

    @Override
    public String getPath() {
        return resource.getPath();
    }

    @Override
    public ValueMap getProperties() {
        return getContentResource().getValueMap();
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
    public String getPublishedUrl() {
        Site site = getSite();
        if (site != null) {
            return site.getUrl() + getPublishedPath();
        } else {
            return resource.getPath();
        }
    }

    @Override
    public Resource getResource() {
        return resource;
    }

    @Override
    public Site getSite() {
        SiteManager siteMgr = resource.adaptTo(SiteManager.class);
        Site site = null;
        if (siteMgr != null) {
            site = siteMgr.getSite();
        }
        return site;
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

    @Override
    public boolean isPublished() {
        return published;
    }
}
