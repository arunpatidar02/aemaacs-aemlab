package com.community.aemlab.oneweb.core.servlets.ds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author arunpatidar02
 *
 */
@Component(service = Servlet.class, immediate = true, property = {
        Constants.SERVICE_DESCRIPTION + "=Get Coral Dropdown options from json",
        ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES + "=" + PopulateTouchUIDropdownFromJSON.DATASOURCE_TYPE,
        ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET })
public class PopulateTouchUIDropdownFromJSON extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(PopulateTouchUIDropdownFromJSON.class);

    static final String DATASOURCE_TYPE = "aemlab/oneweb/dialog/granite/components/select/datasource/json";
    private static final String OPTIONS_PROPERTY = "options";
    private static final String DATASOURCE_NODE = "datasource";
    private static final String TEXT = "text";
    private static final String VALUE = "value";
    private static final String SELECTED = "selected";
    private static final String DISABLED = "disabled";

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        try {
            ResourceResolver resolver = request.getResourceResolver();
            // set fallback
            request.setAttribute(DataSource.class.getName(), EmptyDataSource.instance());

            Resource datasource = request.getResource().getChild(DATASOURCE_NODE);
            String optionJosn = ResourceUtil.getValueMap(datasource).get(OPTIONS_PROPERTY, String.class);

            if (optionJosn != null) {
                Resource jsonResource = resolver.getResource(optionJosn + "/" + JcrConstants.JCR_CONTENT);
                if (!ResourceUtil.isNonExistingResource(jsonResource)) {

                    Gson gson = new Gson();
                    TypeToken<List<Option>> token = new TypeToken<List<Option>>() {
                    };

                    List<Option> optionList = gson.fromJson(getJsonString(jsonResource), token.getType());
                    List<Resource> optionResourceList = new ArrayList<>();

                    Iterator<Option> oi = optionList.iterator();
                    while (oi.hasNext()) {
                        Option opt = oi.next();
                        ValueMap vm = getOptionValueMap(opt);
                        optionResourceList.add(new ValueMapResource(resolver, new ResourceMetadata(),
                                JcrConstants.NT_UNSTRUCTURED, vm));
                    }

                    DataSource ds = new SimpleDataSource(optionResourceList.iterator());
                    request.setAttribute(DataSource.class.getName(), ds);
                }

                else {
                    LOGGER.info("JSON file is not found! ");
                }
            } else {
                LOGGER.info(OPTIONS_PROPERTY + " property is missing in datasource node ");
            }
        } catch (IOException io) {
            LOGGER.error("Error fetching JSON data ");
        } catch (Exception e) {
            LOGGER.error("Error in Getting Drop Down Values ");
        }
    }

    /**
     * @param opt
     * @return ValueMap
     */
    private ValueMap getOptionValueMap(Option opt) {
        ValueMap vm = new ValueMapDecorator(new HashMap<>());

        vm.put(VALUE, opt.getValue());
        vm.put(TEXT, opt.getText());
        if (opt.isSelected()) {
            vm.put(SELECTED, true);
        }
        if (opt.isDisabled()) {
            vm.put(DISABLED, true);
        }
        return vm;
    }

    /**
     * @param jsonResource
     * @return jsonString
     * @throws RepositoryException
     * @throws IOException
     */
    private String getJsonString(Resource jsonResource) throws RepositoryException, IOException {
        Node cfNode = jsonResource.adaptTo(Node.class);
        InputStream in = cfNode.getProperty(JcrConstants.JCR_DATA).getBinary().getStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }

    /**
     * @author arunpatidar02
     *
     */
    private class Option {
        String text;
        String value;
        boolean selected;
        boolean disabled;

        public String getText() {
            return text;
        }

        public String getValue() {
            return value;
        }

        public boolean isSelected() {
            return selected;
        }

        public boolean isDisabled() {
            return disabled;
        }
    }

}