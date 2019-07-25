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

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.sling.api.resource.Resource;

public class ResourceTree {

    private Resource root;

    public static Stream<ResourceTree> stream(Resource resource) {
        return new ResourceTree(resource).streamTree();
    }

    public static Stream<ResourceTree> stream(Resource resource, String filterType) {
        return new ResourceTree(resource).streamTree(filterType);
    }

    private ResourceTree(Resource root) {
        this.root = root;
    }

    public Resource getResource() {
        return root;
    }

    private Stream<ResourceTree> streamTree() {
        return Stream.concat(Stream.of(this), StreamSupport.stream(root.getChildren().spliterator(), false)
                .map(ResourceTree::new).flatMap(ResourceTree::streamTree));
    }

    private Stream<ResourceTree> streamTree(String filterType) {
        return Stream.concat(Stream.of(this),
                StreamSupport.stream(root.getChildren().spliterator(), false)
                        .filter(c -> filterType.equals(c.getResourceType())).map(ResourceTree::new)
                        .flatMap(rt -> rt.streamTree(filterType)));
    }
}