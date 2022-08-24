
package com.community.aemlab.core.servlets.rendercondition;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.ui.components.rendercondition.RenderCondition;
import com.adobe.granite.ui.components.rendercondition.SimpleRenderCondition;

/**
 * @author arunpatidar02
 *
 * Servlet that use to render or not render fields based on sites(content paths) and/or app paths
 * 
 * granite:rendercondition node properties 
 * - sling:resourceType (String)
 * 		aemlab/granite/rendercondition/simple/sites 
 * - hiddenSitePaths (String[])
 * 		content path regex for which field will not rendered 
 * - hiddenAppPaths	(String[])
 * 		apps path regex for which field will not rendered in dialog 
 * - and (Boolean)
 * 		true to not rendered field based on both App and Content path regex,false otherwise.
 * 
 */
@Component(service = Servlet.class, property = {
		Constants.SERVICE_DESCRIPTION + "=Custom Path RenderConditions Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.resourceTypes=" + CustomRenderConditionsServlet.RENDERCONDITION_RESOURCETYPE })
public class CustomRenderConditionsServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomRenderConditionsServlet.class);

	static final String RENDERCONDITION_RESOURCETYPE = "aemlab/granite/rendercondition/simple/sites";
	private static final String HIDDEN_SITE_PATH_PROP = "hiddenSitePaths";
	private static final String HIDDEN_APP_PATH_PROP = "hiddenAppPaths";
	private static final String AND = "and";
	private static final String REGEX = "^/mnt/override";

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {
		
		LOGGER.trace("doGet");

		boolean render = true;
		ValueMap cfg = ResourceUtil.getValueMap(req.getResource());

		// Reading renderer properties
		String[] sitePatterns = cfg.get(HIDDEN_SITE_PATH_PROP, new String[] {});
		String[] appPatterns = cfg.get(HIDDEN_APP_PATH_PROP, new String[] {});
		Boolean andCondition = cfg.get(AND, Boolean.class) != null && cfg.get(AND, Boolean.class);

		String sitePath = req.getRequestPathInfo().getSuffix();
		String appPath = req.getRequestPathInfo().getResourcePath().replaceAll(REGEX, StringUtils.EMPTY);

		if ((ArrayUtils.isNotEmpty(sitePatterns) && StringUtils.isNotBlank(sitePath))
				|| (ArrayUtils.isNotEmpty(appPatterns) && StringUtils.isNotBlank(appPath))) {
			render = evaluateRenderondition(render, sitePatterns, appPatterns, andCondition, sitePath, appPath);
		}

		req.setAttribute(RenderCondition.class.getName(), new SimpleRenderCondition(render));
	}

	/**
	 * @param render
	 * @param sitePatterns
	 * @param appPatterns
	 * @param andCondition
	 * @param sitePath
	 * @param appPath
	 * @return render
	 */
	private boolean evaluateRenderondition(boolean render, String[] sitePatterns, String[] appPatterns,
			Boolean andCondition, String sitePath, String appPath) {
		if (ArrayUtils.isNotEmpty(sitePatterns) && ArrayUtils.isNotEmpty(appPatterns)) {
			render = Boolean.TRUE.equals(andCondition)
					? isRender(sitePatterns, sitePath) || isRender(appPatterns, appPath)
					: isRender(sitePatterns, sitePath) && isRender(appPatterns, appPath);
		} else if (ArrayUtils.isNotEmpty(sitePatterns)) {
			render = isRender(sitePatterns, sitePath);
		} else if (ArrayUtils.isNotEmpty(appPatterns)) {
			render = isRender(appPatterns, appPath);
		}

		return render;
	}

	/**
	 * @param patterns
	 * @param path
	 * @return true or false
	 */
	private boolean isRender(String[] patterns, String path) {
		for (int i = 0; i < patterns.length; i++) {
			if (path.matches(patterns[i])) {
				return false;
			}
		}
		return true;
	}
}