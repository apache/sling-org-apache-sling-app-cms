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
package org.apache.sling.cms.core.internal.filters;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.cms.PublishableResource;
import org.apache.sling.cms.publication.PUBLICATION_MODE;
import org.apache.sling.cms.publication.PublicationManagerFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Checks to ensure that the user is logged in for requests which otherwise
 * would be allowed when accessing through a CMS-specific domain name.
 */
@Component(service = { Filter.class }, property = { "sling.filter.scope=request" }, immediate = true)
public class CMSSecurityFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(CMSSecurityFilter.class);

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policyOption = ReferencePolicyOption.GREEDY)
    private List<CMSSecurityConfigInstance> securityConfigInstances;

    @Reference
    private PublicationManagerFactory pubMgrFactory;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Nothing required
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (pubMgrFactory.getPublicationMode() == PUBLICATION_MODE.STANDALONE) {
            SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
            for (CMSSecurityConfigInstance securityConfig : securityConfigInstances) {
                log.trace("Checking to see if security config {} applies to request", securityConfig);
                if (securityConfig.applies(slingRequest)) {
                    boolean allowed = checkAllowed(securityConfig, slingRequest);
                    // permission checked failed, so return an unauthorized error
                    if (!allowed) {
                        log.trace("Request to {} not allowed for user {}", slingRequest.getRequestURI(),
                                slingRequest.getResourceResolver().getUserID());
                        ((HttpServletResponse) response).sendError(401);
                        return;
                    }
                }
            }
        } else {
            log.trace("Publication mode {} is not standalone", pubMgrFactory.getPublicationMode());
        }

        chain.doFilter(request, response);
    }

    private boolean checkAllowed(CMSSecurityConfigInstance securityConfig, SlingHttpServletRequest slingRequest) {
        log.trace("Filtering requests to host {}", slingRequest.getServerName());
        String uri = slingRequest.getRequestURI();
        boolean allowed = false;
        if (securityConfig.isUriAllowed(uri)) {
            log.trace("Allowing request to uri {} based on allow patterns", uri);
            allowed = true;
        }

        PublishableResource publishableResource = slingRequest.getResource().adaptTo(PublishableResource.class);
        if (publishableResource.isPublished()) {
            log.trace("Resource is published");
            allowed = true;
        }

        // the uri isn't allowed automatically, so check user permissions
        if (!allowed) {
            log.trace("Request to {} not public, checking user permissions", uri);
            // check to see if the user is a member of the specified group
            if (StringUtils.isNotBlank(securityConfig.getGroupName())) {
                allowed = checkGroupMembership(securityConfig, slingRequest);
            } else {
                // just check to make sure the user is logged in
                if (!"anonymous".equals(slingRequest.getResourceResolver().getUserID())) {
                    allowed = true;
                }
            }
        } else {
            log.trace("Request to {} allowed", uri);
        }
        return allowed;
    }

    private boolean checkGroupMembership(CMSSecurityConfigInstance securityConfig,
            SlingHttpServletRequest slingRequest) {
        boolean allowed = false;
        try {
            Session session = slingRequest.getResourceResolver().adaptTo(Session.class);
            UserManager userManager = null;
            if (session instanceof JackrabbitSession) {
                userManager = ((JackrabbitSession) session).getUserManager();
            }
            if (userManager == null) {
                log.warn("Unable to retrieve user manager");
                return false;
            }
            log.trace("Retrieved user manager {} with session {}", userManager, session);
            Authorizable auth;

            auth = userManager.getAuthorizable(slingRequest.getUserPrincipal());
            if (auth == null) {
                log.warn("Unable to retrieve user from principal {}", slingRequest.getUserPrincipal());
                return false;
            }

            log.trace("Checking to see if user {} is in required group {}", auth.getID(),
                    securityConfig.getGroupName());
            Iterator<Group> groups = ((User) auth).memberOf();
            while (groups.hasNext()) {
                if (groups.next().getID().equals(securityConfig.getGroupName())) {
                    allowed = true;
                    break;
                }
            }

        } catch (RepositoryException e) {
            log.error("Unexpected exception checking group membership", e);
            return false;
        }
        return allowed;
    }

    @Override
    public void destroy() {
        // Nothing required
    }

}
