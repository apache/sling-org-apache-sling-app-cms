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
package org.apache.sling.cms.core.models;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;

/**
 * Allows passing information between redirects
 */
@Model(adaptables = SlingHttpServletRequest.class)
public class RedirectAttribute {

	public enum LEVEL {
		danger, info, success, warning
	}

	private static final String LEVEL_SESSION_KEY = "REDIRECT_LEVEL";

	private static final String MESSAGE_SESSION_KEY = "REDIRECT_MESSAGE";

	public static void setMessage(HttpServletRequest request, LEVEL level, String message) {
		setMessage(request.getSession(), level, message);
	}

	public static void setMessage(HttpSession session, LEVEL level, String message) {
		session.setAttribute(MESSAGE_SESSION_KEY, message);
		session.setAttribute(LEVEL_SESSION_KEY, level.toString());
	}

	private SlingHttpServletRequest request;

	public RedirectAttribute(SlingHttpServletRequest request) {
		this.request = request;
	}

	public String getLevel() {
		String level = (String) request.getSession().getAttribute(LEVEL_SESSION_KEY);
		if (level == null) {
			level = LEVEL.info.toString();
		}
		request.getSession().removeAttribute(LEVEL_SESSION_KEY);
		return level;
	}

	public String getMessageText() {
		String message = (String) request.getSession().getAttribute(MESSAGE_SESSION_KEY);
		request.getSession().removeAttribute(MESSAGE_SESSION_KEY);
		return message;
	}

	public boolean isMessageSet() {
		return request.getSession().getAttribute(MESSAGE_SESSION_KEY) != null;
	}
}
