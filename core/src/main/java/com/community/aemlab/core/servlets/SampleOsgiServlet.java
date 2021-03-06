package com.community.aemlab.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.community.aemlab.core.services.SampleOsgiService;

@Component(immediate = true, service = Servlet.class, property = { "sling.servlet.extensions=txt",
		"sling.servlet.paths=/bin/osgi", "sling.servlet.paths=/bin/foo", "sling.servlet.methods=get" })
@Designate(ocd = SampleOsgiServlet.Configuration.class)
public class SampleOsgiServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;
	
	private static int counter = 0; 

	private static final Logger LOG = LoggerFactory.getLogger(SampleOsgiServlet.class);

	@Reference
	private SampleOsgiService sampleOsgiService;

	private boolean enabled;

	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {

		PrintWriter out = resp.getWriter();
		LOG.info("sampleOsgiService STARTED......");
		resp.setContentType("text/plain");
		out.write("Annotation Demo Servlet - OSGi - enabled: " + enabled + "\n");
		out.write(sampleOsgiService.getSettings());
		LOG.info("sampleOsgiService END......{}"+ counter++);
	}

	@Activate
	@Modified
	protected void Activate(Configuration config) {
		enabled = config.enabled();
	}

	@ObjectClassDefinition(name = "Annotation Demo Servlet - OSGi", description = "Sample servlet config")
	public @interface Configuration {
		@AttributeDefinition(name = "Enable", description = "Sample boolean property", type = AttributeType.BOOLEAN)
		boolean enabled() default false;

	}
}