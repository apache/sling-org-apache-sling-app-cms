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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AvailableActions {

    @Inject
    @Self
    SlingHttpServletRequest slingRequest;
    
    public List<Resource> getChildren() {
        System.out.println(slingRequest.getContextPath());
        System.out.println(slingRequest.getResource().getPath());
        String type = slingRequest.getResource().getValueMap().get("jcr:primaryType", "sling:File");
        System.out.println(type);
        Resource resource = slingRequest.getResourceResolver().resolve("/libs/sling-cms/actions/"+type);
        
        List<Resource> list = new ArrayList<>();
        resource.listChildren().forEachRemaining(list::add);
        System.out.println(list.toString());
        return list;
    }

}
