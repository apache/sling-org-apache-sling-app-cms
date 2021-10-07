/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.cms.core.internal.sitemap;

import java.util.Collections;
import java.util.Set;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.Page;
import org.apache.sling.cms.PageManager;
import org.apache.sling.cms.SiteManager;
import org.apache.sling.sitemap.SitemapException;
import org.apache.sling.sitemap.SitemapService;
import org.apache.sling.sitemap.builder.Sitemap;
import org.apache.sling.sitemap.spi.generator.ResourceTreeSitemapGenerator;
import org.apache.sling.sitemap.spi.generator.SitemapGenerator;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Component;

@Component(service = SitemapGenerator.class)
public class PageTreeSitemapGenerator extends ResourceTreeSitemapGenerator {

    @Override
    public Set<String> getNames(@NotNull Resource sitemapRoot) {
        return Collections.singleton(SitemapService.DEFAULT_SITEMAP_NAME);
    }

    @Override
    public Set<String> getOnDemandNames(Resource sitemapRoot) {
        return Collections.singleton(SitemapService.DEFAULT_SITEMAP_NAME);
    }

    @Override
    protected void addResource(@NotNull String name, @NotNull Sitemap sitemap, @NotNull Resource resource)
            throws SitemapException {
        String site = resource.adaptTo(SiteManager.class).getSite().getUrl();
        String path = resource.adaptTo(PageManager.class).getPage().getPublishedPath();
        sitemap.addUrl(site + path);
    }

    @Override
    protected boolean shouldInclude(@NotNull Resource resource) {
        if (super.shouldInclude(resource)) {
            Page page = resource.adaptTo(PageManager.class).getPage();
            return page.isPublished() && !page.getProperties().get("jcr:content/hideInSitemap", false);
        }
        return false;
    }

}