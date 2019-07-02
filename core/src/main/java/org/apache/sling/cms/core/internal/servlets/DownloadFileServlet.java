package org.apache.sling.cms.core.internal.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

@Component(service = Servlet.class, property = { "sling.servlet.resourceTypes=sling-cms/file/download",
        "sling.servlet.methods=GET", "sling.servlet.extensions=html" })
public class DownloadFileServlet extends SlingSafeMethodsServlet {

    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        handleMethodNotImplemented(request, response);
    }

}
