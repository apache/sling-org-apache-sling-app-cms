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

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.Site;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

/**
 * A model for representing a site.
 */
@Model(adaptables = Resource.class, adapters = Site.class)
public class SiteImpl implements Site {

    public static final String PN_CONFIG = CMSConstants.NAMESPACE + ":configRef";
    public static final String PN_URL = CMSConstants.NAMESPACE + ":url";

    private static Resource findSiteResource(Resource resource) {
        if (CMSConstants.NT_SITE.equals(resource.getValueMap().get(JcrConstants.JCR_PRIMARYTYPE, String.class))) {
            return resource;
        } else if (resource.getParent() != null) {
            return findSiteResource(resource.getParent());
        }
        return null;
    }

    public static Site getSite(Resource resource) {
        Site site = null;
        Resource siteResource = SiteImpl.findSiteResource(resource);
        if (siteResource != null) {
            site = siteResource.adaptTo(Site.class);
        }
        return site;
    }

    @Inject
    @Named(CMSConstants.PN_DESCRIPTION)
    @Optional
    private String description;

    @Inject
    @Named(CMSConstants.PN_LANGUAGE)
    private String locale;

    private Resource resource;

    @Inject
    @Named(CMSConstants.PN_TITLE)
    private String title;

    @Inject
    @Named(PN_URL)
    private String url;

    public SiteImpl(Resource resource) {
        this.resource = resource;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Site other = (Site) obj;
        if (resource == null) {
            if (other.getResource() != null) {
                return false;
            }
        } else if (!resource.getPath().equals(other.getResource().getPath())) {
            return false;
        }
        return true;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Locale getLocale() {
        String[] segments = locale.split("_");
        if (segments.length == 3) {
            return new Locale(segments[0], segments[1], segments[2]);
        } else if (segments.length == 2) {
            return new Locale(segments[0], segments[1]);
        }
        return new Locale(segments[0]);
    }

    @Override
    public String getLocaleString() {
        return locale;
    }

    @Override
    public String getPath() {
        return resource.getPath();
    }

    @Override
    public Resource getResource() {
        return resource;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + resource.getPath().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Site [description=" + description + ", locale=" + locale + ", resource=" + resource + ", title=" + title
                + ", url=" + url + "]";
    }

}
