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
        + "=members")
public class MembersOperation implements PostOperation {

    private static final Logger log = LoggerFactory.getLogger(MembersOperation.class);
    public static final String PN_MEMBERS = ":members";

    @Override
    public void run(SlingHttpServletRequest request, PostResponse response, SlingPostProcessor[] processors) {
        final List<Modification> changes = new ArrayList<>();
        try {

            List<String> auths = new ArrayList<>();
            Optional.ofNullable(request.getParameterValues(PN_MEMBERS)).ifPresent(p -> auths.addAll(Arrays.asList(p)));

            AuthorizableWrapper groupWrapper = Optional
                    .ofNullable(request.getResource().adaptTo(AuthorizableWrapper.class))
                    .orElseThrow(() -> new RepositoryException("Failed to get group"));
            if (!groupWrapper.getAuthorizable().isGroup()) {
                throw new RepositoryException("Provided authorizable is not a group");
            }
            Group group = (Group) groupWrapper.getAuthorizable();
            response.setPath(group.getPath());
            changes.add(Modification.onModified(group.getPath()));

            group.getDeclaredMembers().forEachRemaining(member -> {
                try {
                    if (!auths.contains(member.getPath())) {
                        log.debug("Removing member {} from {}", member, group);
                        group.removeMember(member);
                        changes.add(Modification.onModified(member.getPath()));
                    } else {
                        auths.remove(member.getPath());
                    }
                } catch (RepositoryException e) {
                    log.warn("Failed to remove members", e);
                }
            });

            for (String path : auths) {
                Resource resource = request.getResourceResolver().getResource(path);
                if (resource == null) {
                    throw new RepositoryException("Failed to resolve authorizable at " + path);
                }
                Authorizable authorizable = Optional.ofNullable(resource.adaptTo(AuthorizableWrapper.class))
                        .map(AuthorizableWrapper::getAuthorizable)
                        .orElseThrow(() -> new RepositoryException("Failed to get authorizable from: " + resource));
                group.addMember(authorizable);
                changes.add(Modification.onModified(authorizable.getPath()));
                log.debug("Adding member {} to {}", authorizable, group);
            }

            // invoke processors
            if (processors != null) {
                for (SlingPostProcessor processor : processors) {
                    processor.process(request, changes);
                }
            }

            request.getResourceResolver().commit();

            response.onModified(group.getPath());
        } catch (Exception e) {
            log.warn("Failed to update members", e);
            response.setError(e);
        }
    }

}
