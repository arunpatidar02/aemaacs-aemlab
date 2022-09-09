package com.community.aemlab.core.servlets.sample;

import java.io.IOException;
import java.io.PrintWriter;
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
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.community.aemlab.core.utils.AEMLABConstants;
import com.community.aemlab.oneweb.core.services.constants.OneWebConstants;

/**
 * @author arunpatidar02
 *
 */
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Simple Demo Servlet to get Session ",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/get/session/subservice" })

public class SeesionInServletWithSubservice extends SlingSafeMethodsServlet {

	@Reference
	private transient ResourceResolverFactory resourceFactory;

	@Reference
	private transient SlingRepository repository = null;

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(SeesionInServletWithSubservice.class);

	@Override
	public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType(AEMLABConstants.CONTENTTYPE_TXT_HTML);
		PrintWriter pw = response.getWriter();

		sampleSubserviceSession(pw);
		sampleJCRSession(pw);
	}

	private void sampleSubserviceSession(PrintWriter pw) {
		Session session = null;
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put(ResourceResolverFactory.SUBSERVICE, OneWebConstants.ONEWEB_SUBSERVICE_READ);
		try (ResourceResolver resourceResolver = resourceFactory.getServiceResourceResolver(paramMap)) {
			LOGGER.trace("User id inside {}", resourceResolver.getUserID());
			session = resourceResolver.adaptTo(Session.class);
			// Business logic
			pw.println("Session User from getServiceResourceResolver is - " + session.getUserID());

		} catch (Exception e) {
			LOGGER.error("sampleSubserviceSession : Unable to Login : ", e);
		} finally {
			closeSession(session);
		}
	}

	// Get Session for JCR repository operation
	private void sampleJCRSession(PrintWriter pw) {
		Session session = null;
		try {
			session = repository.loginService(OneWebConstants.ONEWEB_SUBSERVICE_READ, null);
			LOGGER.trace("User id inside {}", session.getUserID());
			// Business logic
			pw.println("Session User from loginService is - " + session.getUserID());
		} catch (Exception e1) {
			e1.printStackTrace();
			LOGGER.error("sampleJCRSession : Unable to Login : ", e1);
		} finally {
			closeSession(session);
		}
	}

	// Close Session
	private void closeSession(Session session) {
		if (session != null && session.isLive()) {
			session.logout();
		}
	}

}