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
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.cms.AuthorizableWrapper;
import org.apache.sling.cms.Site;
import org.apache.sling.cms.SiteManager;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sets the locale for the request based on the containing site.
 */
@Component(service = { Filter.class }, immediate = true, property = { "sling.filter.scope=request" })
public class LocaleFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(LocaleFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Nothing required
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof SlingHttpServletRequest) {
            Locale locale = null;
            SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
            SiteManager mgr = slingRequest.getResource().adaptTo(SiteManager.class);
            if (mgr != null) {
                Site site = mgr.getSite();
                if (site != null) {
                    log.debug("Setting bundle for {}", site.getLocaleString());
                    locale = site.getLocale();
                } else {
                    log.trace("No site for {}", slingRequest);
                }
            } else {
                log.trace("No site manager found for {}", slingRequest);
            }
            if (locale == null) {
                locale = loadUserLocale(slingRequest);
            }
            if (locale == null) {
                locale = request.getLocale();
            }
            setLocale(locale, slingRequest);
        }

        chain.doFilter(request, response);
    }

    private Locale loadUserLocale(SlingHttpServletRequest slingRequest) {
        return Optional.ofNullable(slingRequest.getResourceResolver().adaptTo(AuthorizableWrapper.class))
                .map(AuthorizableWrapper::getLocale).orElse(null);
    }

    private void setLocale(Locale locale, SlingHttpServletRequest slingRequest) {
        log.debug("Setting locale to {}", locale);
        ResourceBundle bundle = slingRequest.getResourceBundle(locale);
        Config.set(slingRequest, "javax.servlet.jsp.jstl.fmt.localizationContext",
                new LocalizationContext(bundle, locale));
    }

    @Override
    public void destroy() {
        // Nothing required
    }

}
