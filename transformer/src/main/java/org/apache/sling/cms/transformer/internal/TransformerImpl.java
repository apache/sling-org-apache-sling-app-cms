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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.transformer.OutputFileFormat;
import org.apache.sling.cms.transformer.ThumbnailProvider;
import org.apache.sling.cms.transformer.Transformation;
import org.apache.sling.cms.transformer.TransformationHandler;
import org.apache.sling.cms.transformer.Transformer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;

@Component(service = Transformer.class)
public class TransformerImpl implements Transformer {

    private static final Logger log = LoggerFactory.getLogger(TransformerImpl.class);

    @Reference(cardinality = ReferenceCardinality.AT_LEAST_ONE, policyOption = ReferencePolicyOption.GREEDY)
    private List<TransformationHandler> handlers;

    @Reference(cardinality = ReferenceCardinality.AT_LEAST_ONE, policyOption = ReferencePolicyOption.GREEDY)
    private List<ThumbnailProvider> thumbnailProviders;

    public void addThumbnailProvider(ThumbnailProvider thumbnailProvider) {
        if (thumbnailProviders == null) {
            thumbnailProviders = new ArrayList<>();
        }
        this.thumbnailProviders.add(thumbnailProvider);
    }

    public void addTransformationHandler(TransformationHandler handler) {
        if (handlers == null) {
            handlers = new ArrayList<>();
        }
        handlers.add(handler);
    }

    public List<TransformationHandler> getHandlers() {
        return handlers;
    }

    private ThumbnailProvider getThumbnailProvider(Resource resource) throws IOException {
        return Lists.reverse(thumbnailProviders).stream().filter(tp -> tp.applies(resource)).findFirst()
                .orElseThrow(() -> new IOException("Unable to find thumbnail provider for: " + resource.getPath()));
    }

    /**
     * @return the thumbnailProviders
     */
    public List<ThumbnailProvider> getThumbnailProviders() {
        return thumbnailProviders;
    }

    public TransformationHandler getTransformationHandler(String resourceType) {
        return handlers.stream().filter(h -> resourceType.equals(h.getResourceType())).findFirst().orElse(null);
    }

    @Override
    public void transform(Resource resource, Transformation transformation, OutputFileFormat format, OutputStream out)
            throws IOException {
        ThumbnailProvider provider = getThumbnailProvider(resource);
        log.debug("Using thumbnail provider {} for resource {}", provider, resource);
        Builder<? extends InputStream> builder = Thumbnails.of(provider.getThumbnail(resource));
        for (Resource config : transformation.getHandlers()) {
            log.debug("Handling command: {}", config);
            TransformationHandler handler = getTransformationHandler(config.getResourceType());
            if (handler != null) {
                log.debug("Invoking handler {} for command {}", handler.getClass().getCanonicalName(),
                        config.getName());
                handler.handle(builder, config);
            } else {
                log.info("No handler found for: {}", config.getName());
            }
        }
        builder.outputFormat(format.toString());
        builder.toOutputStream(out);

    }

}
