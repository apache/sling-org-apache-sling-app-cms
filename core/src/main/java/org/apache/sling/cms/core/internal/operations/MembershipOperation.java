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
package org.apache.sling.cms.core.internal.operations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.jcr.RepositoryException;

import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.AuthorizableWrapper;
import org.apache.sling.servlets.post.Modification;
import org.apache.sling.servlets.post.PostOperation;
import org.apache.sling.servlets.post.PostResponse;
import org.apache.sling.servlets.post.SlingPostProcessor;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The <code>MembersOperation</code> will update the membership of a group.
 */
@Component(immediate = true, service = { PostOperation.class }, property = PostOperation.PROP_OPERATION_NAME
        + "=membership")
public class MembershipOperation implements PostOperation {

    private static final Logger log = LoggerFactory.getLogger(MembershipOperation.class);
    public static final String PN_MEMBERSHIP = ":membership";

    @Override
    public void run(SlingHttpServletRequest request, PostResponse response, SlingPostProcessor[] processors) {
        final List<Modification> changes = new ArrayList<>();
        try {

            List<String> groups = new ArrayList<>();
            Optional.ofNullable(request.getParameterValues(PN_MEMBERSHIP))
                    .ifPresent(p -> groups.addAll(Arrays.asList(p)));

            AuthorizableWrapper authWrapper = request.getResource().adaptTo(AuthorizableWrapper.class);

            Authorizable auth = authWrapper.getAuthorizable();
            response.setPath(auth.getPath());
            changes.add(Modification.onModified(auth.getPath()));

            auth.declaredMemberOf().forEachRemaining(group -> {
                try {
                    if (!groups.contains(group.getPath())) {
                        log.debug("Removing member {} from {}", auth, group);
                        group.removeMember(auth);
                        changes.add(Modification.onModified(group.getPath()));
                    } else {
                        groups.remove(group.getPath());
                    }
                } catch (RepositoryException e) {
                    log.warn("Failed to remove members", e);
                }
            });

            for (String path : groups) {
                Resource resource = request.getResourceResolver().getResource(path);
                if (resource == null) {
                    throw new RepositoryException("Failed to resolve authorizable at " + path);
                }
                Group group = (Group) resource.adaptTo(AuthorizableWrapper.class).getAuthorizable();
                group.addMember(auth);
                changes.add(Modification.onModified(group.getPath()));
                log.debug("Adding member {} to {}", auth, group);
            }

            // invoke processors
            if (processors != null) {
                for (SlingPostProcessor processor : processors) {
                    processor.process(request, changes);
                }
            }

            request.getResourceResolver().commit();

            response.onModified(auth.getPath());
        } catch (Exception e) {
            log.warn("Failed to update membership", e);
            response.setError(e);
        }
    }

}
