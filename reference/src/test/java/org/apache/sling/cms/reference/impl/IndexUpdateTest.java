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

import static org.junit.Assert.assertTrue;

import org.apache.jackrabbit.oak.InitialContent;
import org.apache.jackrabbit.oak.plugins.memory.EmptyNodeState;
import org.apache.jackrabbit.oak.spi.state.NodeBuilder;
import org.apache.sling.cms.core.internal.IndexCreator;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

public class IndexUpdateTest {

    private @NotNull NodeBuilder propertiesRoot;

    @Before
    public void setup() {
        NodeBuilder builder = EmptyNodeState.EMPTY_NODE.builder();
        new InitialContent().initialize(builder);
        new IndexCreator().initialize(builder);
        new IndexUpdate().initialize(builder);
        propertiesRoot = builder.getChildNode("oak:index").getChildNode("slingPage").getChildNode("indexRules")
                .child("sling:Page").getChildNode("properties");
        ;
    }

    @Test
    public void verifyHideInSitemap() {
        assertTrue(propertiesRoot.hasChildNode("hideInSitemap"));
    }

    @Test
    public void verifyPublishDate() {
        assertTrue(propertiesRoot.hasChildNode("publishDate"));
    }

}
