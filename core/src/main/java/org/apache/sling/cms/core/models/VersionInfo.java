/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.cms.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;

import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

/**
 * Returns all of the versions for a Resource. At the moment only JCR nodes are
 * supported.
 */
@Model(adaptables = { Resource.class })
public class VersionInfo {

    private Resource resource;

    public VersionInfo(Resource resource) {
        this.resource = resource;
    }

    @SuppressWarnings("deprecation")
    public List<Version> getVersions() throws RepositoryException {
        List<Version> versions = new ArrayList<>();
        final Node node = resource.adaptTo(Node.class);
        if (node != null && node.isNodeType(JcrConstants.MIX_VERSIONABLE)) {
            final VersionHistory history = node.getVersionHistory();
            for (final VersionIterator it = history.getAllVersions(); it.hasNext();) {
                final Version v = it.nextVersion();
                versions.add(v);
            }
        }
        return versions;
    }
}
