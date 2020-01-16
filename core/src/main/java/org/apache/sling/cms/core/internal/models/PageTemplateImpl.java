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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.ComponentPolicy;
import org.apache.sling.cms.PageTemplate;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

/**
 * A simple model for representing a page template
 */
@Model(adaptables = Resource.class, adapters = PageTemplate.class)
public class PageTemplateImpl implements PageTemplate {

    @Inject
    @Optional
    private String[] allowedPaths;

    @Inject
    private List<Resource> fields;

    @Inject
    @Optional
    private List<Resource> policies;

    private Resource resource;

    @Inject
    @Optional
    private String template;

    @Inject
    @Named(CMSConstants.PN_TITLE)
    private String title;

    public PageTemplateImpl(Resource resource) {
        this.resource = resource;
    }

    /**
     * @return the allowedPaths
     */
    @Override
    public String[] getAllowedPaths() {
        if (allowedPaths == null) {
            return new String[0];
        }
        return allowedPaths;
    }

    @Override
    public List<ComponentPolicy> getComponentPolicies() {
        if(policies != null){
            return policies.stream().map(p -> p.adaptTo(ComponentPolicy.class)).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }

    }

    /**
     * @return the fields
     */
    public List<Resource> getFields() {
        return fields;
    }

    /**
     * @return the resource
     */
    @Override
    public Resource getResource() {
        return resource;
    }

    /**
     * @return the template
     */
    @Override
    public String getTemplate() {
        return template;
    }

    /**
     * @return the title
     */
    @Override
    public String getTitle() {
        return title;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PageTemplateImpl [allowedPaths=" + Arrays.toString(allowedPaths) + ", fields=" + fields + ", policies="
                + getComponentPolicies() + ", resource=" + resource + ", template=" + template + ", title=" + title
                + "]";
    }

}
