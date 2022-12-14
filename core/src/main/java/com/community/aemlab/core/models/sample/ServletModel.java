
package com.community.aemlab.core.models.sample;

import java.io.ByteArrayOutputStream;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.engine.SlingRequestProcessor;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.contentsync.handler.util.RequestResponseFactory;
import com.day.cq.wcm.api.WCMMode;

/**
 * @author arunpatidar02
 * 
 *         Not in used, created only for ServletModel Test
 *
 */
@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ServletModel {

	@SlingObject
	private ResourceResolver resourceResolver;

	@OSGiService
	private RequestResponseFactory requestResponseFactory;

	@OSGiService
	private SlingRequestProcessor requestProcessor;

	@SlingObject
	private Resource resource;

	@Self
	SlingHttpServletRequest request;

	protected String text = "Hello";
	private static final Logger LOGGER = LoggerFactory.getLogger(ServletModel.class);

	@PostConstruct
	protected void init() {
		text = doSlingGetRequest(resource.getPath(), requestResponseFactory, requestProcessor, resourceResolver);
		LOGGER.debug(text);
	}

	/**
	 * @param contentPath
	 * @param requestResponseFactory
	 * @param requestProcessor
	 * @param resourceResolver
	 * @return String
	 */
	private String doSlingGetRequest(final String contentPath, RequestResponseFactory requestResponseFactory,
			SlingRequestProcessor requestProcessor, ResourceResolver resourceResolver) {

		LOGGER.debug("Content Path : {}", contentPath);

		// Setup request
		HttpServletRequest req = requestResponseFactory.createRequest("GET", contentPath);
		WCMMode.DISABLED.toRequest(req);

		// Setup response
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		HttpServletResponse resp = requestResponseFactory.createResponse(out);

		try {
			// Process request through Sling
			requestProcessor.processRequest(req, resp, resourceResolver);
			return out.toString("UTF-8");
		} catch (Exception e) {
			LOGGER.error("Error while reading path {}", contentPath, e);
		}
		return null;
	}

	/**
	 * @return text
	 */
	public String getMessage() {
		return text;
	}

}
