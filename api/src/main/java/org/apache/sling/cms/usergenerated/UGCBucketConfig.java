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
package org.apache.sling.cms.usergenerated;

import org.apache.sling.cms.usergenerated.UserGeneratedContentService.APPROVE_ACTION;
import org.apache.sling.cms.usergenerated.UserGeneratedContentService.CONTENT_TYPE;
import org.osgi.annotation.versioning.ProviderType;

/*
 * Simple POJO for providing the required data for a UGC bucket
 */
@ProviderType
public class UGCBucketConfig {

	private CONTENT_TYPE contentType;
	private int pathDepth = -1;
	private String bucket;
	private APPROVE_ACTION action;

	/**
	 * @return the contentType
	 */
	public CONTENT_TYPE getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(CONTENT_TYPE contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the pathDepth
	 */
	public int getPathDepth() {
		return pathDepth;
	}

	/**
	 * @param pathDepth the pathDepth to set
	 */
	public void setPathDepth(int pathDepth) {
		this.pathDepth = pathDepth;
	}

	/**
	 * @return the bucket
	 */
	public String getBucket() {
		return bucket;
	}

	/**
	 * @param bucket the bucket to set
	 */
	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	/**
	 * @return the action
	 */
	public APPROVE_ACTION getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(APPROVE_ACTION action) {
		this.action = action;
	}
}
