package com.community.aemlab.core.filters;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.engine.EngineConstants;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.components.IncludeOptions;

@Component(service = Filter.class, property = {
		Constants.SERVICE_DESCRIPTION + "=Context Aware Configuration - Decoration Tag and Class based on configuration",
		Constants.SERVICE_VENDOR + "=AEMLAB",
		EngineConstants.SLING_FILTER_SCOPE + "=" + EngineConstants.FILTER_SCOPE_COMPONENT,
		"sling.filter.resourceTypes" + "=" + CacXfFilter.CAC_XF_RESOURCETYPE,
		Constants.SERVICE_RANKING + ":Integer=" + 201 })
public class CacXfFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(CacXfFilter.class);
	public static final String CAC_XF_RESOURCETYPE = "aemlab/oneweb/concept/components/cac-xf";
	private static final String TAGNAME = "tagName";
	private static final String CLASS_LIST = "classList";

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain)
			throws IOException, ServletException {

		SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
		IncludeOptions options = IncludeOptions.getOptions(request, true);

		Resource res = slingRequest.getResource();
		LOGGER.trace("Resource Path : {}", res.getPath());

		ValueMap vm = res.getValueMap();

		String tagName = vm.get(TAGNAME, StringUtils.EMPTY);
		String[] classList = vm.get(CLASS_LIST, new String[] {});

		LOGGER.trace("tagName : {} and classList : {}", tagName, classList);

		if (StringUtils.isNotBlank(tagName)) {
			options.setDecorationTagName(tagName);
		}

		if (ArrayUtils.isNotEmpty(classList)) {
			options.getCssClassNames().addAll(Arrays.asList(classList));
		}

		filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) {
		LOGGER.trace("init CacXfFilter");
	}

	@Override
	public void destroy() {
		LOGGER.trace("destroy CacXfFilter");
	}

}