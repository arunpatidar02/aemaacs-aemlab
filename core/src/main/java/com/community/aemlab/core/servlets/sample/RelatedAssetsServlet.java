package com.community.aemlab.core.servlets.sample;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.asset.api.Asset;
import com.adobe.granite.asset.api.AssetManager;
import com.community.aemlab.core.utils.AEMLABConstants;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=servlet to relate assets",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/demo/relate-assets" })
public class RelatedAssetsServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 2598426539166789516L;
	private static final Logger LOGGER = LoggerFactory.getLogger(RelatedAssetsServlet.class);

	@Override
	protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response)
			throws IOException {
		response.setContentType(AEMLABConstants.CONTENTTYPE_TXT_HTML);
		try {
			AssetManager assetMgr = request.getResourceResolver().adaptTo(AssetManager.class);
			String parentAsset = "/content/dam/aemlab/banners/banner-1.png";
			String childAsset = "/content/dam/aemlab/images/banner-1.png/asset.jpg";
			if (assetMgr.assetExists(parentAsset) && assetMgr.assetExists(childAsset)) {
				Asset asset = assetMgr.getAsset(parentAsset);
				asset.addRelation("derived", childAsset);
			}
			response.getWriter().print("<p>Relation added</p>");
			Iterator<? extends Asset> it = assetMgr.getAsset(parentAsset).listRelated("derived");
			response.getWriter().print("<p>Relation from derived</p>");
			while (it.hasNext()) {
				response.getWriter().print(it.next().getPath() + "<br>");
			}
		} catch (Exception e) {
			LOGGER.error("Error while seraching related assets", e);
		}
	}
}