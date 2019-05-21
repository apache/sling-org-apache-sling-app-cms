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
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.cms.File;
import org.apache.sling.cms.transformation.FileThumbnailTransformer;
import org.apache.sling.cms.transformation.OutputFileFormat;
import org.apache.sling.cms.transformation.ThumbnailProvider;
import org.apache.sling.cms.transformation.TransformationHandler;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;

@Component(service = FileThumbnailTransformer.class)
public class FileThumbnailTransformerImpl implements FileThumbnailTransformer {

    private static final Logger log = LoggerFactory.getLogger(FileThumbnailTransformerImpl.class);

    @Reference(cardinality = ReferenceCardinality.AT_LEAST_ONE, policyOption = ReferencePolicyOption.GREEDY)
    private List<TransformationHandler> handlers;

    @Reference(cardinality = ReferenceCardinality.AT_LEAST_ONE, policyOption = ReferencePolicyOption.GREEDY)
    private List<ThumbnailProvider> thumbnailProviders;

    @Override
    public TransformationHandler getTransformationHandler(String command) {
        return handlers.stream().filter(h -> h.applies(command)).findFirst().orElse(null);
    }

    /**
     * @param handlers the handlers to set
     */
    public void setHandlers(List<TransformationHandler> handlers) {
        this.handlers = handlers;
    }

    /**
     * @param thumbnailProviders the thumbnailProviders to set
     */
    public void setThumbnailProviders(List<ThumbnailProvider> thumbnailProviders) {
        this.thumbnailProviders = thumbnailProviders;
    }

    @Override
    public void transformFile(File file, String[] commands, OutputFileFormat format, OutputStream out)
            throws IOException {
        ThumbnailProvider provider = thumbnailProviders.stream().filter(tp -> tp.applies(file)).findFirst()
                .orElseThrow(() -> new IOException("Unable to find thumbnail provider for: " + file.getPath()));
        log.debug("Using thumbnail provider {} for file {}", provider, file);
        Builder<? extends InputStream> builder = Thumbnails.of(provider.getThumbnail(file));
        for (String command : commands) {
            if (StringUtils.isNotBlank(command)) {
                log.debug("Handling command: {}", command);
                TransformationHandler handler = getTransformationHandler(command);
                if (handler != null) {
                    log.debug("Invoking handler {} for command {}", handler.getClass().getCanonicalName(), command);
                    handler.handle(builder, command);
                } else {
                    log.info("No handler found for: {}", command);
                }
            }
        }
        builder.outputFormat(format.toString());
        builder.toOutputStream(out);
    }

    @Override
    public void transformFile(SlingHttpServletRequest request, OutputStream out) throws IOException {
        OutputFileFormat fileFormat = OutputFileFormat.forRequest(request);
        transformFile(request.getResource().adaptTo(File.class),
                Optional.ofNullable(request.getRequestPathInfo().getSuffix())
                        .map(s -> StringUtils.substringBeforeLast(s, ".")).map(s -> s.split("/")).orElse(new String[0]),
                fileFormat, out);
    }

}
