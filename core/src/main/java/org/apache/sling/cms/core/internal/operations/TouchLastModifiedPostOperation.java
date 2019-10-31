/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.sling.cms.core.internal.operations;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.CMSUtils;
import org.apache.sling.servlets.post.Modification;
import org.apache.sling.servlets.post.ModificationType;
import org.apache.sling.servlets.post.SlingPostConstants;
import org.apache.sling.servlets.post.SlingPostProcessor;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The <code>TouchLastModifiedPostOperation</code> class will update the last
 * modified date and user of the parent publishable resource when a child
 * resource is modified
 */
@Component(immediate = true, service = { SlingPostProcessor.class }, property = Constants.SERVICE_RANKING + "=-1")
public class TouchLastModifiedPostOperation implements SlingPostProcessor {

    public static final String RP_UPDATE_REFERENCES = SlingPostConstants.RP_PREFIX + "updateReferences";

    private static final Logger log = LoggerFactory.getLogger(TouchLastModifiedPostOperation.class);

    @Override
    public void process(SlingHttpServletRequest request, final List<Modification> changes) throws Exception {

        // get the source of creates, modifies and reorders
        List<String> paths = changes.stream()
                .filter(m -> (m.getType() == ModificationType.CREATE || m.getType() == ModificationType.MODIFY
                        || m.getType() == ModificationType.ORDER))
                .map(Modification::getSource).collect(Collectors.toList());

        // get the destination for copies and moves
        paths.addAll(changes.stream()
                .filter(m -> (m.getType() == ModificationType.COPY || m.getType() == ModificationType.MOVE))
                .map(Modification::getDestination).collect(Collectors.toList()));
        log.debug("Found {} applicable paths", paths.size());
        
        // filter down to only distinct publishable resources
        Set<String> parentPaths = new HashSet<>();
        List<Resource> resources = paths.stream().map(p -> request.getResourceResolver().getResource(p))
                .map(CMSUtils::findPublishableParent).filter(p -> {
                    if (p == null || parentPaths.contains(p.getPath())) {
                        return false;
                    } else {
                        parentPaths.add(p.getPath());
                        return true;
                    }
                }).collect(Collectors.toList());
        log.debug("Filtered to {} publishable resources", resources.size());
        
        resources.forEach(r -> {
            Optional<ModifiableValueMap> op = Optional.ofNullable(r.getChild(JcrConstants.JCR_CONTENT))
                    .map(c -> c.adaptTo(ModifiableValueMap.class));
            op.ifPresent(mvm -> {
                log.debug("Updating last modified date on parent of {}", request.getResource());
                mvm.put(CMSConstants.PN_LAST_MODIFIED_BY, request.getResourceResolver().getUserID());
                mvm.put(JcrConstants.JCR_LASTMODIFIED, Calendar.getInstance());
            });
            if (!op.isPresent()) {
                log.warn("Unable to get modifiable value map for {}", request.getResource());
            }
        });
    }
}
