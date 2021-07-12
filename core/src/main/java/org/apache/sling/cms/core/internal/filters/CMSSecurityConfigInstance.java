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
package org.apache.sling.cms.core.internal.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

@Component(service = { CMSSecurityConfigInstance.class })
@Designate(ocd = CMSSecurityFilterConfig.class, factory = true)
public class CMSSecurityConfigInstance {

    private CMSSecurityFilterConfig config;
    private final List<Pattern> patterns = new ArrayList<>();

    @Modified
    @Activate
    public void activate(CMSSecurityFilterConfig config) {
        this.config = config;
        if (config.allowedPatterns() != null) {
            for (String p : config.allowedPatterns()) {
                patterns.add(Pattern.compile(p));
            }
        }

    }

    private boolean domainsSet() {
        if (ArrayUtils.isEmpty(config.hostDomains())) {
            return false;
        }
        for (String value : config.hostDomains()) {
            if (StringUtils.isNotEmpty(value)) {
                return true;
            }
        }
        return false;
    }

    public boolean applies(HttpServletRequest request) {
        return !domainsSet() || ArrayUtils.contains(config.hostDomains(), request.getServerName());
    }

    public String getGroupName() {
        return config.group();
    }

    public boolean isUriAllowed(String uri) {
        for (Pattern p : patterns) {
            if (p.matcher(uri).matches()) {
                return true;
            }
        }
        return false;
    }

}