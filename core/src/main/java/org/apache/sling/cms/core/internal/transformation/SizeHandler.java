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
package org.apache.sling.cms.core.internal.transformation;

import java.io.IOException;
import java.io.InputStream;

import org.apache.sling.cms.transformation.TransformationHandler;
import org.osgi.service.component.annotations.Component;

import net.coobird.thumbnailator.Thumbnails.Builder;

@Component(service = TransformationHandler.class)
public class SizeHandler implements TransformationHandler {

    @Override
    public boolean applies(String command) {
        return command.startsWith("size-");
    }

    @Override
    public void handle(Builder<? extends InputStream> builder, String cmd) throws IOException {
        int width = -1;
        try {
            width = Integer.parseInt(cmd.split("\\-")[1], 10);
        } catch (NumberFormatException nfe) {
            throw new IOException("Failed to get width from " + cmd.split("\\-")[1]);
        }

        int height = -1;
        try {
            height = Integer.parseInt(cmd.split("\\-")[2], 10);
        } catch (NumberFormatException nfe) {
            throw new IOException("Failed to get height from " + cmd.split("\\-")[2]);
        }
        builder.size(width, height);
    }

}
