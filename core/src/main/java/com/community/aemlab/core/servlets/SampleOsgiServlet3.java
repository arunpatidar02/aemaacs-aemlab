package com.community.aemlab.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * @author arunpatidar02
 *
 */
@Component(immediate = true, service = Servlet.class, property = {
        ServletResolverConstants.SLING_SERVLET_EXTENSIONS + "=txt",
        "sling.servlet.methods=" + HttpConstants.METHOD_GET })
@Designate(ocd = SampleOsgiServlet3.Configuration.class)
public class SampleOsgiServlet3 extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;

	private boolean enabled;

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {

		PrintWriter out = resp.getWriter();
		resp.setContentType("text/plain");
		if (enabled)
			out.write("Servlet3 is enabled  -- >" + req.getPathInfo());
		else
			out.write("Servlet3 is not enabled");
	}

	@Activate
	@Modified
	protected void activate(Configuration config) {
		enabled = config.enabled();
	}

	@ObjectClassDefinition(name = "Annotation Demo Servlet - OSGi - 3", description = "Sample servlet config 3")
	public @interface Configuration {
		@AttributeDefinition(name = "Enable", description = "Servlet Enabled", type = AttributeType.BOOLEAN)
		boolean enabled() default true;

		@AttributeDefinition(name = "sling.servlet.resourceTypes", description = "Servlet ResourceTypes ", type = AttributeType.STRING)
		String[] sling_servlet_resourceTypes() default { "aemlab/components/text", "aemlab/components/breadcrumb" };


	}
}