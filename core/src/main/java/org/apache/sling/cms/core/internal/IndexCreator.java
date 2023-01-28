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
package org.apache.sling.cms.core.internal;

import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.oak.api.Type;
import org.apache.jackrabbit.oak.plugins.index.IndexConstants;
import org.apache.jackrabbit.oak.plugins.index.lucene.util.IndexDefinitionBuilder;
import org.apache.jackrabbit.oak.plugins.index.lucene.util.IndexDefinitionBuilder.IndexRule;
import org.apache.jackrabbit.oak.spi.lifecycle.RepositoryInitializer;
import org.apache.jackrabbit.oak.spi.state.NodeBuilder;
import org.apache.sling.cms.CMSConstants;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Component;

@SuppressWarnings("deprecation") // The non-deprecated versions reference a non-exported class
@Component(service = RepositoryInitializer.class)
public class IndexCreator implements RepositoryInitializer {

    private static final String VAL_NODE_NAME = ":nodeName";
    private static final String PN_NODE_NAME = "nodeName";
    private static final String PN_JCR_TITLE = "jcrTitle";
    private static final String[] CONTENT_PATHS = new String[] { "/content", "/static" };
    private static final String JCR_CONTENT_PROPERTIES = "jcr:content/*";
    private static final String SLINGCMS = "slingcms";

    @Override
    public void initialize(@NotNull NodeBuilder builder) {
        NodeBuilder indexRoot = builder.child(IndexConstants.INDEX_DEFINITIONS_NAME);
        ensureFolderIndex(indexRoot);
        ensurentHierarchyNodeIndex(indexRoot);
        ensureSlingComponentIndex(indexRoot);
        ensureSlingEventJobIndex(indexRoot);
        ensureSlingFileIndex(indexRoot);
        ensureSlingPageIndex(indexRoot);
        ensureSlingTaxonomyIndex(indexRoot);
    }

    private void ensureFolderIndex(NodeBuilder indexRoot) {
        NodeBuilder index = ensureNode(indexRoot, "ntFolder", IndexConstants.INDEX_DEFINITIONS_NODE_TYPE);

        IndexDefinitionBuilder builder = new IndexDefinitionBuilder(index, true);
        builder.aggregateRule(JcrConstants.NT_FOLDER, JCR_CONTENT_PROPERTIES);
        builder.async(IndexConstants.ASYNC_PROPERTY_NAME, IndexConstants.INDEXING_MODE_NRT);
        builder.tags(SLINGCMS, "slingcms-ntFolder");

        IndexRule indexRule = builder.indexRule(JcrConstants.NT_FOLDER);
        ensureCommonSlingProperties(indexRule);
    }

    private void ensurentHierarchyNodeIndex(NodeBuilder indexRoot) {
        NodeBuilder index = ensureNode(indexRoot, "ntHierarchyNode", IndexConstants.INDEX_DEFINITIONS_NODE_TYPE);

        IndexDefinitionBuilder builder = new IndexDefinitionBuilder(index, true);
        builder.aggregateRule(CMSConstants.NT_PAGE, JCR_CONTENT_PROPERTIES);
        builder.async(IndexConstants.ASYNC_PROPERTY_NAME, IndexConstants.INDEXING_MODE_NRT);
        builder.evaluatePathRestrictions();
        builder.includedPaths(CONTENT_PATHS);
        builder.tags(SLINGCMS, "slingcms-ntHierarchyNode");

        IndexRule indexRule = builder.indexRule(JcrConstants.NT_HIERARCHYNODE);
        ensureCommonSlingProperties(indexRule);
    }

    private void ensureSlingComponentIndex(NodeBuilder indexRoot) {
        NodeBuilder index = ensureNode(indexRoot, "slingComponent", IndexConstants.INDEX_DEFINITIONS_NODE_TYPE);

        IndexDefinitionBuilder builder = new IndexDefinitionBuilder(index, true);
        builder.async(IndexConstants.ASYNC_PROPERTY_NAME, IndexConstants.INDEXING_MODE_NRT);
        builder.evaluatePathRestrictions();
        builder.includedPaths("/apps", "/libs");
        builder.tags(SLINGCMS, "slingcms-slingComponent");

        IndexRule indexRule = builder.indexRule("sling:Component");
        indexRule.property("componentType", "componentType", false).propertyIndex().notNullCheckEnabled();
        indexRule.property(PN_JCR_TITLE, "jcr:title", false).analyzed().propertyIndex().ordered();
        indexRule.property(PN_NODE_NAME, VAL_NODE_NAME, false).analyzed().propertyIndex().ordered();

        indexRule.property("jcrLastModifiedBy", "jcr:content/jcr:lastModifiedBy", false).propertyIndex();
    }

