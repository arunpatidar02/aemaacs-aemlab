package com.community.aemlab.core.servlets;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@Component(immediate = true, service = Servlet.class, property = { "sling.servlet.extensions=txt",
		"sling.servlet.paths=/bin/create/xf", "sling.servlet.methods=get" })
public class CreateXFServlet extends SlingSafeMethodsServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {

		try {
			ResourceResolver resourceResolver = req.getResourceResolver();
			Session session = resourceResolver.adaptTo(Session.class);
			PageManager pm = req.getResourceResolver().adaptTo(PageManager.class);
			Page page = pm.create("/content/experience-fragments/aemlab/language-masters/en/site", "import-page",
					"/libs/cq/experience-fragments/components/experiencefragment/template", "Import", true);

			//create master
			Page masterPage=pm.create(page.getPath(), "master",
					"/conf/aemlab/settings/wcm/templates/xf-web-variation", page.getTitle(), true);
			Node masterNode = masterPage.getContentResource().adaptTo(Node.class);
			masterNode.setProperty("cq:xfVariantType", "web");
			masterNode.setProperty("cq:xfMasterVariation", true);
			
			session.save();

			resp.setContentType("text/plain");
			resp.getWriter().write("Page is created");
		} catch (Exception e) {
			resp.getWriter().write("Error : " + e.getMessage());
		}
	}

}
