package com.community.aemlab.core.servlets.sample;

import java.io.IOException;

import javax.jcr.Session;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;
import javax.jcr.version.VersionManager;
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
import com.community.aemlab.oneweb.core.services.constants.OneWebConstants;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Get Page Version Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/page/getVersion" })
public class GetPageVersionServlet extends SlingSafeMethodsServlet {

	@Reference
	private transient SlingRepository repository = null;

	private static final long serialVersionUID = 2598426539166789516L;
	private static final Logger LOGGER = LoggerFactory.getLogger(GetPageVersionServlet.class);

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {
		String qs = "/content/aemlab/oneweb/oneweb/us/en/jcr:content";
		resp.setContentType(AEMLABConstants.CONTENTTYPE_TXT_HTML);

		try {

			VersionManager vm = getJCRSession().getWorkspace().getVersionManager();
			VersionHistory vh;
			Version version;

			vh = vm.getVersionHistory(qs);
			version = vm.getBaseVersion(qs);
			resp.getWriter().write("Base Version is " + version.getPath() + "<br>");

			VersionIterator vi = vh.getAllVersions();
			resp.getWriter().write("Other Version are <br>");
			while (vi.hasNext()) {
				Version v = vi.nextVersion();
				resp.getWriter().write("<br>" + vi.getPosition() + "." + v.getPath());
			}

		} catch (Exception e) {
			resp.getWriter().write("ERROR : Not able to get Version, something is wrong --->" + e);
			LOGGER.error("Error Occured ", e);
		}

	}

	public Session getJCRSession() {
		Session session = null;
		try {
			session = repository.loginService(OneWebConstants.ONEWEB_SUBSERVICE_READ, null);
		} catch (Exception e1) {
			e1.printStackTrace();
			LOGGER.error("getJCRSession : Unable to Login : ", e1);
		}
		return session;
	}
}