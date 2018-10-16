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
package org.apache.sling.cms.core.readability.impl;

import java.util.Collection;
import java.util.Locale;

import org.apache.sling.cms.readability.ReadabilityService;
import org.apache.sling.cms.readability.ReadabilityServiceFactory;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the ReadabilityServiceFactory service
 */
@Component(service = ReadabilityServiceFactory.class)
public class ReadabilityServiceFactoryImpl implements ReadabilityServiceFactory {

    private static final Logger log = LoggerFactory.getLogger(ReadabilityServiceFactoryImpl.class);

    private ComponentContext context;

    @Activate
    public void activate(ComponentContext context) {
        this.context = context;
    }

    public ReadabilityService getReadabilityService(Locale locale) {

        try {
            log.debug("Locating readability service for {}", locale);
            Collection<ServiceReference<ReadabilityService>> references = context.getBundleContext()
                    .getServiceReferences(ReadabilityService.class, "(locale=" + locale.toString() + ")");
            if (references == null || references.isEmpty()) {
                log.debug("Trying language fallback {}", locale.getLanguage());
                references = context.getBundleContext().getServiceReferences(ReadabilityService.class,
                        "(locale=" + locale.getLanguage() + ")");
            }
            if (references != null && !references.isEmpty()) {
                for (ServiceReference<ReadabilityService> ref : references) {
                    ReadabilityService service = context.getBundleContext().getService(ref);
                    if (service != null) {
                        log.debug("Found readability service for {}", locale);
                        return service;
                    }
                }
            } else {
                log.debug("No readibility services found for {}", locale);
            }
        } catch (InvalidSyntaxException e) {
            log.error("Exception creating expression to find service", e);
        }
        return null;
    }

}