    private void ensureSlingEventJobIndex(NodeBuilder indexRoot) {
        NodeBuilder index = ensureNode(indexRoot, "slingeventJob", IndexConstants.INDEX_DEFINITIONS_NODE_TYPE);

        IndexDefinitionBuilder builder = new IndexDefinitionBuilder(index, true);
        builder.async(IndexConstants.ASYNC_PROPERTY_NAME, IndexConstants.INDEXING_MODE_NRT);
        builder.evaluatePathRestrictions();
        builder.includedPaths("/var/eventing");
        builder.tags(SLINGCMS, "slingcms-slingeventJob");

        IndexRule indexRule = builder.indexRule("slingevent:Job");
        indexRule.property("eventJobTopic", "event.job.topic", false).propertyIndex().analyzed();
        indexRule.property("slingeventCreated", "slingevent:created", false).propertyIndex().ordered().type("Date");
        indexRule.property("slingeventId", "slingevent:eventId", false).propertyIndex();
        indexRule.property("slingeventFinishedState", "slingevent:finishedState", false).nullCheckEnabled()
                .propertyIndex();
        indexRule.property("finishedDate", "slingevent:finishedDate", false).propertyIndex().ordered().type("Date");
    }

    private void ensureSlingFileIndex(NodeBuilder indexRoot) {
        NodeBuilder index = ensureNode(indexRoot, "slingFile", IndexConstants.INDEX_DEFINITIONS_NODE_TYPE);

        IndexDefinitionBuilder builder = new IndexDefinitionBuilder(index, true);
        builder.aggregateRule(CMSConstants.NT_FILE, JCR_CONTENT_PROPERTIES, "jcr:content/metadata/*");
        builder.async(IndexConstants.ASYNC_PROPERTY_NAME, IndexConstants.INDEXING_MODE_NRT);
        builder.evaluatePathRestrictions();
        builder.includedPaths(CONTENT_PATHS);
        builder.supersedes("/oak:index/slingFileLucene");
        builder.tags(SLINGCMS, "slingcms-slingFile");

        IndexRule indexRule = builder.indexRule(CMSConstants.NT_FILE);
        ensureCommonSlingProperties(indexRule);
        indexRule.property("metadata", "jcr:content/metadata/.*", true).propertyIndex().analyzed();
    }

    private void ensureSlingPageIndex(NodeBuilder indexRoot) {
        NodeBuilder index = ensureNode(indexRoot, "slingPage", IndexConstants.INDEX_DEFINITIONS_NODE_TYPE);

        IndexDefinitionBuilder builder = new IndexDefinitionBuilder(index, true);
        builder.aggregateRule(CMSConstants.NT_PAGE, JCR_CONTENT_PROPERTIES, "jcr:content/*/*",
                "jcr:content/*/*/*", "jcr:content/*/*/*/*");
        builder.async(IndexConstants.ASYNC_PROPERTY_NAME, IndexConstants.INDEXING_MODE_NRT);
        builder.evaluatePathRestrictions();
        builder.includedPaths(CONTENT_PATHS);
        builder.supersedes("/oak:index/slingPageLucene");
        builder.tags(SLINGCMS, "slingcms-slingPage");

        IndexRule indexRule = builder.indexRule(CMSConstants.NT_PAGE);
        ensureCommonSlingProperties(indexRule);
        indexRule.property("slingTemplate", "jcr:content/sling:template", false).propertyIndex();
    }

    private void ensureSlingTaxonomyIndex(NodeBuilder indexRoot) {
        NodeBuilder index = ensureNode(indexRoot, "slingTaxonomy", IndexConstants.INDEX_DEFINITIONS_NODE_TYPE);

        IndexDefinitionBuilder builder = new IndexDefinitionBuilder(index, true);
        builder.async(IndexConstants.ASYNC_PROPERTY_NAME, IndexConstants.INDEXING_MODE_NRT);
        builder.evaluatePathRestrictions();
        builder.includedPaths("/etc/taxonomy");
        builder.tags(SLINGCMS, "slingcms-slingTaxonomy");

        IndexRule indexRule = builder.indexRule("sling:Taxonomy");
        indexRule.property(PN_JCR_TITLE, "jcr:title", false).analyzed().propertyIndex().ordered();
        indexRule.property(PN_NODE_NAME, VAL_NODE_NAME, false).analyzed().propertyIndex().ordered();
    }

    private void ensureCommonSlingProperties(IndexRule indexRule) {
        indexRule.property(PN_JCR_TITLE, "jcr:content/jcr:title", false).analyzed().propertyIndex().boost(2).ordered();
        indexRule.property(PN_NODE_NAME, VAL_NODE_NAME, false).analyzed().propertyIndex().ordered();
        indexRule.property("jcrDescription", "jcr:content/jcr:description", false).analyzed().propertyIndex();
        indexRule.property("jcrLastModified", "jcr:content/jcr:lastModified", false).propertyIndex().ordered()
                .type("Date");
        indexRule.property("jcrLastModifiedBy", "jcr:content/jcr:lastModifiedBy", false).propertyIndex();
        indexRule.property("slingPublished", "jcr:content/sling:published", false).propertyIndex().type("Boolean");
        indexRule.property("slingTaxonomy", "jcr:content/sling:taxonomy", false).propertyIndex().analyzed();
        indexRule.property("allProperties", "jcr:content/.*", true).propertyIndex().analyzed();
    }

    private NodeBuilder ensureNode(NodeBuilder parent, String childName, String type) {
        NodeBuilder child = parent.child(childName);
        ensureProperty(child, JcrConstants.JCR_PRIMARYTYPE, type, Type.NAME);
        return child;
    }

    private <T> void ensureProperty(NodeBuilder node, String propertyName, T value, Type<T> type) {
        node.setProperty(propertyName, value, type);
    }

}
