package com.community.aemlab.core.filters.sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.engine.EngineConstants;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.community.aemlab.core.services.sample.CRXDERestrictionService;

/**
 * Simple Servlet filter component that redirect users on Touch UI home page if
 * access CRXDE for incoming requests.
 */
@Component(service = Filter.class, property = {
		Constants.SERVICE_DESCRIPTION + "= Filter incoming CRXDE requests and redirect to new home page",
		EngineConstants.SLING_FILTER_SCOPE + "=" + EngineConstants.FILTER_SCOPE_REQUEST,
		EngineConstants.SLING_FILTER_PATTERN + "=/libs/cq/i18n/dict.*",
		"sling.filter.methods=" + HttpConstants.METHOD_GET, Constants.SERVICE_RANKING + "=-701"

})
public class CRXDERestrictionFilter implements Filter {

	@Reference
	private CRXDERestrictionService restrictionCrxService;

	private static final String CRXDE_URL = "/crx/de/index.jsp";
	private static final String CRXDE_MODE_SCRIPT = "/apps/aemlab/oneweb/clientlibs/author/clientlib-crxredirect.js";
	private static final String SCRIPT_URL = "/libs/cq/i18n/dict";
	private static final String ADMIN = "admin";
	private static final String REFERER = "referer";

	private static final Logger LOGGER = LoggerFactory.getLogger(CRXDERestrictionFilter.class);

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain)
			throws IOException, ServletException {

		if (restrictionCrxService.isEnabled()) {
			LOGGER.debug("service is enabled");
			final SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
			final SlingHttpServletResponse slingResponse = (SlingHttpServletResponse) response;

			try {

				User currentUser = slingRequest.getResourceResolver().adaptTo(User.class);
				String userId = currentUser.getID();
				if (userId.equalsIgnoreCase(ADMIN)) {
					LOGGER.debug("admin user found");
					filterChain.doFilter(request, response);
				}

				Iterator<Group> currentUserGroups = currentUser.memberOf();
				List<String> currentUserGroupids = new ArrayList<>();
				while (currentUserGroups.hasNext()) {
					Group grp = currentUserGroups.next();
					currentUserGroupids.add(grp.getID());
				}

				String refererURL = slingRequest.getHeader(REFERER);
				String url = slingRequest.getPathInfo();

				if (url.contains(SCRIPT_URL) && refererURL != null && refererURL.contains(CRXDE_URL)
						&& restrictionCrxService.isRestrictedUser(currentUserGroupids, userId)) {
					LOGGER.debug("CRXDE url is accessed");

					slingResponse.sendRedirect(CRXDE_MODE_SCRIPT);

				}
			} catch (Exception e) {
				LOGGER.error("Error : ", e);
			}

		}
		filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		LOGGER.trace("CRXDERestrictionFilter init");

	}

	@Override
	public void destroy() {
		LOGGER.trace("CRXDERestrictionFilter destroy");

	}

}