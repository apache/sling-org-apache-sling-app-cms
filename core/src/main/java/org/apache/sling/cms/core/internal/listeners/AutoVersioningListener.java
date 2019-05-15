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
package org.apache.sling.cms.core.internal.listeners;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.version.Version;

import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.observation.ExternalResourceChangeListener;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.CMSUtils;
import org.apache.sling.cms.Page;
import org.apache.sling.cms.core.models.VersionInfo;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Resource Change Listener will automatically version pages when a page is
 * published / unpublished or an interval passes since the page was last saved.
 */
@Component(service = { AutoVersioningListener.class, ResourceChangeListener.class,
        ExternalResourceChangeListener.class }, property = { ResourceChangeListener.CHANGES + "=ADDED",
                ResourceChangeListener.CHANGES + "=CHANGED", ResourceChangeListener.CHANGES + "=REMOVED",
                ResourceChangeListener.PATHS + "=/content" }, immediate = true)
@Designate(ocd = AutoVersioningListenerConfig.class)
public class AutoVersioningListener implements ResourceChangeListener, ExternalResourceChangeListener {

    private static final Logger log = LoggerFactory.getLogger(AutoVersioningListener.class);

    private int cutoff;
    private boolean enabled;

    @Activate
    @Modified
    public void activate(AutoVersioningListenerConfig config) {
        this.cutoff = config.cutoff();
        this.enabled = config.enabled();
    }

    @Reference
    private ResourceResolverFactory factory;

    @Override
    public void onChange(List<ResourceChange> changes) {
        if (enabled) {
            log.trace("onChange");

            try (ResourceResolver serviceResolver = factory.getServiceResourceResolver(
                    Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, "sling-cms-versionmgr"))) {

                Set<String> pages = new HashSet<>();
                for (ResourceChange rc : changes) {
                    Resource changed = serviceResolver.getResource(rc.getPath());
                    Resource page = CMSUtils.findPublishableParent(changed);
                    if (changed != null && page != null && CMSConstants.NT_PAGE.equals(page.getResourceType())
                            && !pages.contains(page.getPath())) {
                        log.debug("Evaluating the changes to {}", page.getPath());
                        String user = changed.getValueMap().get(CMSConstants.PN_LAST_MODIFIED_BY, String.class);
                        if (pastLastModified(page)) {
                            log.debug("Page {} needs to be versioned", page.getPath());
                            versionPage(page, user);
                        } else {
                            log.trace("Page {} does not need to be versioned", page.getPath());
                        }
                        pages.add(page.getPath());
                    } else {
                        log.trace("Not versioning {}", page);
                    }
                }
            } catch (LoginException e) {
                log.error("Exception getting service user", e);
            }
        }
    }

    private boolean pastLastModified(Resource pageResource) {
        try {
            Page page = pageResource.adaptTo(Page.class);
            if (page != null && page.isPublished()) {
                Version latestVersion = Optional.ofNullable(pageResource.adaptTo(VersionInfo.class))
                        .map(VersionInfo::getVersions).map(vs -> !vs.isEmpty() ? vs.get(vs.size() - 1) : null)
                        .orElse(null);
                if (latestVersion != null) {
                    if (latestVersion.hasProperty("jcr:frozenNode/jcr:content/jcr:lastModified")) {
                        Calendar lMod = latestVersion.getProperty("jcr:frozenNode/jcr:content/jcr:lastModified")
                                .getDate();
                        Calendar co = Calendar.getInstance();
                        co.add(Calendar.SECOND, cutoff * -1);
                        if (lMod.before(co)) {
                            log.trace("Page should be versioned");
                            return true;
                        } else {
                            log.trace("Page should not be versioned");
                        }
                    } else {
                        log.trace("No last modified found for version");
                    }
                } else {
                    return true;
                }
            } else {
                log.trace("Page is not published");
            }
        } catch (RepositoryException e) {
            log.error("Failed to check if modified date outside cutoff", e);
        }
        return false;
    }

    private void versionPage(Resource page, String user) {
        log.debug("Versioning page {}", page);
        ModifiableValueMap mvm = null;
        Resource content = page.getChild(JcrConstants.JCR_CONTENT);
        if (content != null) {
            mvm = content.adaptTo(ModifiableValueMap.class);
        }
        Node node = page.adaptTo(Node.class);

        if (mvm != null && node != null) {
            try {
                mvm.put(CMSConstants.PN_LAST_MODIFIED_BY, user);
                mvm.put(JcrConstants.JCR_LASTMODIFIED, Calendar.getInstance());
                node.addMixin(JcrConstants.MIX_VERSIONABLE);
                page.getResourceResolver().commit();
                node.getSession().getWorkspace().getVersionManager().checkpoint(node.getPath());
            } catch (PersistenceException e) {
                log.warn("Failed to save modification date on page: " + page, e);
            } catch (RepositoryException e) {
                log.warn("Failed to version page: " + page, e);
            }
        }
    }

}
