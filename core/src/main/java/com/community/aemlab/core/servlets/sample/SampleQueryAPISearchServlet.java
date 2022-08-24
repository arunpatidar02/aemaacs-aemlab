package com.community.aemlab.core.servlets.sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

/**
 * @author arunpatidar02
 *
 */
@Component(service = Servlet.class, immediate = true, property = {
		Constants.SERVICE_DESCRIPTION + "=Sample Query Builder API Serach Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/page/sample-qb-search" })
public class SampleQueryAPISearchServlet extends SlingAllMethodsServlet {

	@Reference
	private transient QueryBuilder queryBuilder;

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(SampleQueryAPISearchServlet.class);

	@Override
	public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		ResourceResolver resourceResolver = request.getResourceResolver();
		Session session = resourceResolver.adaptTo(Session.class);

		/**
		 * Query
		 * 
		 * type=cq:Page 
		 * path=/content/aemlab 
		 * group.1_property=jcr:content/jcr:created
		 * group.1_property.operation= exists
		 * group.2_property=jcr:content/cq:lastModified
		 * group.2_property.operation=exists 
		 * group.p.or=true 
		 * p.limit=-1
		 * 
		 **/

		Map<String, String> filteredQueryParamMap = new HashMap<>();

		filteredQueryParamMap.put("type", "cq:Page");
		filteredQueryParamMap.put("path", "/content/aemlab");
		filteredQueryParamMap.put("group.1_property", "jcr:content/@jcr:created");
		filteredQueryParamMap.put("group.1_property.operation", "exists");
		filteredQueryParamMap.put("group.2_property", "jcr:content/@cq:lastModified");
		filteredQueryParamMap.put("group.2_property.operation", "exists");
		filteredQueryParamMap.put("group.p.or", "true");
		filteredQueryParamMap.put("p.limit", "-1");

		// Map 2
		Map<String, String[]> filteredQueryParamMap2 = new HashMap<>();

		filteredQueryParamMap2.put("type", new String[] { "cq:Page" });
		filteredQueryParamMap2.put("path", new String[] { "/content/aemlab", "/content/experience-fragments/aemlab" });
		filteredQueryParamMap2.put("group.1_property", new String[] { "jcr:content/@jcr:created" });
		filteredQueryParamMap2.put("group.1_property.operation", new String[] { "exists" });
		filteredQueryParamMap2.put("group.2_property", new String[] { "jcr:content/@cq:lastModified" });
		filteredQueryParamMap2.put("group.2_property.operation", new String[] { "exists" });
		filteredQueryParamMap2.put("group.p.or", new String[] { "true" });
		filteredQueryParamMap2.put("p.limit", new String[] { "-1" });

		Query query = queryBuilder.createQuery(PredicateGroup.create(filteredQueryParamMap2), session);
		SearchResult result = query.getResult();

		// QueryBuilder has a leaking ResourceResolver, so the following work around is required.
		ResourceResolver leakingResourceResolver = null;

		LOGGER.debug("Result node {}", result.getTotalMatches());

		response.getWriter().print("Result nodes found  : " + query.getResult().getHits().size());
		try {
			final List<Resource> resources = new ArrayList<>();
			for (final Hit hit : result.getHits()) {
				if (leakingResourceResolver == null) {
					// Get a reference to QB's leaking ResourceResolver
					leakingResourceResolver = hit.getResource().getResourceResolver();
				}
				resources.add(resourceResolver.getResource(hit.getPath()));
			}

		} catch (RepositoryException e) {
			LOGGER.error("Error collecting search results", e);
		} finally {
			if (leakingResourceResolver != null) {
				// Always Close the leaking QueryBuilder resourceResolver.
				leakingResourceResolver.close();
			}
		}

	}

}