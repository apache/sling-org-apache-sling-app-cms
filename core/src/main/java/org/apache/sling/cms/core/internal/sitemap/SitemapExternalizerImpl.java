
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

import java.util.Optional;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.Site;
import org.apache.sling.cms.SiteManager;
import org.apache.sling.sitemap.spi.common.SitemapLinkExternalizer;
import org.osgi.service.component.annotations.Component;

/**
 * Externalizes the URLs using the Sling CMS site url.
 */
@Component
public class SitemapExternalizerImpl implements SitemapLinkExternalizer {

    @Override
    public String externalize(SlingHttpServletRequest context, String uri) {
        return context.getResourceResolver().map(context, uri);
    }

    @Override
    public String externalize(Resource resource) {
        return Optional.ofNullable(resource.adaptTo(SiteManager.class)).map(SiteManager::getSite).map(Site::getUrl)
                .map(url -> url + resource.getResourceResolver().map(resource.getPath())).orElse(null);
    }
}