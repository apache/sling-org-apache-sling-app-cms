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
import java.util.stream.Collectors;

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

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContentTable extends BaseModel {

    @Inject
    @Via("resource")
    private List<Resource> columns;
    
    @Inject
    @Via("resource")
    @Default(values="/")
    private String defaultPath;

    private String[] types;


    @PostConstruct
    public void init() {
        ValueMap data = resource.getChild("columns").getValueMap();
        types = data.get("resourceTypes", new String[]{});
    }
    
    public List<ColumnData> getColumnData() {
        return columns.stream().map(ColumnData::new).filter(ColumnData::isEligible).collect(Collectors.toList());
    }

    public List<ChildResourceData> getChildren() {
        Resource suffix = slingRequest.getRequestPathInfo().getSuffixResource();
        if (suffix == null) {
            suffix = slingRequest.getResourceResolver().getResource(defaultPath);
            if (suffix == null) {
                return Collections.emptyList();
            }
        }
        List<ChildResourceData> response = new ArrayList<>();
        suffix.listChildren().forEachRemaining(child -> {
            for (String type:types) {
                if (child.getResourceType().equals(type)) {
                    response.add(new ChildResourceData(child));
                }
            }
        });
        return response;
    }

    public class ColumnData {

        private Resource resource;

        private String name;

        public ColumnData(Resource resource) {
            this.resource = resource;
            this.name = resource.getName();
            
        }

        public String getClassString() {
            String reply = "";
            switch (name) {
            case "actions":
                reply = "is-hidden";
                break;
            case "publish":
                reply = "has-text-centered";
                break;
            }
            return reply;
        }

        public String getName() {
            return name;
        }

        public String getTitle() {
            return resource.getValueMap().get("jcr:title", "foo");
        }

        public String getFieldResourceType() {
            return resource.getValueMap().get("sling:resourceType", "foo");
        }
        
        public Resource getResource() {
            return resource;
        }

        public boolean isEligible() {
            return true;
        }
    }

    public class ChildResourceData {

        private Resource resource;

        public ChildResourceData(Resource resource) {
            this.resource = resource;
        }

        public String getPath() {
            return resource.getPath();
        }

        public String getDataType() {
            return resource.getResourceType();
        }

        public boolean isEligible() {
            return true;
        }
    }

}
