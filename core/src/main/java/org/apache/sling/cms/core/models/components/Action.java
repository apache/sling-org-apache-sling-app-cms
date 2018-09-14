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

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.core.models.BaseModel;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Action extends BaseModel {

    @Inject
    boolean modal;

    @Inject
    boolean target;

    @PostConstruct
    public void init() {
        System.out.println("here");
    }

    public String getClasses() {
        String response = "button";
        if (modal) {
            response += "  Fetch-Modal";
        }
        return response;
    }

    public String getTitle() {
        return xss.encodeForHTMLAttr(get("title"));
    }

    public String getDataPath() {
        return get("ajaxPath", ".Main-Content form");
    }

    public String getIcon() {
        return String.format("jam jam-%s", get("icon", "file"));
    }

    public String getTarget() {
        if (target) {
            return "target='_blank'";
        }
        return "";
    }

}
