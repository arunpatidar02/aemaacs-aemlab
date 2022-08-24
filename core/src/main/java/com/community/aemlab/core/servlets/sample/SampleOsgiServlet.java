package com.community.aemlab.core.servlets.sample;

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

import com.community.aemlab.core.services.sample.SampleOsgiService;
import com.community.aemlab.core.utils.AEMLABConstants;

/**
 * @author arunpatidar02
 *
 */
@Component(immediate = true, service = Servlet.class, property = { "sling.servlet.extensions=txt",
		"sling.servlet.paths=/bin/osgi", "sling.servlet.paths=/bin/foo", "sling.servlet.methods=get" })
@Designate(ocd = SampleOsgiServlet.Configuration.class)
public class SampleOsgiServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;
	
	private static int counter = 0; 

	private static final Logger LOGGER = LoggerFactory.getLogger(SampleOsgiServlet.class);

	@Reference
	private transient SampleOsgiService sampleOsgiService;

	private boolean enabled;

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {
		
		resp.setContentType(AEMLABConstants.CONTENTTYPE_TXT_HTML);

		PrintWriter out = resp.getWriter();
		LOGGER.debug("sampleOsgiService STARTED......");
		out.write("Annotation Demo Servlet - OSGi - enabled: " + enabled + "\n");
		out.write(sampleOsgiService.getSettings());
		LOGGER.debug("sampleOsgiService END......{}", counter++);
	}

	@Activate
	@Modified
	protected void activate(Configuration config) {
		enabled = config.enabled();
	}

	@ObjectClassDefinition(name = "Annotation Demo Servlet - OSGi", description = "Sample servlet config")
	public @interface Configuration {
		@AttributeDefinition(name = "Enable", description = "Sample boolean property", type = AttributeType.BOOLEAN)
		boolean enabled() default false;

	}
}