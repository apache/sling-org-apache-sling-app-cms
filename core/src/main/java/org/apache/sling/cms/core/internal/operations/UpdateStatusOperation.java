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
import java.util.Optional;

import javax.jcr.RepositoryException;

import org.apache.jackrabbit.api.security.user.User;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.cms.AuthorizableWrapper;
import org.apache.sling.servlets.post.Modification;
import org.apache.sling.servlets.post.PostOperation;
import org.apache.sling.servlets.post.PostResponse;
import org.apache.sling.servlets.post.SlingPostProcessor;
import org.jsoup.helper.StringUtil;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The <code>UpdateStatusOperation</code> will update the status of a user.
 */
@Component(immediate = true, service = { PostOperation.class }, property = PostOperation.PROP_OPERATION_NAME
        + "=updatestatus")
public class UpdateStatusOperation implements PostOperation {

    private static final Logger log = LoggerFactory.getLogger(UpdateStatusOperation.class);

    public static final String PN_REASON = ":reason";

    @Override
    public void run(SlingHttpServletRequest request, PostResponse response, SlingPostProcessor[] processors) {
        final List<Modification> changes = new ArrayList<>();
        try {

            String reason = request.getParameter(PN_REASON);

            AuthorizableWrapper authWrapper = Optional
                    .ofNullable(request.getResource().adaptTo(AuthorizableWrapper.class))
                    .orElseThrow(() -> new RepositoryException("Failed to get authorizable: " + request.getResource()));

            if (authWrapper.getAuthorizable().isGroup()) {
                throw new RepositoryException("Authorizable is not a user");
            }

            User user = (User) authWrapper.getAuthorizable();
            user.disable(StringUtil.isBlank(reason) ? null : reason);

            // invoke processors
            if (processors != null) {
                for (SlingPostProcessor processor : processors) {
                    processor.process(request, changes);
                }
            }

            request.getResourceResolver().commit();

            response.setPath(user.getPath());
            response.onCreated(user.getPath());
        } catch (Exception e) {
            log.warn("Failed to update user status", e);
            response.setError(e);
        }
    }

}
