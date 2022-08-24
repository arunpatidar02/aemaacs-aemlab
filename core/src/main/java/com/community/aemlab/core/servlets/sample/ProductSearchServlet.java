package com.community.aemlab.core.servlets.sample;

import java.io.IOException;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author arunpatidar02
 *
 */
@Component(service = { Servlet.class }, immediate = true)
@SlingServletResourceTypes(resourceTypes = "aemlab/utils/product/search", methods = HttpConstants.METHOD_GET, selectors = "product-search", extensions = "html")
@ServiceDescription("Product Search Servlet")
public class ProductSearchServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductSearchServlet.class);

    @Activate
    public void activate() {
        LOGGER.trace("ProductSearchServlet.class Activated");
    }

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        LOGGER.trace("Start");
        response.getWriter().write("Hello");
        LOGGER.trace("Ends");
        response.setContentType("text/html; charset=UTF-8");
    }

}