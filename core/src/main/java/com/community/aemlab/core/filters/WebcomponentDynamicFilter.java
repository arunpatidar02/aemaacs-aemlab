package com.community.aemlab.core.filters;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.components.ComponentContext;
import com.day.cq.wcm.commons.WCMUtils;
import com.google.gson.Gson;

/**
 * @author arunpatidar02
 *
 */
public class WebcomponentDynamicFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebcomponentDynamicFilter.class);
	private static final String SPACE = " ";
	private static final String EQUAL = "=";
	private static final char QUOT = '"';
	private static final String DATA_JSON = "dataJSON";
	private List<String> excludedPropList = Arrays.asList("jcr:primaryType", "jcr:createdBy", "jcr:lastModifiedBy",
			"jcr:created", "jcr:lastModified", "sling:resourceType");

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain)
			throws IOException, ServletException {
		
		LOGGER.debug("Filter doFilter");
		
		SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
		SlingHttpServletResponse slingResponse = (SlingHttpServletResponse) response;
		Gson gson = new Gson();
		Resource resource = slingRequest.getResource();
		PrintWriter pw = slingResponse.getWriter();

		ComponentContext componentname = WCMUtils.getComponentContext(request);
		String tagName = componentname.getDecorationTagName();

		String datavalue = gson.toJson(getFilteredMap(resource.getValueMap()));
		String componentId = String.valueOf(resource.getPath().hashCode());
		StringBuilder sb = new StringBuilder();

		insertOpenTag(sb, tagName);
		addAttribute(sb, DATA_JSON, datavalue);
		addAttribute(sb, "component-id", componentId);

		interClosingTag(sb, tagName);
		pw.write(sb.toString());

		filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) {
		LOGGER.trace("Filter init");
	}

	@Override
	public void destroy() {
		LOGGER.trace("Filter destroy");
	}

	/**
	 * Create open tag
	 * 
	 * @param sb
	 * @param tagName
	 */
	private void insertOpenTag(StringBuilder sb, String tagName) {
		sb.append("<").append(tagName);
	}

	/**
	 * Create close tag
	 * 
	 * @param sb
	 * @param tagName
	 */
	private void interClosingTag(StringBuilder sb, String tagName) {
		sb.append("></").append(tagName).append(">");

	}

	/**
	 * Get Filter map to exclude system properties 
	 * 
	 * @param map
	 * @return filtered map
	 */
	private Map<String, Object> getFilteredMap(ValueMap map) {
		// Map -> Stream -> Filter -> MAP
		return map.entrySet().stream().filter(x -> !excludedPropList.contains(x.getKey()))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));

	}

	/**
	 * Add attribute String
	 * 
	 * @param sb
	 * @param attributeName
	 * @param attributeValue
	 */
	private void addAttribute(StringBuilder sb, String attributeName, String attributeValue) {
		sb.append(SPACE).append(attributeName).append(EQUAL).append(QUOT)
				.append(StringEscapeUtils.escapeHtml4(attributeValue)).append(QUOT);
	}

}