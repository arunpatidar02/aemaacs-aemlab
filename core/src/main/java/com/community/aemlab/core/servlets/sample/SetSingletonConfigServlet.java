package com.community.aemlab.core.servlets.sample;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.community.aemlab.core.utils.AEMLABConstants;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Simple Demo Servlet with GET",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.paths=" + "/bin/admin/set-singleton-config" })
public class SetSingletonConfigServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 2598426539166789516L;
	private static final String PID = "com.community.aemlab.core.schedulers.sample.SimpleScheduledTask";
	private static final String TARGET_PROPERTY_NAME = "myParameter";
	private static final String PROPERTY_VALUE = "myParameter";

	private static final Logger LOGGER = LoggerFactory.getLogger(SetSingletonConfigServlet.class);

	@Reference
	private transient ConfigurationAdmin configAdmin;

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws IOException {
		try {
			Configuration configuration = configAdmin.getConfiguration(PID);

			resp.setContentType(AEMLABConstants.CONTENTTYPE_TXT_HTML);
			if (configuration != null) {
				resp.getWriter().print("<h2>Configurations Found</h2>");
				// get properties
				Dictionary<String, Object> properties = configuration.getProperties();
				// update properties
				/* Set properties */
				if (properties == null) {
					properties = new Hashtable<>();
				}
				properties.put(TARGET_PROPERTY_NAME, PROPERTY_VALUE);
				configuration.update(properties);
				resp.getWriter().print("<h4>Property Updated</h4>");
			}

		} catch (Exception e) {
			LOGGER.error("Not able to update OSGi config", e);
		}
	}
}