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
package org.apache.sling.cms;

import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;

/**
 * Constants used throughout the Sling CMS
 */
public class CMSConstants {

    /**
     * The Request attribute for whether or not editing is enabled
     */
    public static final String ATTR_EDIT_ENABLED = "cmsEditEnabled";

    /**
     * The Component type for pages
     */
    public static final String COMPONENT_TYPE_PAGE = "Page";

    /**
     * Content path.
     */
    public static final String CONTENT_PATH = "/content";

    /**
     * The name of the group for super users
     */
    public static final String GROUP_ADMINISTRATORS = "administrators";

    /**
     * The name of the group for the users who can execute jobs
     */
    public static final String GROUP_JOB_USERS = "job-users";

    /**
     * The bucket for the Insights CA Configs
     */
    public static final String INSIGHTS_CA_CONFIG_BUCKET = "insights";

    /**
     * Node type base.
     */
    public static final String NAMESPACE = "sling";

    /**
     * The subpath for the metadata under sling:File resources
     */
    public static final String NN_METADATA = "metadata";

    /**
     * Component node type.
     */
    public static final String NT_COMPONENT = NAMESPACE + ":Component";

    /**
     * File node type
     */
    public static final String NT_FILE = NAMESPACE + ":File";

    /**
     * Page node type.
     */
    public static final String NT_PAGE = NAMESPACE + ":Page";

    /**
     * Site node type.
     */
    public static final String NT_SITE = NAMESPACE + ":Site";

    /**
     * User Generated Content node type.
     */
    public static final String NT_UGC = NAMESPACE + ":UGC";

    /**
     * Constant for the CA Config Reference
     */
    public static final String PN_CONFIG_REF = CMSConstants.NAMESPACE + ":configRef";

    /**
     * Description attribute name
     */
    public static final String PN_DESCRIPTION = "jcr:description";

    /**
     * i18n Locale property
     */
    public static final String PN_LANGUAGE = "jcr:language";

    /**
     * Constant for the last modified by user
     */
    public static final String PN_LAST_MODIFIED_BY = JcrConstants.JCR_LASTMODIFIED + "By";

    /**
     * Published flag property
     */
    public static final String PN_PUBLISHED = "published";

    /**
     * Constant for the Site URL
     */
    public static final String PN_SITE_URL = CMSConstants.NAMESPACE + ":url";

    /**
     * Taxonomy attribute name
     */
    public static final String PN_TAXONOMY = NAMESPACE + ":taxonomy";

    /**
     * Title attribute name
     */
    public static final String PN_TITLE = "jcr:title";

    /**
     * The resource types which can be published
     */
    public static final String[] PUBLISHABLE_TYPES = new String[] { CMSConstants.NT_FILE, CMSConstants.NT_PAGE,
            JcrResourceConstants.NT_SLING_FOLDER, JcrResourceConstants.NT_SLING_ORDERED_FOLDER };
    
    /**
     * The name of the admin user
     */
    public static final String USER_ADMIN = "admin";

    /**
     * Private constructor to prevent instantiation of class.
     */
    private CMSConstants() {
    }

}