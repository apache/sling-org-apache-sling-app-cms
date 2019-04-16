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
package org.apache.sling.cms.core.models;

import java.util.Collections;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletResponse;

import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.oak.spi.security.user.UserConstants;
import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestDispatcherOptions;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.wrappers.SlingHttpServletRequestWrapper;
import org.apache.sling.cms.CMSUtils;
import org.apache.sling.cms.Site;
import org.apache.sling.cms.SiteManager;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.osgi.annotation.versioning.ProviderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sling Model for retrieving an error handler based on the specified Sling
 * Request. Checks if the specified resource is contained within a Sling site
 * and if so, will display the error page found at
 * [site-root]/errors/[error-code] or [site-root]/errors/default
 */
@ProviderType
@Model(adaptables = SlingHttpServletRequest.class)
public class ErrorHandler {

    private static class GetRequest extends SlingHttpServletRequestWrapper {

        public GetRequest(SlingHttpServletRequest wrappedRequest) {
            super(wrappedRequest);
        }

        @Override
        public String getMethod() {
            return "GET";
        }

        @Override
        public RequestPathInfo getRequestPathInfo() {
            return new HTMLRequestPathInfo(super.getRequestPathInfo());
        }
    }

    private static class HTMLRequestPathInfo implements RequestPathInfo {

        private RequestPathInfo info;

        public HTMLRequestPathInfo(RequestPathInfo info) {
            this.info = info;
        }

        @Override
        public String getResourcePath() {
            return info.getResourcePath();
        }

        @Override
        public String getExtension() {
            return "html";
        }

        @Override
        public String getSelectorString() {
            return "";
        }

        @Override
        public String[] getSelectors() {
            return new String[0];
        }

        @Override
        public String getSuffix() {
            return "";
        }

        @Override
        public Resource getSuffixResource() {
            return null;
        }

    }

    /**
     * The page to fall back to if there is not an error page for the specific code
     */
    public static final String DEFAULT_ERROR_PAGE = "default";

    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    /**
     * Service User Name for the Error Handler
     */
    public static final String SERVICE_USER_NAME = "sling-cms-error";

    /**
     * The subpath under which to find the error pages
     */
    public static final String SITE_ERRORS_SUBPATH = "errors/";

    /**
     * Path under which the error pages for Sling CMS can be found.
     */
    public static final String SLING_CMS_ERROR_PATH = "/content/sling-cms/errorhandling/";

    @RequestAttribute
    @Named(SlingConstants.ERROR_STATUS)
    @Optional
    @Default(intValues = HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
    private Integer errorCode;

    @OSGiService
    private ResourceResolverFactory factory;

    private Resource handler;

    @SlingObject
    private SlingHttpServletResponse slingResponse;

    private SlingHttpServletRequest slingRequest;

    public ErrorHandler(SlingHttpServletRequest slingRequest) {
        this.slingRequest = slingRequest;
    }

    private void calculateErrorCode(ResourceResolver resolver) {
        if (errorCode == HttpServletResponse.SC_NOT_FOUND) {
            log.debug("Validating the resource does not exist for all users");
            ResourceResolver adminResolver = null;
            try {

                adminResolver = factory.getServiceResourceResolver(
                        Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, SERVICE_USER_NAME));
                Resource pResource = adminResolver.resolve(slingRequest, slingRequest.getResource().getPath());
                if (!CMSUtils.isPublished(pResource) || pResource.isResourceType(Resource.RESOURCE_TYPE_NON_EXISTING)) {
                    errorCode = HttpServletResponse.SC_NOT_FOUND;
                } else if (UserConstants.DEFAULT_ANONYMOUS_ID.equals(resolver.getUserID())) {
                    errorCode = HttpServletResponse.SC_UNAUTHORIZED;
                } else {
                    errorCode = HttpServletResponse.SC_FORBIDDEN;
                }
            } catch (LoginException e) {
                log.error("Exception retrieving service user", e);
            } finally {
                if (adminResolver != null) {
                    adminResolver.close();
                }
            }
        }
    }

    private void doInclude() {

        Resource handlerContent = handler.getChild(JcrConstants.JCR_CONTENT);
        if (handlerContent != null) {
            log.debug("Including handler {}", handlerContent);

            RequestDispatcherOptions rdo = new RequestDispatcherOptions();
            rdo.setReplaceSelectors("");
            rdo.setReplaceSuffix("");
            rdo.setForceResourceType(handlerContent.getResourceType());
            final RequestDispatcher dispatcher = slingRequest.getRequestDispatcher(handlerContent, rdo);
            if (dispatcher != null) {
                try {
                    dispatcher.include(new GetRequest(slingRequest), slingResponse);
                } catch (Exception e) {
                    log.debug("Exception swallowed while including error page", e);
                }
            } else {
                log.warn("Failed to get request dispatcher for handler {}", handler.getPath());
            }
        } else {
            log.warn("Error hander {} content is null", handler);
        }
    }

    @PostConstruct
    public void init() {

        Resource resource = slingRequest.getResource();
        ResourceResolver resolver = slingRequest.getResourceResolver();

        log.debug("Calculating error handling scripts for resource {} and error code {}", resource, errorCode);

        if (slingRequest.getAttribute(SlingConstants.ERROR_EXCEPTION) != null) {
            log.warn("Handing exception of type {} {}", errorCode,
                    slingRequest.getAttribute(SlingConstants.ERROR_EXCEPTION));
        }

        calculateErrorCode(resolver);

        try {
            SiteManager siteMgr = resource.adaptTo(SiteManager.class);
            if (siteMgr != null && siteMgr.getSite() != null) {
                Site site = siteMgr.getSite();
                log.debug("Checking for error pages in the site {}", site.getPath());

                handler = site.getResource().getChild(SITE_ERRORS_SUBPATH + errorCode.toString());
                if (handler == null) {
                    handler = site.getResource().getChild(SITE_ERRORS_SUBPATH + DEFAULT_ERROR_PAGE);
                }
                if (handler != null) {
                    log.debug("Using error handler {}", handler);
                } else {
                    log.debug("No error page defined for site {}", site.getPath());
                }
            }
        } catch (Exception e) {
            log.debug("Failed to retrieve current site, using default error handling");
        }

        if (handler == null) {
            log.debug("Using Sling CMS default error pages");
            handler = resolver.getResource(SLING_CMS_ERROR_PATH + errorCode.toString());
            if (handler == null) {
                handler = resolver.getResource(SLING_CMS_ERROR_PATH + DEFAULT_ERROR_PAGE);
            }
            log.debug("Using Sling CMS error handler {}", handler);
        }

        log.debug("Sending error {}", errorCode);
        slingResponse.reset();
        slingResponse.setContentType("text/html");
        slingResponse.setStatus(errorCode);

        doInclude();

        log.debug("Error handler initialized successfully!");
    }

}
