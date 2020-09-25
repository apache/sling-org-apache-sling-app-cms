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
package org.apache.sling.cms;

import static org.junit.Assert.assertEquals;
import java.util.stream.Stream;

import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ResourceTreeTest {

    @Rule
    public final SlingContext context = new SlingContext();

    @Before
    public void init() {
        context.load().json("/content.json", "/content");
    }

    @Test
    public void testSimple() {
        context.currentResource("/content");
        Stream<ResourceTree> tree = ResourceTree.stream(context.currentResource());
        assertEquals(16, tree.count());
    }

    @Test
    public void testFilterType() {
        context.currentResource("/content/apache/sling-apache-org");
        Stream<ResourceTree> tree = ResourceTree.stream(context.currentResource(), "sling:File");
        assertEquals(3, tree.map(rt -> {
            assertEquals("sling:File", rt.getResource().getResourceType());
            return rt;
        }).count());
    }

    @Test
    public void testPredicates() {
        context.currentResource("/content/apache");
        Stream<ResourceTree> tree = ResourceTree.stream(context.currentResource(), (r) -> true,
                rt -> "sling:Page".equals(rt.getResource().getResourceType()));
        assertEquals(2, tree.map(rt -> {
            assertEquals("sling:Page", rt.getResource().getResourceType());
            return rt;
        }).count());
    }

}
