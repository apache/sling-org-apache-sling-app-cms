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
package org.apache.sling.cms.core.internal.bindings;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.scripting.api.BindingsValuesProvider;
import org.osgi.service.component.annotations.Component;

import javax.script.Bindings;

@Component(
        property = {"javax.script.name=sightly"}
)
public class SightlyBindings implements BindingsValuesProvider {
    public SightlyBindings(){

    }

    @Override
    public void addBindings(Bindings bindings) {
        if (!bindings.containsKey("isEditor") && bindings.containsKey("request")){
            SlingHttpServletRequest request = ((SlingHttpServletRequest)bindings.get("request"));
            Object cmsEditEnabled = request.getAttribute("cmsEditEnabled");
            bindings.put("isEditor", cmsEditEnabled);
        }
    }

}
