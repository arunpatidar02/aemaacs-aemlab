package com.community.aemlab.core.servlets.sample;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.community.aemlab.core.utils.AEMLABConstants;

@Component(service = Servlet.class, immediate = true, property = {
		Constants.SERVICE_DESCRIPTION + "=Populating Coral Dropdown using servlet",
		"sling.servlet.resourceTypes=aemlab/oneweb/concept/components/servletmodel",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET })
public class SampleServlet extends SlingSafeMethodsServlet {

	@Reference
	private transient SlingRepository repository = null;

	private static final long serialVersionUID = 2598426539166789516L;
	private static final Logger LOGGER = LoggerFactory.getLogger(SampleServlet.class);

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType(AEMLABConstants.CONTENTTYPE_TXT_HTML);
		LOGGER.debug("inside SampleServlet");
		resp.getWriter().write("Response from SampleServlet");

	}
}