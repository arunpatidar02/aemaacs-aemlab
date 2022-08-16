package com.community.aemlab.core.filters.sample;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.sling.engine.EngineConstants;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.components.IncludeOptions;

/**
 * @author arunpatidar02
 *
 */
@Component(service = Filter.class, property = {
		EngineConstants.SLING_FILTER_SCOPE + "=" + EngineConstants.FILTER_SCOPE_COMPONENT,
		"sling.filter.resourceTypes" + "=" + XFFilter.XF_RESOURCETYPE, Constants.SERVICE_RANKING + ":Integer=" + 201 })
public class XFFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(XFFilter.class);
	public static final String XF_RESOURCETYPE = "cq/experience-fragments/editor/components/experiencefragment";
	private static final String CLASS_NAME = "footer";
	private static final String TAG_NAME = "div";

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain)
			throws IOException, ServletException {

		LOGGER.trace("doFilter");

		IncludeOptions options = IncludeOptions.getOptions(request, true);
		options.getCssClassNames().add(CLASS_NAME);
		options.setDecorationTagName(TAG_NAME);

		filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) {
		LOGGER.trace("init");
	}

	@Override
	public void destroy() {
		LOGGER.trace("destroy");
	}

}