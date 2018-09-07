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
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

@Model(adaptables = SlingHttpServletRequest.class,defaultInjectionStrategy=DefaultInjectionStrategy.OPTIONAL)
public class Breadcrumbs {

    @Inject
    int depth;

    @Inject
    @Named("jcr:title")
    String title;

    @Inject
    String prefix;
    
    @Inject
    Resource resource;
    
    @Self
    SlingHttpServletRequest servletRequest;
    
    Resource suffixResource;
    
    List<PathData> pathData = new ArrayList<>();

    @PostConstruct
    public void postConstruct() {
        suffixResource = servletRequest.getRequestPathInfo().getSuffixResource();
        pathData.add(new PathData(prefix+resource.getPath(),title));
    }
    
    public String getTitle() {
        return null;
    }
    
    public List<PathData> getPathData() {
        return pathData;
    }
    
    public String getString() {
        return "flounder";
    }
    
    public static class PathData{
        
        private String href;
        private String title;
        
        public PathData(String href, String title) {
            this.href = href;
            this.title = title;
        }
        public String getHref() {
            return href; //prefix + resource path
        }
        //${parent.valueMap['jcr:title'] != null ? parent.valueMap['jcr:title'] : parent.valueMap['jcr:content/jcr:title']}" default="${parent.name}"
        public String getTitle() {
            return title;
        }
        
    }

}
