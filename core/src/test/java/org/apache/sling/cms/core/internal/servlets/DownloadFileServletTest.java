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
package org.apache.sling.cms.core.internal.servlets;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.sling.cms.core.helpers.SlingCMSTestHelper;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class DownloadFileServletTest {

    @Rule
    public final SlingContext context = new SlingContext();

    @Before
    public void init() {
        SlingCMSTestHelper.initContext(context);

    }

    @Test
    public void testValidRequest() throws ServletException, IOException {

        context.requestPathInfo().setSuffix("/content/apache/sling-apache-org/index/apache.png");

        DownloadFileServlet dfs = new DownloadFileServlet();

        dfs.doGet(context.request(), context.response());
        
        assertTrue(context.response().getStatus() == 200);
        assertTrue(context.response().getHeaderNames().contains("Content-Disposition"));
    }
    

    @Test
    public void testInValidRequest() throws ServletException, IOException {

        context.requestPathInfo().setSuffix("/content/apache/sling-apache-org/index/notafile.png");

        DownloadFileServlet dfs = new DownloadFileServlet();

        dfs.doGet(context.request(), context.response());
        
        assertTrue(context.response().getStatus() == 404);
        assertFalse(context.response().getHeaderNames().contains("Content-Disposition"));
    }
}
