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
package org.apache.sling.cms.core.internal.operations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.sling.api.SlingException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.servlets.post.Modification;
import org.apache.sling.servlets.post.PostOperation;
import org.apache.sling.servlets.post.PostResponse;
import org.apache.sling.servlets.post.SlingPostProcessor;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The <code>BulkReplaceOperation</code> class will update all of the properties
 * applicable to the supplied parameters under the resource for the operation,
 * replacing the find string with the replacement value.
 */
@Component(immediate = true, service = { PostOperation.class }, property = PostOperation.PROP_OPERATION_NAME
        + "=bulkreplace")
public class BulkReplaceOperation implements PostOperation {

    private static final Logger log = LoggerFactory.getLogger(BulkReplaceOperation.class);

    public static final String PN_UPDATE_PROPERTIES = "updateProperties";
    public static final String MODE_REGEX = "regex";
    public static final String PN_FIND = "find";
    public static final String PN_REPLACE = "replace";
    public static final String PN_MODE = "mode";

    @SuppressWarnings("javasecurity:S2631") // ignoring warning since this servlet can only be executed by privileged users
    @Override
    public void run(SlingHttpServletRequest request, PostResponse response, SlingPostProcessor[] processors) {

        try {
            // calculate the paths
            String path = request.getResource().getPath();
            response.setPath(path);

            // perform the bulk replacement
            Pattern updateProperties = Pattern.compile(request.getParameter(PN_UPDATE_PROPERTIES));
            if (log.isDebugEnabled()) {
                log.debug("Updating properties matching: {}", updateProperties.pattern());
            }
            Pattern rfind = null;
            String find = request.getParameter(PN_FIND);
            if (MODE_REGEX.equals(request.getParameter(PN_MODE))) {
                log.debug("Using regular expressions to search for {}", find);
                
                rfind = Pattern.compile(find);
            } else {
                log.debug("Searching for {}", find);
            }
            String replace = request.getParameter(PN_REPLACE);
            log.debug("Replacing with {}", replace);

            final List<Modification> changes = new ArrayList<>();
            updateProperties(request.getResource(), updateProperties, rfind, find, replace, response, changes);

            // invoke processors
            if (processors != null) {
                for (SlingPostProcessor processor : processors) {
                    processor.process(request, changes);
                }
            }

            // check modifications for remaining postfix and store the base path
            final Map<String, String> modificationSourcesContainingPostfix = new HashMap<>();
            final Set<String> allModificationSources = new HashSet<>(changes.size());
            for (final Modification modification : changes) {
                final String source = modification.getSource();
                if (source != null) {
                    allModificationSources.add(source);
                    final int atIndex = source.indexOf('@');
                    if (atIndex > 0) {
                        modificationSourcesContainingPostfix.put(source.substring(0, atIndex), source);
                    }
                }
            }
            request.getResourceResolver().commit();

        } catch (Exception e) {

            log.error("Exception during response processing.", e);
            response.setError(e);

        }
    }

    private void updateProperties(Resource resource, Pattern updateProperties, Pattern rfind, String find,
            String replace, PostResponse response, List<Modification> changes) {
        ModifiableValueMap properties = resource.adaptTo(ModifiableValueMap.class);
        boolean updated = false;

        if (properties == null) {
            throw new SlingException("Failed to retrieve modifiable value map, cannot update properties", null);
        }

        for (Entry<String, Object> entry : properties.entrySet()) {

            if (updateProperties.matcher(entry.getKey()).matches()) {
                updated = updateProperty(resource, rfind, find, replace, properties, updated, entry);
            }
        }
        if (updated) {
            response.onModified(resource.getPath());
            changes.add(Modification.onModified(resource.getPath()));
        }
        for (Resource child : resource.getChildren()) {
            updateProperties(child, updateProperties, rfind, find, replace, response, changes);
        }
    }

    @SuppressWarnings("unlikely-arg-type")
    private boolean updateProperty(Resource resource, Pattern rfind, String find, String replace,
            ModifiableValueMap properties, boolean updated, Entry<String, Object> entry) {
        log.trace("Checking property {}@{}", resource.getPath(), entry.getKey());
        if (properties.get(entry.getKey()) instanceof String) {
            String value = (String) entry.getValue();
            if (rfind == null && (value.contains(find) || value.equals(find))) {
                value = value.replace(find, replace);
                log.trace("Value after replacement: {}", value);
                properties.put(entry.getKey(), value);
                updated = true;
            } else if (rfind != null) {
                Matcher m = rfind.matcher(value);
                if (m.find()) {
                    value = rfind.matcher(value).replaceAll(replace);
                    log.trace("Value after replacement: {}", value);
                    properties.put(entry.getKey(), value);
                    updated = true;
                }
            }
        } else if (properties.get(entry) instanceof String[]) {
            log.trace("Found array value");
            boolean arrUpdated = false;
            String[] v = (String[]) entry.getValue();
            for (int i = 0; i < v.length; i++) {
                String value = v[i];
                if (rfind == null && (value.contains(find) || value.equals(find))) {
                    v[i] = value.replace(find, replace);
                    arrUpdated = true;
                } else if (rfind != null) {
                    Matcher m = rfind.matcher(value);
                    if (m.find()) {
                        v[i] = rfind.matcher(value).replaceAll(replace);
                        arrUpdated = true;
                    }
                }
            }
            if (arrUpdated) {
                if (log.isTraceEnabled()) {
                    log.trace("Value after replacement: {}", Arrays.toString(v));
                }
                properties.put(entry.getKey(), v);
                updated = true;
            }
        }
        return updated;
    }
}
