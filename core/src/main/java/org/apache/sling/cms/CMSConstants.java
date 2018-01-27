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

/**
 * Constants used throughout the application.
 */
public class CMSConstants {

	/**
	 * Private constructor to prevent instantiation of class.
	 */
	private CMSConstants() {
	}

	/**
	 * Content path.
	 */
	public static final String CONTENT_PATH = "/content";

	/**
	 * Node type base.
	 */
	public static final String NAMESPACE = "sling";

	/**
	 * Description attribute name
	 */
	public static final String PN_DESCRIPTION = "jcr:description";

	/**
	 * Title attribute name
	 */
	public static final String PN_TITLE = "jcr:title";

	/**
	 * Site node type.
	 */
	public static final String NT_SITE = NAMESPACE + ":Site";

	/**
	 * Page node type.
	 */
	public static final String NT_PAGE = NAMESPACE + ":Page";

	/**
	 * Component node type.
	 */
	public static final String NT_COMPONENT = NAMESPACE + ":Component";

}