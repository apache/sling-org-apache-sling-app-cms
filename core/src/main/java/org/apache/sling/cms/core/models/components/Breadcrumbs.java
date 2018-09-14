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
package org.apache.sling.cms.core.models.components;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.cms.core.models.BaseModel;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;

/**
 * Logic for the Suffix BreadCrumb Component
 * 
 * 
 *
 */
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Breadcrumbs extends BaseModel {

    @Inject
    @Via("resource")
    @Default(intValues = 2)
    int depth;


    @Inject
    @Via("resource")
    @Default(values = "jcr:title")
    String titleProp;

    Resource suffixResource;

    List<PathData> pathData = new ArrayList<>();

    @PostConstruct
    public void postConstruct() {
        suffixResource = slingRequest.getRequestPathInfo().getSuffixResource();
        if (suffixResource == null) {
            return;
        }
        String prefix = slingRequest.getPathInfo();
        boolean first = true;
        while (suffixResource.getParent() != null) {
            String suffix = suffixResource.getPath();
            pathData.add(0, new PathData(suffix, prefix + getTitle(suffixResource), first));
            if (first) {
                first = false;
            }
            suffixResource = suffixResource.getParent();
        }
        while (--depth > 0) {
            pathData.remove(0);
        }
    }

    private String getTitle(Resource resource) {
        String title = get("jcr:title", String.class);
        if (title != null) {
            return title;
        }
        title = get("jcr:content/jcr:title", String.class);
        if (title != null) {
            return title;
        }
        return resource.getName();
    }

    public List<PathData> getPathData() {
        return pathData;
    }

    public static class PathData {

        private String href;
        private String title;
        private boolean first;

        public PathData(String href, String title, boolean first) {
            this.href = href;
            this.title = title;
            this.first = first;
        }

        public String getHref() {
            return href; // prefix + resource path
        }

        // ${parent.valueMap['jcr:title'] != null ? parent.valueMap['jcr:title'] :
        // parent.valueMap['jcr:content/jcr:title']}" default="${parent.name}"
        public String getTitle() {
            return title;
        }

        public String getAria() {
            if (first) {
                return "aria-current='page'";
            }
            return "";
        }

        public String getClassAttr() {
            if (first) {
                return "class='has-background-grey-lighter'";
            }
            return "";
        }
    }
}
