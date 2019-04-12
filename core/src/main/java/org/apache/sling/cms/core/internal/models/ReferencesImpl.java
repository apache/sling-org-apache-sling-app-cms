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

import java.util.ArrayList;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.cms.Reference;
import org.apache.sling.cms.References;
import org.apache.sling.models.annotations.Model;

/**
 * Model for finding the references to a Resource
 */
@Model(adaptables = Resource.class, adapters = References.class)
public class ReferencesImpl extends ReferenceOperation implements References {

    private List<Reference> referenceList = new ArrayList<>();

    public ReferencesImpl(Resource resource) {
        super(resource);
    }

    @Override
    public List<Reference> getReferences() {
        init();
        return referenceList;
    }

    @Override
    public void doProcess(Resource r, String matchingKey) {
        referenceList.add(new Reference(r, matchingKey));
    }
}
