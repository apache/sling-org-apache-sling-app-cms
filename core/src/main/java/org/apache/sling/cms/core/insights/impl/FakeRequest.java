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
                parameters.put(key, (String[]) value);
            else
                parameters.put(key, new String[] { value.toString() });
        }

    }

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public Enumeration<?> getAttributeNames() {
        return Collections.enumeration(attributes.keySet());
    }

    @Override
    public String getAuthType() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return "utf-8";
    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public String getContextPath() {
        return "";
    }

    @Override
    public Cookie[] getCookies() {
        return new Cookie[0];
    }

    @Override
    public long getDateHeader(String name) {
        return -1L;
    }

    @Override
    public String getHeader(String name) {
        return null;
    }

    @Override
    public Enumeration<?> getHeaderNames() {
        return null;
    }

    @Override
    public Enumeration<?> getHeaders(String name) {
        return null;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public int getIntHeader(String name) {
        return -1;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public Locale getLocale() {
        return Locale.getDefault();
    }

    @Override
    public Enumeration<?> getLocales() {
        return Collections.enumeration(Collections.singleton(Locale.getDefault()));
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
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

    @Override
    public Map<String,Object> getParameterMap() {
        return parameters;
    }

    @Override
    public Enumeration<?> getParameterNames() {
        return Collections.enumeration(parameters.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPathInfo() {
        return null;
    }

    @Override
    public String getPathTranslated() {
        return null;
    }

    @Override
    public String getProtocol() {
        return "HTTP/1.1";
    }

    @Override
    public String getQueryString() {
        return null;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRealPath(String path) {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        return null;
    }

    @Override
    public String getRequestURI() {
        return path;
    }

    @Override
    public StringBuffer getRequestURL() {
        return new StringBuffer("http://localhost:8080" + path);
    }

    @Override
    public String getScheme() {
        return "http";
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public String getServletPath() {
        return this.getRequestURI();
    }

    @Override
    public HttpSession getSession() {
        return session;
    }

    @Override
    public HttpSession getSession(boolean create) {
        return session;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    @Override
    public boolean isSecure() {
        return false;
    }
    @Override
    public boolean isUserInRole(String role) {
        return false;
    }
    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }
    @Override
    public void setAttribute(String name, Object o) {
        attributes.put(name, o);
    }
    @Override
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {
        // do nothing
    }
}
