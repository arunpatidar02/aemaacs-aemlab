package com.community.aemlab.core.servlets.sample;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.community.aemlab.core.utils.AEMLABConstants;
import com.community.aemlab.oneweb.core.services.AEMLABResourceResolverProvider;

/**
 * @author arunpatidar02
 *
 */
@Component(service = Servlet.class, immediate = true, property = {
		Constants.SERVICE_DESCRIPTION + "= Test AEMLABResourceResolverProvider Servlet",
		"sling.servlet.paths=/bin/get/resource" })
public class GetResourceFromSubServiceServlet extends SlingSafeMethodsServlet {

	@Reference
	private transient AEMLABResourceResolverProvider resolverProvider;

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(GetResourceFromSubServiceServlet.class);
	private static final String PATH = "/content/aemlab/oneweb/reference-content";

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType(AEMLABConstants.CONTENTTYPE_TXT_HTML);

		try {

			ResourceResolver rr = resolverProvider.getReadResourceResolver();
			Resource res = rr.getResource(PATH);
			LOGGER.debug("ResourceResolver User - {} and Resource Path - {}", rr.getUserID(), res.getPath());
			resolverProvider.closeResourceResolver(rr);

			rr = resolverProvider.getWriteResourceResolver();
			res = rr.getResource(PATH);
			LOGGER.debug("ResourceResolver User - {} and Resource Path - {}", rr.getUserID(), res.getPath());
			resolverProvider.closeResourceResolver(rr);

		} catch (Exception e) {
			LOGGER.error("Error white getting Resources", e);
		}

	}

}