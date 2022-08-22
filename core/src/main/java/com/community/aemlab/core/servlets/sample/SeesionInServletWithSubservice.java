package com.community.aemlab.core.servlets.sample;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.community.aemlab.core.utils.AEMLABConstants;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Simple Demo Servlet to get Session ",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/get/session/subservice" })

public class SeesionInServletWithSubservice extends SlingSafeMethodsServlet {

	@Reference
	private transient ResourceResolverFactory resourceFactory;

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(SeesionInServletWithSubservice.class);

	@Override
	public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType(AEMLABConstants.CONTENTTYPE_TXT_HTML);

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put(ResourceResolverFactory.SUBSERVICE, AEMLABConstants.AEMLAB_SERVICEUSER_READ);
		Session session = null;

		try (ResourceResolver resourceResolver = resourceFactory.getServiceResourceResolver(paramMap)) {
			session = resourceResolver.adaptTo(Session.class);
			if (session.isLive()) {
				response.getWriter().println("Session User is " + session.getUserID());
			}

		} catch (Exception e) {
			response.getWriter().println("Can't get session from subservice");
			LOGGER.error(e.getMessage());
		} finally {
			if (session != null && session.isLive()) {
				LOGGER.trace("Session is alive, closing now");
				session.logout();
			}
		}
	}

}