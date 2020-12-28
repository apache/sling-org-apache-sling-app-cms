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
package org.apache.sling.cms.publication;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.SlingConstants;
import org.apache.sling.cms.PublishableResource;
import org.osgi.service.event.Event;

/**
 * Helper for generating OSGi Events for publication events
 */
public class PublicationEvent extends Event {

    public static final String EVENT_PUBLISH = "org/apache/sling/cms/publication/PUBLISH";
    public static final String EVENT_UNPUBLISH = "org/apache/sling/cms/publication/UNPUBLISH";
    public static final String PN_PUBLICATION_TYPE = "publicationType";
    public static final String PN_DEEP = "isDeep";

    private PublicationEvent(String topic, Map<String, ?> properties) {
        super(topic, properties);
    }

    /**
     * Creates an event to be fired when a publication occurs.
     * 
     * @param resource the content published
     * @param deep     if true publish the resource and all child resources
     * @return the publish event
     */
    public static PublicationEvent publish(PublishableResource resource, boolean deep) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(SlingConstants.PROPERTY_PATH, resource.getPath());
        properties.put(PN_PUBLICATION_TYPE, PublicationType.ADD.toString());
        properties.put(PN_DEEP, deep);
        return new PublicationEvent(EVENT_PUBLISH, properties);
    }

    /**
     * Creates an event to be fired when a unpublish occurs.
     * 
     * @param resource the content unpublish
     * @param deep     if true unpublish the resource and all child resources
     * @return the unpublish event
     */
    public static PublicationEvent unpublish(PublishableResource resource, boolean deep) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(SlingConstants.PROPERTY_PATH, resource.getPath());
        properties.put(PN_PUBLICATION_TYPE, PublicationType.DELETE.toString());
        properties.put(PN_DEEP, deep);
        return new PublicationEvent(EVENT_UNPUBLISH, properties);
    }

}