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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.CMSUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Checks to ensure that the user is logged in for requests which otherwise
 * would be allowed when accessing through a CMS-specific domain name.
 */
@Component(service = { Filter.class }, property = {
        "sling.filter.scope=request" }, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = CMSSecurityFilterConfig.class)
public class CMSSecurityFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(CMSSecurityFilter.class);

    private CMSSecurityFilterConfig config;

    private List<Pattern> patterns = new ArrayList<>();

    private static final String[] VALID_METHODS = new String[] { "GET", "HEAD" };

    @Modified
    @Activate
    public void activate(CMSSecurityFilterConfig config) {
        if (config.hostDomains() != null && config.hostDomains().length > 0) {
            if (log.isInfoEnabled()) {
                log.info("Applying CMS Security Filter for domains {}", Arrays.toString(config.hostDomains()));
            }
            this.config = config;
            for (String p : config.allowedPatterns()) {
                patterns.add(Pattern.compile(p));
            }
        } else {
            this.config = null;
            log.info("No host domains supplied, CMS Security Filter not enabled");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Nothing required
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
        if (config != null && ArrayUtils.contains(config.hostDomains(), request.getServerName())) {
            log.trace("Filtering requests to host {}", request.getServerName());
            String uri = slingRequest.getRequestURI();
            boolean allowed = false;
            for (Pattern p : this.patterns) {
                if (p.matcher(uri).matches()) {
                    log.trace("Allowing request matching pattern {}", p);
                    allowed = true;
                    break;
                }
            }

            // the uri isn't allowed automatically, so check user permissions
            if (!allowed) {

                // check to see if the user is a member of the specified group
                if (StringUtils.isNotBlank(config.group())) {
                    allowed = checkGroupMembership(slingRequest);

                    // just check to make sure the user is logged in
                } else {
                    if (!"anonymous".equals(slingRequest.getResourceResolver().getUserID())) {
                        allowed = true;
                    }
                }
            }

            // permission checked failed, so return an unauthorized error
            if (!allowed) {
                log.trace("Request to {} not allowed for user {}", slingRequest.getRequestURI(),
                        slingRequest.getResourceResolver().getUserID());
                ((HttpServletResponse) response).sendError(401);
                return;
            }
        } else if (ArrayUtils.contains(VALID_METHODS, slingRequest.getMethod())) {
            Object editEnabled = slingRequest.getAttribute(CMSConstants.ATTR_EDIT_ENABLED);
            if (!"true".equals(editEnabled) && !CMSUtils.isPublished(slingRequest.getResource())) {
                ((HttpServletResponse) response).sendError(404);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean checkGroupMembership(SlingHttpServletRequest slingRequest) {
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

            log.trace("Checking to see if user {} is in required group {}", auth.getID(), config.group());
            Iterator<Group> groups = ((User) auth).memberOf();
            while (groups.hasNext()) {
                if (groups.next().getID().equals(config.group())) {
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
