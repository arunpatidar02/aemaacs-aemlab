package com.community.aemlab.core.filters.sample;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.engine.EngineConstants;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author arunpatidar02
 *
 */
@Component(service = Filter.class, property = { Constants.SERVICE_DESCRIPTION + "=Annotation Demo Filter - OSGi",
		Constants.SERVICE_VENDOR + "=AEMLAB", Constants.SERVICE_RANKING + ":Integer=" + -700 })
@Designate(ocd = WebcomponentFilter2.Configuration.class)
public class WebcomponentFilter2 implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebcomponentFilter2.class);

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain)
			throws IOException, ServletException {

		LOGGER.debug("Filter doFilter");

		SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
		SlingHttpServletResponse slingResponse = (SlingHttpServletResponse) response;

		processResponse(slingRequest, slingResponse, filterChain);

	}

	/**
	 * logged component's html response
	 * 
	 * @param slingRequest
	 * @param slingResponse
	 * @param filterChain
	 * @throws IOException
	 * @throws ServletException
	 */
	private void processResponse(final SlingHttpServletRequest slingRequest, SlingHttpServletResponse slingResponse,
			final FilterChain filterChain) throws IOException, ServletException {
		ResponseWrapper responseWrapper = new ResponseWrapper(slingResponse, new StringWriter());
		filterChain.doFilter(slingRequest, responseWrapper);
		String contents = responseWrapper.getContent();
		LOGGER.debug("Content is {}", contents);
		slingResponse.getWriter().write(contents);
	}

	@Override
	public void init(FilterConfig filterConfig) {
		LOGGER.trace("Filter init");
	}

	@Override
	public void destroy() {
		LOGGER.trace("Filter destroy");
	}

	@ObjectClassDefinition(name = "Annotation Demo Filter - OSGi", description = "Sample Filter config")
	public @interface Configuration {

		@AttributeDefinition(name = "sling.filter.scope", description = "Filter scope e.g. "
				+ EngineConstants.FILTER_SCOPE_INCLUDE + ", " + EngineConstants.FILTER_SCOPE_COMPONENT
				+ " etc. A filter is ignored if you set an invalid sling.filter.scope. To disable a this filter use an invalid sling.filter.scope, for instance DISABLED.", type = AttributeType.STRING)
		String[] sling_filter_scope() default "DISABLED";

		@AttributeDefinition(name = "sling.filter.resourceTypes", description = "Filter ResourceTypes ", type = AttributeType.STRING)
		String[] sling_filter_resourceTypes() default { "aemlab/oneweb/components/text", "aemlab/oneweb/components/breadcrumb" };

	}

	/**
	 * Custom ResponseWrapper
	 * 
	 * @author arunpatidar02
	 *
	 */
	private static final class ResponseWrapper extends HttpServletResponseWrapper {
		private final StringWriter stringWriter;

		public ResponseWrapper(HttpServletResponse wrappedResponse, StringWriter stringWriter) {
			super(wrappedResponse);
			if (stringWriter == null) {
				stringWriter = new StringWriter();
			}
			this.stringWriter = stringWriter;
		}

		@Override
		public PrintWriter getWriter() {
			return new PrintWriter(stringWriter);
		}

		public String getContent() {
			return stringWriter.toString();
		}

	}

}