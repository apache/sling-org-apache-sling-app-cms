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
package org.apache.sling.cms.core.publication;

import org.apache.sling.cms.publication.PublicationManagerFactory;
import org.apache.sling.discovery.PropertyProvider;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = PropertyProvider.class, property = {
        PropertyProvider.PROPERTY_PROPERTIES + "=" + PublicationPropertyProvider.ENDPOINT_PATHS,
        PropertyProvider.PROPERTY_PROPERTIES + "=" + PublicationPropertyProvider.INSTANCE_TYPE })
@Designate(ocd = PublicationPropertyProviderConfig.class)
public class PublicationPropertyProvider implements PropertyProvider {

    private static final Logger log = LoggerFactory.getLogger(PublicationPropertyProvider.class);
    public static final String ENDPOINT_PATHS = "pub.endpointPaths";
    public static final String INSTANCE_TYPE = "pub.instanceType";

    @Reference
    private PublicationManagerFactory publicationManagerFactory;

    private PublicationPropertyProviderConfig config;

    @Activate
    public void activate(PublicationPropertyProviderConfig config) {
        log.info("activate");
        this.config = config;
    }

    @Override
    public String getProperty(String name) {
        log.trace("getProperty({})", name);
        if (ENDPOINT_PATHS.equals(name)) {
            return config.endpointPath();
        } else if (INSTANCE_TYPE.equals(name)) {
            return publicationManagerFactory.getInstanceType().toString();
        }
        return null;
    }



}
