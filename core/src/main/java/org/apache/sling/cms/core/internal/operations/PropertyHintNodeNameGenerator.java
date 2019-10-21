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

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.request.RequestParameterMap;
import org.apache.sling.cms.NameFilter;
import org.apache.sling.cms.core.internal.operations.PropertyHintNodeNameGenerator.Config;
import org.apache.sling.servlets.post.NodeNameGenerator;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Custom NodeNameGenerator for generating names based on the value of another
 * named property
 */
@Component(service = { NodeNameGenerator.class, NameFilter.class }, immediate = true)
@Designate(ocd = Config.class)
public class PropertyHintNodeNameGenerator implements NodeNameGenerator, NameFilter {

    @ObjectClassDefinition(name = "%cms.name.generator.name", description = "%cms.name.generator.description", localization = "OSGI-INF/l10n/bundle")

    public @interface Config {

        @SuppressWarnings("squid:S00100") // ignore we have to support the old property format
        @AttributeDefinition(name = "%allowed.chars.name", description = "%allowed.chars.description")
        String allowed_chars() default "abcdefghijklmnopqrstuvwxyz0123456789_-";

        @SuppressWarnings("squid:S00100") // ignore we have to support the old property format
        @AttributeDefinition(name = "%replacement.char.name", description = "%replacement.char.description")
        String replacement_char() default "-";
    }

    /**
     * Optional request parameter specifying a parameter name to use for the name of
     * the newly created node (value is ":nameParam").
     */
    public static final String RP_NODE_NAME_PARAM = ":nameParam";

    private String allowedChars;

    private char replacementChar;

    @Activate
    @Modified
    public void activate(Config config) {
        this.allowedChars = config.allowed_chars();
        this.replacementChar = config.replacement_char().toCharArray()[0];
    }

    @Override
    public String filter(String nodeName) {
        final StringBuilder sb = new StringBuilder();
        char lastAdded = 0;

        nodeName = nodeName.toLowerCase();
        for (int i = 0; i < nodeName.length(); i++) {
            final char c = nodeName.charAt(i);
            char toAdd = c;

            if (allowedChars.indexOf(c) < 0) {
                if (lastAdded == replacementChar) {
                    // do not add several _ in a row
                    continue;
                }
                toAdd = replacementChar;

            } else if (i == 0 && Character.isDigit(c)) {
                sb.append(replacementChar);
            }

            sb.append(toAdd);
            lastAdded = toAdd;
        }

        if (sb.length() == 0) {
            sb.append(replacementChar);
        }

        return sb.toString();
    }

    @Override
    public String getNodeName(SlingHttpServletRequest request, String parentPath, boolean requirePrefix,
            NodeNameGenerator defaultNodeNameGenerator) {
        RequestParameterMap parameters = request.getRequestParameterMap();
        String name = null;

        // If the :nameParam parameter is specified use that parameter to generate the
        // name
        RequestParameter nameParam = null;
        RequestParameter paramName = parameters.getValue(RP_NODE_NAME_PARAM);
        if (paramName != null && StringUtils.isNotBlank(paramName.getString())) {
            nameParam = parameters.getValue(paramName.getString());
        }
        if (nameParam != null && StringUtils.isNotBlank(nameParam.getString())) {
            name = filter(nameParam.getString());
        }

        return name;
    }

}
