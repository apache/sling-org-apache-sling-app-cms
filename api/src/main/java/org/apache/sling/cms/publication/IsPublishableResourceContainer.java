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
package org.apache.sling.cms.publication;

import java.util.Optional;
import java.util.function.Predicate;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.CMSConstants;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;

/**
 * A predicate for evaluating if a resource can contain publishable resources or
 * be a publishable resource itself
 */
public class IsPublishableResourceContainer implements Predicate<Resource> {

    @Override
    public boolean test(Resource r) {
        return Optional.ofNullable(r).map(Resource::getResourceType)
                .map(rt -> JcrResourceConstants.NT_SLING_FOLDER.equals(rt)
                        || JcrResourceConstants.NT_SLING_ORDERED_FOLDER.equals(rt) || CMSConstants.NT_FILE.equals(rt)
                        || CMSConstants.NT_SITE.equals(rt) || CMSConstants.NT_PAGE.equals(rt))
                .orElse(false);
    }
}