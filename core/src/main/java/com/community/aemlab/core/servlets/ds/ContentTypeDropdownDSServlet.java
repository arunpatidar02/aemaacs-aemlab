package com.community.aemlab.core.servlets.ds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.EmptyDataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.day.cq.commons.jcr.JcrConstants;

/**
 * @author arunpatidar02
 *
 */
@Component(service = Servlet.class, immediate = true, property = {
        Constants.SERVICE_DESCRIPTION + "=Populating Coral Dropdown using datasource servlet",
        "sling.servlet.resourceTypes=aemlab/dialog/granite/components/select/datasource/content/type",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET })
public class ContentTypeDropdownDSServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger Logger = LoggerFactory.getLogger(ContentTypeDropdownDSServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        try {

            // set fallback
            request.setAttribute(DataSource.class.getName(), EmptyDataSource.instance());
            ResourceResolver resolver = request.getResourceResolver();
            // Create an ArrayList to hold data
            List<Resource> fakeResourceList = new ArrayList<>();
            ValueMap vm = null;

            String[] contentType = { "Page", "Asset", "XF"};

            for (int i = 0; i < contentType.length; i++) {
                vm = new ValueMapDecorator(new HashMap<>());
                vm.put("value", contentType[i].trim().toLowerCase());
                vm.put("text", contentType[i].trim());
                fakeResourceList.add(new ValueMapResource(resolver, new ResourceMetadata(), JcrConstants.NT_UNSTRUCTURED, vm));
            }

            // Create a DataSource that is used to populate the drop-down control
            DataSource ds = new SimpleDataSource(fakeResourceList.iterator());
            request.setAttribute(DataSource.class.getName(), ds);
        } catch (Exception e) {
            Logger.error("Error in Getting Drop Down Values - {}", e.getMessage());
        }
    }

}