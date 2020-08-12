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

import org.apache.sling.api.adapter.AdapterFactory;
import org.apache.sling.cms.publication.PUBLICATION_MODE;
import org.apache.sling.cms.publication.PublicationManager;
import org.apache.sling.cms.publication.PublicationManagerFactory;
import org.apache.sling.distribution.Distributor;
import org.osgi.service.component.ComponentException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.metatype.annotations.Designate;

@Component(service = { PublicationManagerFactory.class, AdapterFactory.class }, property = {
        AdapterFactory.ADAPTABLE_CLASSES + "=org.apache.sling.api.SlingHttpServletRequest",
        AdapterFactory.ADAPTABLE_CLASSES + "=org.apache.sling.api.resource.ResourceResolver",
        AdapterFactory.ADAPTER_CLASSES + "=org.apache.sling.cms.publication.PublicationManager" })
@Designate(ocd = PublicationConfig.class)
public class PublicationManagerFactoryImpl implements PublicationManagerFactory, AdapterFactory {

    @Reference
    private EventAdmin eventAdmin;

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private Distributor distributor;

    private PUBLICATION_MODE publicationMode;

    private String[] agents;

    @Activate
    public void activate(PublicationConfig config) {
        this.publicationMode = config.publicationMode();
        this.agents = config.agents();
    }

    @Override
    public PUBLICATION_MODE getPublicationMode() {
        return this.publicationMode;
    }

    @Override
    public PublicationManager getPublicationManager() {
        if (publicationMode == PUBLICATION_MODE.STANDALONE) {
            return new StandalonePublicationManager(eventAdmin);
        } else if (distributor != null) {
            return new ContentDistributionPublicationManager(distributor, agents, eventAdmin);
        } else {
            throw new ComponentException("Sling Content Distribution not found on this instance");
        }
    }

    @Override
    public <T> T getAdapter(Object adaptable, Class<T> type) {
        return type.cast(getPublicationManager());
    }

}