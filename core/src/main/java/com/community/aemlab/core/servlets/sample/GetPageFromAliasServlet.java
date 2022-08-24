package com.community.aemlab.core.servlets.sample;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.community.aemlab.core.utils.AEMLABConstants;
import com.day.cq.wcm.api.Page;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Page Get Child Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/page/get-alias" })
public class GetPageFromAliasServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(GetPageFromAliasServlet.class);
	private static final String PAGE = "page";

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType(AEMLABConstants.CONTENTTYPE_TXT_HTML);

		Object qs = req.getParameter(PAGE);
		if (qs == null) {
			LOGGER.info("{} is null", PAGE);
			return;
		}
		try {
			Resource rs = req.getResourceResolver().resolve(qs.toString());
			Page page = rs.adaptTo(Page.class);
			if (page != null) {
				resp.getWriter().write("Resource is " + rs.getPath() + " and Page Title is " + page.getPageTitle());
			} else {
				resp.getWriter().write("Resource is " + rs.getPath() + " and No Page Found");
			}

		} catch (Exception e) {
			LOGGER.error("Not able to get Resource", e);
		}

	}
}