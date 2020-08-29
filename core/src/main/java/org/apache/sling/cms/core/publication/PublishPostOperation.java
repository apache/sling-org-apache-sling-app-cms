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

import java.util.ArrayList;
import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.cms.PublishableResource;
import org.apache.sling.cms.publication.PublicationManagerFactory;
import org.apache.sling.servlets.post.Modification;
import org.apache.sling.servlets.post.PostOperation;
import org.apache.sling.servlets.post.PostResponse;
import org.apache.sling.servlets.post.SlingPostProcessor;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Sling Post Operation to publish content
 */
@Component(immediate = true, service = { PostOperation.class }, property = PostOperation.PROP_OPERATION_NAME
        + "=publish")
public class PublishPostOperation implements PostOperation {

    private static final Logger log = LoggerFactory.getLogger(PublishPostOperation.class);

    @Reference
    private PublicationManagerFactory publicationManagerFactory;

    @Override
    public void run(SlingHttpServletRequest request, PostResponse response, SlingPostProcessor[] processors) {
        final List<Modification> changes = new ArrayList<>();
        try {
            response.setPath(request.getResource().getPath());
            publicationManagerFactory.getPublicationManager()
                    .publish(request.getResource().adaptTo(PublishableResource.class));

            if (processors != null) {
                for (SlingPostProcessor processor : processors) {
                    processor.process(request, changes);
                }
            }

            request.getResourceResolver().commit();

            response.onModified(request.getResource().getPath());
        } catch (Exception e) {
            log.error("Failed to publish", e);
            response.setError(e);
        }
    }
}