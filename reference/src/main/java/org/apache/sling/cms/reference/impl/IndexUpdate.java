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
package org.apache.sling.cms.reference.impl;

import org.apache.jackrabbit.oak.api.Type;
import org.apache.jackrabbit.oak.plugins.index.IndexConstants;
import org.apache.jackrabbit.oak.spi.lifecycle.RepositoryInitializer;
import org.apache.jackrabbit.oak.spi.state.NodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = RepositoryInitializer.class, property = { Constants.SERVICE_RANKING + ":Integer=100" })
public class IndexUpdate implements RepositoryInitializer {

    private static final Logger log = LoggerFactory.getLogger(IndexUpdate.class);

    @Override
    public void initialize(@NotNull NodeBuilder builder) {
        log.info("Initializing Reference Index Updates");
        NodeBuilder indexRoot = builder.child(IndexConstants.INDEX_DEFINITIONS_NAME);
        amendSlingPageIndex(indexRoot);
    }

    private void amendSlingPageIndex(NodeBuilder indexRoot) {
        log.info("amendSlingPageIndex");
        NodeBuilder propertiesRoot = indexRoot.child("slingPage").child("indexRules").child("sling:Page")
                .child("properties");

        NodeBuilder hideInSitemap = propertiesRoot.child("hideInSitemap");
        hideInSitemap.setProperty("jcr:primaryType", "nt:unstructured", Type.NAME);
        hideInSitemap.setProperty("propertyIndex", true, Type.BOOLEAN);
        hideInSitemap.setProperty("name", "jcr:content/hideInSitemap", Type.STRING);
        hideInSitemap.setProperty("type", "Boolean", Type.STRING);
        hideInSitemap.setProperty("nullCheckEnabled", true, Type.BOOLEAN);

        NodeBuilder publishDate = propertiesRoot.child("publishDate");
        publishDate.setProperty("jcr:primaryType", "nt:unstructured", Type.NAME);
        publishDate.setProperty("propertyIndex", true, Type.BOOLEAN);
        publishDate.setProperty("name", "jcr:content/publishDate", Type.STRING);
        publishDate.setProperty("type", "String", Type.STRING);
        publishDate.setProperty("nullCheckEnabled", true, Type.BOOLEAN);
        publishDate.setProperty("ordered", true, Type.BOOLEAN);

    }

}
