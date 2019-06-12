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

import java.util.List;
import java.util.Locale;

import org.apache.sling.cms.readability.ReadabilityService;
import org.apache.sling.cms.readability.ReadabilityServiceFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the ReadabilityServiceFactory service
 */
@Component(service = ReadabilityServiceFactory.class)
public class ReadabilityServiceFactoryImpl implements ReadabilityServiceFactory {

    private static final Logger log = LoggerFactory.getLogger(ReadabilityServiceFactoryImpl.class);

    @Reference(policyOption = ReferencePolicyOption.GREEDY)
    private List<ReadabilityService> services;

    @Override
    public ReadabilityService getReadabilityService(Locale locale) {
        log.debug("Locating readability service for {}", locale);
        return services.stream().filter(svc -> locale.equals(svc.getLocale())).findFirst().orElse(services.stream()
                .filter(svc -> locale.getLanguage().equals(svc.getLocale().getLanguage())).findFirst().orElse(null));
    }

}
