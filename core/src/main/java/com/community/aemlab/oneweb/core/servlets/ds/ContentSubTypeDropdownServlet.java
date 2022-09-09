package com.community.aemlab.oneweb.core.servlets.ds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * @author arunpatidar02
 *
 */
@Component(service = Servlet.class, immediate = true, property = {
        Constants.SERVICE_DESCRIPTION + "=Populating Coral Dropdown using servlet",
        "sling.servlet.resourceTypes=aemlab/oneweb/dialog/granite/components/select/datasource/content/subtype",
        ServletResolverConstants.SLING_SERVLET_EXTENSIONS + "=json",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET })
public class ContentSubTypeDropdownServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger Logger = LoggerFactory.getLogger(ContentSubTypeDropdownServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        Gson gson = new Gson();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        List<String> subtypeList = new ArrayList<>();
        try {
            Object qs = request.getParameter("type");

            if (qs != null) {
                switch (qs.toString()) {
                case "page":
                    subtypeList.add("page");
                    subtypeList.add("conf");
                    subtypeList.add("template");
                    break;
                case "xf":
                    subtypeList.add("page");
                    subtypeList.add("xf");
                    subtypeList.add("conf");
                    subtypeList.add("template");
                    subtypeList.add("target");
                    break;
                case "asset":
                    subtypeList.add("metadata");
                    subtypeList.add("asset");
                    subtypeList.add("rendition");
                    break;
                default:
                    subtypeList.add("aem");
                }
            }

        } catch (Exception e) {
            Logger.error("Error in Setting Dropdown Values - {}", e.getMessage());
        }

        String jsonString = gson.toJson(subtypeList);
        response.getWriter().write(jsonString);
    }

}