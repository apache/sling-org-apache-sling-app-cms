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

import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.util.Text;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.File;
import org.apache.sling.cms.Site;
import org.apache.sling.cms.SiteManager;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

/**
 * A model representing a file
 */
@Model(adaptables = Resource.class, adapters = File.class)
public class FileImpl implements File {

    @Inject
    @Optional
    @Named("jcr:content")
    private Resource contentResource;

    @Inject
    @Named("jcr:content/jcr:mimeType")
    private String contentType;

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
    @Named("jcr:primaryType")
    private String type;

    public FileImpl(Resource resource) {
        this.resource = resource;
    }

    @Override
    public Resource getContentResource() {
        return contentResource;
    }

    @Override
    public String getContentType() {
        return contentType;
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
    public Calendar getLastModified() {
        return lastModified != null ? lastModified : created;
    }

    @Override
    public String getLastModifiedBy() {
        return lastModifiedBy != null ? lastModifiedBy : createdBy;
    }

    @Override
    public ValueMap getMetadata() {
        Resource metadata = this.getContentResource().getChild(CMSConstants.NN_METADATA);
        Map<String, Object> data = new TreeMap<>();
        if (metadata != null) {
            metadata.getValueMap().entrySet()
                    .forEach(e -> data.put(Text.unescapeIllegalJcrChars(e.getKey()), e.getValue()));
        }
        data.remove(JcrConstants.JCR_PRIMARYTYPE);
        return new ValueMapDecorator(data);
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
            return resource.getPath().replace(site.getPath(), "");
        } else {
            return resource.getPath();
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
    public boolean isPublished() {
        return published;
    }
}
