package com.community.aemlab.core.servlets.sample;

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

import com.community.aemlab.core.utils.AEMLABConstants;

/**
 * @author arunpatidar02
 *
 */
@Component(immediate = true, service = Servlet.class)
@Designate(ocd = SampleOsgiServlet2.Configuration.class)
public class SampleOsgiServlet2 extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;

	private boolean enabled;

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {

		PrintWriter out = resp.getWriter();
		resp.setContentType(AEMLABConstants.CONTENTTYPE_TXT_HTML);
		
		if (enabled)
			out.write(req.getPathInfo());
		else
			out.write("Servlet is not enabled");
	}

	@Activate
	@Modified
	protected void activate(Configuration config) {
		enabled = config.enabled();
	}

	@ObjectClassDefinition(name = "Annotation Demo Servlet - OSGi - 2", description = "Sample servlet config 2")
	public @interface Configuration {
		@AttributeDefinition(name = "Enable", description = "Servlet Enabled", type = AttributeType.BOOLEAN)
		boolean enabled() default true;

		@AttributeDefinition(name = "sling.servlet.resourceTypes", description = "Servlet ResourceTypes ", type = AttributeType.STRING)
		String[] sling_servlet_resourceTypes() default { "aemlab/components/title", "aemlab/components/image" };

		@AttributeDefinition(name = ServletResolverConstants.SLING_SERVLET_EXTENSIONS, description = "Servlet Extension", type = AttributeType.STRING)
		String[] sling_servlet_extensions() default { "txt" };

		@AttributeDefinition(name = "Methods", description = "Servlet Methods", type = AttributeType.STRING)
		String[] sling_servlet_methods() default HttpConstants.METHOD_GET;

	}
}