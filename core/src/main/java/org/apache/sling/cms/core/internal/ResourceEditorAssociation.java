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

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;

/**
 * OSGi Component for setting the editor based on a path pattern. Used for
 * constructing breadcrumbs.
 */
@Component(service = ResourceEditorAssociation.class, configurationPolicy = ConfigurationPolicy.REQUIRE, immediate = true)
@Designate(ocd = ResourceEditorAssociationConfig.class, factory = true)
public class ResourceEditorAssociation {

    private Pattern pathPattern;
    private String editor;
    private String resourceType;
    private String parentType;

    @Activate
    public void activate(ResourceEditorAssociationConfig config) {
        this.pathPattern = Pattern.compile(config.pathPattern());
        this.editor = config.editor();
        this.resourceType = config.resourceType();
        this.parentType = config.parentType();
    }

    public boolean matches(Resource resource) {
        if (StringUtils.isNotBlank(parentType) && !hasParentType(resource)) {
            return false;
        }
        if (StringUtils.isNotBlank(resourceType) && !resourceType.equals(resource.getResourceType())) {
            return false;
        }
        return pathPattern.matcher(resource.getPath()).matches();
    }

    private boolean hasParentType(Resource resource) {
        Resource parent = resource.getParent();
        if (parent != null && !parent.getResourceType().equals(parentType)) {
            return hasParentType(parent);
        }
        return parent != null && parent.getResourceType().equals(parentType);
    }

    public String getEditor() {
        return editor;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ResourceEditorAssociation [pathPattern=" + pathPattern + ", editor=" + editor + ", resourceType="
                + resourceType + ", parentType=" + parentType + "]";
    }

}
