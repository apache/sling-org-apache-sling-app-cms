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
package org.apache.sling.cms.core.insights.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Simple class for faking a HttpServletRequest
 */
public class FakeRequest implements HttpServletRequest {

    private final Map<String,Object> attributes;

    private final String method;

    private final Map<String,Object> parameters;

    private final String path;

    private final HttpSession session;

    public FakeRequest(String method, String path) {
        this.method = method;
        this.path = path;
        attributes = new HashMap<>();
        parameters = new HashMap<>();
        session = new FakeHttpSession();
    }

    public FakeRequest(String method, String path, Map<String,Object> params) {
        this.method = method;
        this.path = path;
        attributes = new HashMap<>();
        parameters = new HashMap<>();
        session = new FakeHttpSession();
        for (Iterator<String> iterator = params.keySet().iterator(); iterator.hasNext();) {
            String key = iterator.next();
            Object value = params.get(key);
            if (params.get(key) instanceof String[])
                parameters.put(key, (String[]) (String[]) value);
            else
                parameters.put(key, new String[] { value.toString() });
        }

    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public Enumeration<?> getAttributeNames() {
        return Collections.enumeration(attributes.keySet());
    }

    public String getAuthType() {
        return null;
    }

    public String getCharacterEncoding() {
        return "utf-8";
    }

    public int getContentLength() {
        return 0;
    }

    public String getContentType() {
        return null;
    }

    public String getContextPath() {
        return "";
    }

    public Cookie[] getCookies() {
        return new Cookie[0];
    }

    public long getDateHeader(String name) {
        return -1L;
    }

    public String getHeader(String name) {
        return null;
    }

    public Enumeration<?> getHeaderNames() {
        return null;
    }

    public Enumeration<?> getHeaders(String name) {
        return null;
    }

    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    public int getIntHeader(String name) {
        return -1;
    }

    public String getLocalAddr() {
        return null;
    }

    public Locale getLocale() {
        return Locale.getDefault();
    }

    public Enumeration<?> getLocales() {
        return Collections.enumeration(Collections.singleton(Locale.getDefault()));
    }

    public String getLocalName() {
        return null;
    }

    public int getLocalPort() {
        return 0;
    }

    public String getMethod() {
        return method;
    }

    public String getParameter(String name) {
        Object value;
        try {
            value = parameters.get(name);
            if (value instanceof String[]) {
                return ((String[]) value)[0];
            }
        } catch (ClassCastException e) {
            return null;
        }
        return (String) value;
    }

    public Map<String,Object> getParameterMap() {
        return parameters;
    }

    public Enumeration<?> getParameterNames() {
        return Collections.enumeration(parameters.keySet());
    }

    public String[] getParameterValues(String name) {
        throw new UnsupportedOperationException();
    }

    public String getPathInfo() {
        return null;
    }

    public String getPathTranslated() {
        return null;
    }

    public String getProtocol() {
        return "HTTP/1.1";
    }

    public String getQueryString() {
        return null;
    }

    public BufferedReader getReader() throws IOException {
        return null;
    }

    public String getRealPath(String path) {
        return null;
    }

    public String getRemoteAddr() {
        return null;
    }

    public String getRemoteHost() {
        return null;
    }

    public int getRemotePort() {
        return 0;
    }

    public String getRemoteUser() {
        return null;
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }

    public String getRequestedSessionId() {
        return null;
    }

    public String getRequestURI() {
        return path;
    }

    public StringBuffer getRequestURL() {
        return new StringBuffer("http://localhost:8080" + path);
    }

    public String getScheme() {
        return "http";
    }

    public String getServerName() {
        return null;
    }

    public int getServerPort() {
        return 0;
    }

    public String getServletPath() {
        return path;
    }

    public HttpSession getSession() {
        return session;
    }

    public HttpSession getSession(boolean create) {
        return session;
    }

    public Principal getUserPrincipal() {
        return null;
    }

    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    public boolean isRequestedSessionIdValid() {
        return false;
    }

    public boolean isSecure() {
        return false;
    }
    public boolean isUserInRole(String role) {
        return false;
    }
    public void removeAttribute(String name) {
        attributes.remove(name);
    }
    public void setAttribute(String name, Object o) {
        attributes.put(name, o);
    }
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {
        // do nothing
    }
}
