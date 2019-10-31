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
import java.util.List;

import javax.jcr.RepositoryException;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.cms.core.internal.CommonUtils;
import org.apache.sling.cms.core.internal.SimplePrincipal;
import org.apache.sling.servlets.post.Modification;
import org.apache.sling.servlets.post.PostOperation;
import org.apache.sling.servlets.post.PostResponse;
import org.apache.sling.servlets.post.SlingPostConstants;
import org.apache.sling.servlets.post.SlingPostProcessor;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The <code>CreateGroupOperation</code> will create a new group
 */
@Component(immediate = true, service = { PostOperation.class }, property = PostOperation.PROP_OPERATION_NAME
        + "=creategroup")
public class CreateGroupOperation implements PostOperation {

    private static final Logger log = LoggerFactory.getLogger(CreateGroupOperation.class);

    @Override
    public void run(SlingHttpServletRequest request, PostResponse response, SlingPostProcessor[] processors) {
        final List<Modification> changes = new ArrayList<>();
        try {

            String name = request.getParameter(SlingPostConstants.RP_NODE_NAME);

            ResourceResolver resolver = request.getResourceResolver();
            UserManager userManager = CommonUtils.getUserManager(resolver);

            if (userManager.getAuthorizable(new SimplePrincipal(name)) != null) {
                throw new RepositoryException("Authorizable with id " + name + " already exists");
            }
            String intermediatePath = StringUtils.substringBeforeLast(request.getResource().getPath(), "/")
                    .replaceAll("\\/home\\/groups\\/?", "");
            Group group = userManager.createGroup(name, new SimplePrincipal(name), intermediatePath);

            // invoke processors
            if (processors != null) {
                for (SlingPostProcessor processor : processors) {
                    processor.process(request, changes);
                }
            }

            request.getResourceResolver().commit();

            response.setPath(group.getPath());
            response.onCreated(group.getPath());
        } catch (Exception e) {
            log.warn("Failed to create group", e);
            response.setError(e);
        }
    }

}
