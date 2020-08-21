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
package org.apache.sling.cms.core.internal.models;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import javax.inject.Inject;

import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.util.Text;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.File;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

/**
 * A model representing a file
 */
@Model(adaptables = Resource.class, adapters = File.class)
public class FileImpl extends PublishableResourceImpl implements File {

    private final String contentType;

    @Inject
    public FileImpl(@Self Resource resource) {
        super(resource);
        this.contentType = Optional.ofNullable(this.getContentResource())
                .map(r -> r.getValueMap().get(JcrConstants.JCR_MIMETYPE, String.class)).orElse(null);
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public ValueMap getMetadata() {
        Resource metadata = this.getContentResource().getChild(CMSConstants.NN_METADATA);
        Map<String, Object> data = new TreeMap<>();
        if (metadata != null) {
            metadata.getValueMap().entrySet()
                    .forEach(e -> data.put(Text.unescapeIllegalJcrChars(e.getKey()), e.getValue()));
        }
        data.remove(JcrConstants.JCR_PRIMARYTYPE);
        return new ValueMapDecorator(data);
    }

}
