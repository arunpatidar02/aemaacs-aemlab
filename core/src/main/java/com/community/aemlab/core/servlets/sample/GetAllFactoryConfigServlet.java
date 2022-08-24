package com.community.aemlab.core.servlets.sample;

import java.io.IOException;

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

/**
 * @author arunpatidar02
 *
 */
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Simple Demo Servlet with GET",
		"sling.servlet.methods=" + HttpConstants.METHOD_POST,
		"sling.servlet.paths=" + "/bin/admin/get-factory-config" })
public class GetAllFactoryConfigServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 2598426539166789516L;
	private static final String FILTER = "(service.factoryPid=org.apache.sling.commons.log.LogManager.factory.config)";
	private static final Logger LOGGER = LoggerFactory.getLogger(GetAllFactoryConfigServlet.class);

	@Reference
	private transient ConfigurationAdmin configAdmin;

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws IOException {
		
		resp.setContentType(AEMLABConstants.CONTENTTYPE_TXT_HTML);
		
		try {
			Configuration[] configurations = configAdmin.listConfigurations(FILTER);

			String headStr = "<!DOCTYPE html> <html> <head> <title>Factory Configuration Items</title> <style> #feature {font-family: 'Trebuchet MS', Arial, Helvetica, sans-serif; border-collapse: collapse; width: 100%; } #feature td, #feature th {border: 1px solid #ddd; padding: 8px; } #feature tr:nth-child(even){background-color: #f2f2f2;} #feature tr:hover {background-color: #ddd;} #feature th {padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #4CAF50; color: white; } </style> </head> <body>";
			String footStr = " </body> </html>";

			StringBuilder main = new StringBuilder(
					"<h2>Number of Configurations Found  - " + configurations.length + "</h2>");
			main.append("<table id='feature'>");
			main.append("<tr><th>S.No.</th><th>Pid</th></tr>");
			for (int i = 0; i < configurations.length; i++) {
				main.append("<tr><td>" + (i + 1) + "</td><td>" + configurations[i].getPid() + "</td></tr>");
			}
			main.append("</table>");

			resp.getWriter().print(headStr + main + footStr);

		} catch (Exception e) {
			LOGGER.error("Error while getting all factory configuration", e);
		}
	}
}