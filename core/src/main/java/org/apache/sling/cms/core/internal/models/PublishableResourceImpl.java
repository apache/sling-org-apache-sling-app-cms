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

import javax.inject.Inject;

import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.oak.spi.nodetype.NodeTypeConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.PublishableResource;
import org.apache.sling.cms.Site;
import org.apache.sling.cms.SiteManager;
import org.apache.sling.cms.publication.PublicationType;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the publishable resource interface and adaptable from a
 * resource.
 */
@Model(adaptables = Resource.class, adapters = PublishableResource.class)
public class PublishableResourceImpl implements PublishableResource {

    private static final Logger log = LoggerFactory.getLogger(PublishableResourceImpl.class);

    public static final String LEGACY_PUBLISHED_PROPERTY = "published";

    private final Resource contentResource;

    private final Calendar created;

    private final String createdBy;

    private final Calendar lastModified;

    private final String lastModifiedBy;

    private final Calendar lastPublication;

    private final String lastPublicationType;

    private final boolean published;

    protected final Resource resource;

    private String lastPublicationBy;

    @Inject
    public PublishableResourceImpl(@Self Resource resource) {
        this.resource = resource;
        this.created = resource.getValueMap().get(JcrConstants.JCR_CREATED, Calendar.class);
        this.createdBy = resource.getValueMap().get(NodeTypeConstants.JCR_CREATEDBY, String.class);
        this.contentResource = resource.getChild(JcrConstants.JCR_CONTENT);
        if (this.contentResource != null) {
            ValueMap properties = contentResource.getValueMap();
            this.lastModified = properties.get(JcrConstants.JCR_LASTMODIFIED, Calendar.class);
            this.lastModifiedBy = properties.get(NodeTypeConstants.JCR_LASTMODIFIEDBY, String.class);
            this.lastPublication = properties.get(CMSConstants.PN_LAST_PUBLICATION, Calendar.class);
            this.lastPublicationBy = properties.get(NodeTypeConstants.JCR_LASTMODIFIEDBY, String.class);
            this.lastPublicationType = properties.get(CMSConstants.PN_LAST_PUBLICATION_TYPE, String.class);
            this.published = properties.get(CMSConstants.PN_PUBLISHED,
                    properties.get(LEGACY_PUBLISHED_PROPERTY, false));
        } else {
            this.lastModified = null;
            this.lastModifiedBy = null;
            this.lastPublication = null;
            this.lastPublicationBy = null;
            this.lastPublicationType = null;
            this.published = false;
        }
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
    public Calendar getLastModified() {
        return lastModified != null ? lastModified : created;
    }

    @Override
    public String getLastModifiedBy() {
        return lastModifiedBy != null ? lastModifiedBy : createdBy;
    }

    @Override
    public PublicationType getLastPublicationType() {
        if (lastPublicationType != null) {
            try {
                return PublicationType.valueOf(lastPublicationType);
            } catch (IllegalArgumentException iae) {
                log.warn("Invalid publication type: {}", lastPublicationType, iae);
            }
        }
        return null;
    }

    @Override
    public Calendar getLastPublication() {
        return lastPublication;
    }

    @Override
    public String getLastPublicationBy() {
        return lastPublicationBy;
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