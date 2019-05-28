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
package org.apache.sling.cms;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;

public class Reference {

    private final String property;
    private final Resource resource;

    public Reference(Resource resource, String property) {
        super();
        this.resource = resource;
        this.property = property;
    }

    /**
     * @return the property
     */
    public String getProperty() {
        return property;
    }

    public String getSubPath() {
        return StringUtils.replaceOnce(resource.getPath(), getContainingPage().getPath(), "");
    }

    /**
     * @return the resource
     */
    public Resource getResource() {
        return resource;
    }

    public boolean isPage() {
        PageManager pageMgr = resource.adaptTo(PageManager.class);
        if (pageMgr != null) {
            return pageMgr.getPage() != null;
        }
        return false;
    }

    public Page getContainingPage() {
        PageManager pageMgr = resource.adaptTo(PageManager.class);
        if (pageMgr != null) {
            return pageMgr.getPage();
        }
        return null;
    }

    @Override
    public String toString() {
        return resource.getPath() + "@" + property;
    }

}
