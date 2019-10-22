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
package org.apache.sling.cms.core.usergenerated.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.cms.usergenerated.UserGeneratedContentService.APPROVE_ACTION;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.apache.sling.servlets.post.Modification;
import org.apache.sling.servlets.post.PostOperation;
import org.apache.sling.servlets.post.PostResponse;
import org.apache.sling.servlets.post.SlingPostProcessor;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The <code>ApproveUGCOperation</code> class will approve a piece of UGC,
 * moving or publishing it as appropriate
 */
@Component(immediate = true, service = { PostOperation.class }, property = PostOperation.PROP_OPERATION_NAME
        + "=ugcapprove")
public class ApproveUGCOperation implements PostOperation {

    private static final Logger log = LoggerFactory.getLogger(ApproveUGCOperation.class);

    @Override
    public void run(SlingHttpServletRequest request, PostResponse response, SlingPostProcessor[] processors) {

        log.trace("run");

        try {
            // calculate the paths
            String path = request.getResource().getPath();
            response.setPath(path);

            log.debug("Approving UGC {}", path);

            response.setParentLocation(request.getResource().getParent().getPath());

            final List<Modification> changes = new ArrayList<>();
            // perform the approval
            String targetPath = request.getResource().getValueMap().get("targetpath", String.class);
            APPROVE_ACTION action = APPROVE_ACTION
                    .valueOf(request.getResource().getValueMap().get("approveaction", String.class));
            if (action == APPROVE_ACTION.MOVE) {
                ResourceUtil.getOrCreateResource(request.getResourceResolver(), targetPath,
                        Collections.singletonMap(JcrConstants.JCR_PRIMARYTYPE, JcrResourceConstants.NT_SLING_FOLDER),
                        JcrResourceConstants.NT_SLING_FOLDER, false);
                for (Resource resource : request.getResource().getChildren()) {
                    log.debug("Moving {} to {}", resource.getPath(), targetPath);
                    changes.add(Modification.onMoved(resource.getPath(), targetPath));
                    request.getResourceResolver().move(resource.getPath(), targetPath);
                }
                changes.add(Modification.onDeleted(request.getResource().getPath()));
                request.getResourceResolver().delete(request.getResource());
            } else {
                ModifiableValueMap mvm = request.getResource().adaptTo(ModifiableValueMap.class);
                mvm.put("published", true);
                changes.add(Modification.onModified(request.getResource().getPath()));
            }

            // invoke processors
            if (processors != null) {
                for (SlingPostProcessor processor : processors) {
                    processor.process(request, changes);
                }
            }

            // check modifications for remaining postfix and store the base path
            final Map<String, String> modificationSourcesContainingPostfix = new HashMap<>();
            final Set<String> allModificationSources = new HashSet<>(changes.size());
            for (final Modification modification : changes) {
                final String source = modification.getSource();
                if (source != null) {
                    allModificationSources.add(source);
                    final int atIndex = source.indexOf('@');
                    if (atIndex > 0) {
                        modificationSourcesContainingPostfix.put(source.substring(0, atIndex), source);
                    }
                }
            }

            request.getResourceResolver().commit();

        } catch (

        Exception e) {
            log.error("Exception during response processing.", e);
            response.setError(e);

        }
    }
}
