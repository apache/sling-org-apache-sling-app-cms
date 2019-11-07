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
package org.apache.sling.cms.transformer.internal;

import java.io.InputStream;
import java.util.Optional;

import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.cms.File;
import org.apache.sling.cms.transformer.ThumbnailProvider;
import org.osgi.service.component.annotations.Component;

import com.google.common.net.MediaType;

/**
 * A thumbnail provider for image files.
 */
@Component(service = ThumbnailProvider.class, immediate = true)
public class ImageThumbnailProvider implements ThumbnailProvider {

    @Override
    public boolean applies(Resource resource) {
        return (CMSConstants.NT_FILE.equals(resource.getResourceType())
                || JcrConstants.NT_FILE.equals(resource.getResourceType()))
                && Optional.ofNullable(resource.adaptTo(File.class))
                        .map(f -> MediaType.parse(f.getContentType()).is(MediaType.ANY_IMAGE_TYPE)).orElse(false);
    }

    @Override
    public InputStream getThumbnail(Resource resource) {
        return resource.adaptTo(InputStream.class);
    }

}
