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

import java.io.IOException;
import java.io.InputStream;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.transformer.TransformationHandler;
import org.osgi.service.component.annotations.Component;

import net.coobird.thumbnailator.Thumbnails.Builder;

/**
 * A transformer for resizing an image
 */
@Component(service = TransformationHandler.class, property = { TransformationHandler.HANDLER_RESOURCE_TYPE
        + "=sling-cms/components/caconfig/transformationhandlers/size" }, immediate = true)
public class SizeHandler implements TransformationHandler {

    public static final String PN_HEIGHT = "height";
    public static final String PN_WIDTH = "width";

    @Override
    public String getResourceType() {
        return "sling-cms/components/caconfig/transformationhandlers/size";
    }

    @Override
    public void handle(Builder<? extends InputStream> builder, Resource config) throws IOException {
        int width = config.getValueMap().get(PN_WIDTH, -1);
        int height = config.getValueMap().get(PN_HEIGHT, -1);
        builder.size(width, height);
    }

}
