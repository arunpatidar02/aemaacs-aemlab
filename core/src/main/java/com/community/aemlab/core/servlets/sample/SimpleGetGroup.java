package com.community.aemlab.core.servlets.sample;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component(service = Servlet.class, immediate = true, property = {
		Constants.SERVICE_DESCRIPTION + "=Simple Get Group Servlet", "sling.servlet.paths=/bin/get/groups", })
public class SimpleGetGroup extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleGetGroup.class);

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		try {
			response.setHeader("Content-Type", "text/html");

			User currentUser = request.getResourceResolver().adaptTo(User.class); 
			Iterator<Group> currentUserGroups = currentUser.memberOf();

			while (currentUserGroups.hasNext()) {
				Group grp =  currentUserGroups.next();
				response.getWriter().print(grp.getID() + "<br/>");
			}

			response.getWriter().close();

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

	}
}