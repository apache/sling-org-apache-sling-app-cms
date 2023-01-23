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
package org.apache.sling.cms.core.internal;

import static org.junit.Assert.assertTrue;

import org.apache.jackrabbit.oak.InitialContent;
import org.apache.jackrabbit.oak.plugins.memory.EmptyNodeState;
import org.apache.jackrabbit.oak.spi.state.NodeBuilder;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

public class IndexCreatorTest {

    private @NotNull NodeBuilder indexRoot;

    @Before
    public void setup() {
        NodeBuilder builder = EmptyNodeState.EMPTY_NODE.builder();
        new InitialContent().initialize(builder);
        new IndexCreator().initialize(builder);
        indexRoot = builder.getChildNode("oak:index");
    }

    @Test
    public void verifyNtFolder() {
        assertTrue(indexRoot.hasChildNode("ntFolder"));
    }

    @Test
    public void verifyNtHierarchyNode() {
        assertTrue(indexRoot.hasChildNode("ntHierarchyNode"));
    }

    @Test
    public void verifySlingComponent() {
        assertTrue(indexRoot.hasChildNode("slingComponent"));
    }

    @Test
    public void verifySlingEventJob() {
        assertTrue(indexRoot.hasChildNode("slingeventJob"));
    }

    @Test
    public void verifySlingFile() {
        assertTrue(indexRoot.hasChildNode("slingFile"));
    }

    @Test
    public void verifySlingPage() {
        assertTrue(indexRoot.hasChildNode("slingPage"));
    }

    @Test
    public void verifySlingTaxonomy() {
        assertTrue(indexRoot.hasChildNode("slingTaxonomy"));
    }

}
