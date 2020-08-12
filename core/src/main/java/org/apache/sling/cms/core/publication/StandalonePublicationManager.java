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

import java.util.Calendar;
import java.util.Optional;

import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.PublishableResource;
import org.apache.sling.cms.publication.PUBLICATION_MODE;
import org.apache.sling.cms.publication.PublicationEvent;
import org.apache.sling.cms.publication.PublicationException;
import org.apache.sling.cms.publication.PublicationManager;
import org.apache.sling.cms.publication.PublicationType;
import org.osgi.service.event.EventAdmin;

/**
 * Implementation of the PublicationManager interface for standalone instances.
 */
public class StandalonePublicationManager implements PublicationManager {

    private EventAdmin eventAdmin;

    public StandalonePublicationManager(EventAdmin eventAdmin) {
        this.eventAdmin = eventAdmin;
    }

    @Override
    public void publish(PublishableResource resource) throws PublicationException {
        try {
            ModifiableValueMap properties = Optional
                    .ofNullable(resource.getContentResource().adaptTo(ModifiableValueMap.class))
                    .orElseThrow(() -> new PublicationException("Cannot modify resource"));
            properties.put(CMSConstants.PN_PUBLISHED, true);
            properties.put(CMSConstants.PN_LAST_PUBLICATION, Calendar.getInstance());
            properties.put(CMSConstants.PN_LAST_PUBLICATION_TYPE, PublicationType.ADD.toString());
            resource.getResource().getResourceResolver().commit();

            eventAdmin.postEvent(PublicationEvent.publish(resource, false));
        } catch (PersistenceException e) {
            throw new PublicationException("Cannot save publication status", e);
        }
    }

    @Override
    public void unpublish(PublishableResource resource) throws PublicationException {
        try {
            ModifiableValueMap properties = Optional
                    .ofNullable(resource.getContentResource().adaptTo(ModifiableValueMap.class))
                    .orElseThrow(() -> new PublicationException("Cannot modify resource"));
            properties.put(CMSConstants.PN_PUBLISHED, false);
            properties.put(CMSConstants.PN_LAST_PUBLICATION, Calendar.getInstance());
            properties.put(CMSConstants.PN_LAST_PUBLICATION_TYPE, PublicationType.DELETE.toString());
            resource.getResource().getResourceResolver().commit();

            eventAdmin.postEvent(PublicationEvent.unpublish(resource, false));
        } catch (PersistenceException e) {
            throw new PublicationException("Cannot save publication status", e);
        }
    }

    @Override
    public PUBLICATION_MODE getPublicationMode() {
        return PUBLICATION_MODE.STANDALONE;
    }

}