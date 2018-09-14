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
package org.apache.sling.cms.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.xss.XSSAPI;

@Model(adaptables = { SlingHttpServletRequest.class,
        Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BaseModel {

    @OSGiService
    public XSSAPI xss;

    @Inject
    @Self
    public Resource resource;

    @Inject
    @Self
    public SlingHttpServletRequest slingRequest;

    private ValueMap valueMap;

    @PostConstruct
    public void baseResourceModel() {
        if (resource == null) {
            resource = slingRequest.getResource();
        }
        valueMap = resource.getValueMap();
    }

    /**
     * Convenience method for scripting languages such as the JSP EL.
     * 
     * @param key
     * @return
     */
    public String get(String key) {
        return valueMap.get(key, "");
    }

    /**
     * Convenience method for scripting languages such as the JSP EL.
     * 
     * @param key
     * @return
     */
    public <T> T get(String key, T def) {
        return valueMap.get(key, def);
    }
    
    /**
     * Convenience method for scripting languages such as the JSP EL.
     * 
     * @param key
     * @return
     */
    public <T> T get(String key, Class<T> def) {
        return valueMap.get(key, def);
    }

}
