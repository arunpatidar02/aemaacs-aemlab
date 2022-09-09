package com.community.aemlab.core.servlets.sample;

import java.io.IOException;
import java.util.Map;

import javax.jcr.Node;
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
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.dam.commons.util.AssetReferenceSearch;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;

/**
 * @author arunpatidar02
 *
 */
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Page Get Asset Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/page/get-asset" })
public class GetPageAsset extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;
	private static final String PAGE = "page";

	private static final Logger LOGGER = LoggerFactory.getLogger(GetPageAsset.class);

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType(AEMLABConstants.CONTENTTYPE_TXT_HTML);

		Object qs = req.getParameter(PAGE);
		if (qs == null) {
			LOGGER.info("{} is null", PAGE);
			resp.getWriter().write(
					"Please provide Page path in page quesry string parameter e.g. ?page=/content/aemlab/oneweb/reference-content");
			return;
		}

		try {
			Resource rs = req.getResourceResolver().resolve(qs.toString());
			Page page = rs.adaptTo(Page.class);
			if (page != null) {
				resp.getWriter().write("<p>Page has followings assets references : </p>");
				Resource resource = req.getResourceResolver()
						.getResource(qs.toString() + "/" + JcrConstants.JCR_CONTENT);
				Node node = resource.adaptTo(Node.class);
				AssetReferenceSearch assetReference = new AssetReferenceSearch(node, DamConstants.MOUNTPOINT_ASSETS,
						req.getResourceResolver());
				for (Map.Entry<String, Asset> assetMap : assetReference.search().entrySet()) {
					Asset asset = assetMap.getValue();
					Resource resource2 = req.getResourceResolver()
							.getResource(asset.getPath() + "/" + JcrConstants.JCR_CONTENT);
					Node assetsNode = resource2.adaptTo(Node.class);
					String replicationStatus = "NOT REPLICATED YET";
					if (assetsNode.hasProperty(NameConstants.PN_PAGE_LAST_REPLICATION_ACTION)) {
						replicationStatus = assetsNode.getProperty(NameConstants.PN_PAGE_LAST_REPLICATION_ACTION)
								.getString();
					}
					resp.getWriter().write("<br>");
					resp.getWriter()
							.write("Assets " + asset.getPath() + " --- Replication Status " + replicationStatus);

				}
			} else {
				resp.getWriter().write("Resource is " + rs.getPath() + " and No Page Found");
			}

		} catch (Exception e) {
			LOGGER.error("Not able to get Resource", e);
		}
	}
}