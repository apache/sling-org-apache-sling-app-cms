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
package org.apache.sling.cms.core.models.components.column;

import java.util.Calendar;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.core.models.BaseModel;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LastModified extends BaseModel {
    
    private String subPath = "";
    
    @PostConstruct
    private void init() {
        if (resource.getChild("jcr:content") != null) {
            subPath = "jcr:content/";
        }
    }
    
    public String getLastModified() {
        Calendar cal = get(subPath +"jcr:lastModified",Calendar.class);
        if (cal == null) {
            cal = get(subPath +"jcr:created",Calendar.class);
            if (cal == null) {
                return "";
            }
        }
        return xss.encodeForHTML(cal.getTime().toString());
    }
    
    public String getLastModifiedBy() {
        String name = get(subPath+"jcr:lastModifiedBy",String.class);
        if (name == null) {
            name = get(subPath+"jcr:createdBy","");
        }
        return xss.encodeForHTML(name);
    }
    
    public String getTitle() {
        return getLastModified()+" - " + getLastModifiedBy();
    }

}
