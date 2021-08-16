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

import java.util.List;

import javax.jcr.Node;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.servlets.post.Modification;
import org.apache.sling.servlets.post.SlingPostProcessor;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The <code>CheckoutPostOperation</code> service will checkout the node after
 * it is restored so it can be edited.
 */
@Component(immediate = true, service = { SlingPostProcessor.class })
public class CheckoutPostOperation implements SlingPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(CheckoutPostOperation.class);

    private static final String PN_CHECKOUT = ":checkoutPostOp";

    @Override
    public void process(SlingHttpServletRequest request, final List<Modification> changes) throws Exception {
        if ("true".equals(request.getParameter(PN_CHECKOUT))) {
            Resource resource = request.getResource();
            Node node = resource.adaptTo(Node.class);

            if (node == null) {
                log.warn("Resource {} not backed by Node", resource);
                return;
            }

            log.debug("Checking out node {}", node.getPath());
            node.getSession().getWorkspace().getVersionManager().checkout(node.getPath());
            changes.add(Modification.onCheckout(resource.getPath()));
        }

    }

}
