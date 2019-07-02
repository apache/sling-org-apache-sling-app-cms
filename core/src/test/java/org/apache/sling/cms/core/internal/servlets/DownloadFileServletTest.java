package org.apache.sling.cms.core.internal.servlets;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.sling.cms.core.helpers.SlingCMSContextHelper;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class DownloadFileServletTest {

    @Rule
    public final SlingContext context = new SlingContext();

    @Before
    public void init() {
        SlingCMSContextHelper.initContext(context);

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
