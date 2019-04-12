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
package org.apache.sling.cms.core.internal.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jcr.query.Query;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.cms.Component;
import org.apache.sling.cms.ComponentManager;
import org.apache.sling.models.annotations.Model;

/**
 * A model for managing sling:Components
 */
@Model(adaptables = { ResourceResolver.class }, adapters = ComponentManager.class)
public class ComponentManagerImpl implements ComponentManager {

    private ResourceResolver resolver;

    private Map<String, List<Component>> componentCache = null;

    public ComponentManagerImpl(Resource resource) {
        this.resolver = resource.getResourceResolver();
    }

    public ComponentManagerImpl(ResourceResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * Returns all of the components in Sling CMS with a component type specified.
     * 
     * @return a list of the components
     */
    @Override
    public List<Component> getAllComponents() {
        Set<Component> allComponents = new HashSet<>();
        if (componentCache == null) {
            loadComponents();
        }
        for (List<Component> components : componentCache.values()) {
            allComponents.addAll(components);
        }
        return new ArrayList<>(allComponents);
    }

    /**
     * Returns a Map of the components with a component type specified with the key
     * of the map being the component type and the value being the list of
     * components for that type.
     * 
     * @return the components organized by componentType
     */
    @Override
    public Map<String, List<Component>> getComponentsByType() {
        if (componentCache == null) {
            loadComponents();
        }
        return componentCache;
    }

    /**
     * Returns a list of all of the registered component types
     * 
     * @return the list of component types
     */
    @Override
    public List<String> getComponentTypes() {
        if (componentCache == null) {
            loadComponents();
        }
        List<String> types = new ArrayList<>(componentCache.keySet());
        Collections.sort(types);
        return types;
    }

    private void loadComponents() {
        Iterator<Resource> components = resolver
                .findResources("SELECT * FROM [sling:Component] WHERE [componentType] IS NOT NULL", Query.JCR_SQL2);
        componentCache = new HashMap<>();
        while (components.hasNext()) {
            Resource cmpRsrc = components.next();
            Component component = cmpRsrc.adaptTo(Component.class);
            if (component != null) {
                for (String type : component.getComponentType()) {
                    if (!componentCache.containsKey(type)) {
                        componentCache.put(type, new ArrayList<Component>());
                    }
                    componentCache.get(type).add(component);
                }
            }
        }
    }
}
