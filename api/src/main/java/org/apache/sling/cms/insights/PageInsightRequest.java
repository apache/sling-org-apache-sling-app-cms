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
package org.apache.sling.cms.insights;

import java.io.IOException;

import org.apache.sling.cms.Page;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Represents an insight request
 */
public interface PageInsightRequest extends InsightRequest {

    /**
     * Gets the page backing this request
     * 
     * @return the page
     */
    public Page getPage();

    /**
     * Gets a JSoup Element of the body of the page, not the full page (e.g. no
     * header).
     * 
     * @return the HTML of the body
     * @throws IOException an exception occurs retrieving the content
     */
    public Element getPageBodyElement() throws IOException;

    /**
     * Gets the HTML of the body of the page, not the full page (e.g. no header).
     * 
     * @return the HTML of the body
     * @throws IOException an exception occurs retrieving the content
     */
    public String getPageBodyHtml() throws IOException;

    /**
     * Gets the page HTML as a JSoup Document.
     * 
     * @return the page HTML
     * @throws IOException an exception occurs retrieving the content
     */
    public Document getPageDocument() throws IOException;

    /**
     * Gets the page HTML. Loads the HTML lazily so the initial request may take a
     * bit but others will be quick.
     * 
     * @return a string representation of the HTML for a page
     * @throws IOException an exception occurs retrieving the content
     */
    public String getPageHtml() throws IOException;

    public default TYPE getType() {
        return TYPE.PAGE;
    }
}
