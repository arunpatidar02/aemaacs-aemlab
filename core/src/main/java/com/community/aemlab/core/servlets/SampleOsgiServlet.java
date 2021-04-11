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

import com.community.aemlab.core.services.SampleOsgiService;

@Component(immediate = true, service = Servlet.class, property = { "sling.servlet.extensions=txt",
		"sling.servlet.paths=/bin/osgi", "sling.servlet.paths=/bin/foo",
		"sling.servlet.methods=get" })
@Designate(ocd = SampleOsgiServlet.Configuration.class)
public class SampleOsgiServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;

	@Reference
	private SampleOsgiService sampleOsgiService;

	private boolean enabled;

	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {

		PrintWriter out = resp.getWriter();

		resp.setContentType("text/plain");
		out.write("Annotation Demo Servlet - OSGi - enabled: " + enabled + "\n");
		out.write(sampleOsgiService.getSettings());
	}
	@Activate
	@Modified
	protected void Activate(Configuration config) {
		enabled = config.enabled();
	}

	@ObjectClassDefinition(name = "Annotation Demo Servlet - OSGi", description = "Sample servlet config")
	public @interface Configuration {
		@AttributeDefinition(
            name = "Enable",
            description = "Sample boolean property", 
            type = AttributeType.BOOLEAN
        )
		boolean enabled() default false;
		
	}
}