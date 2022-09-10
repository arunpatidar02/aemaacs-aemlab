package com.community.aemlab.core.servlets.sample;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.servlet.Servlet;

import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.community.aemlab.core.utils.AEMLABConstants;

/**
 * @author arunpatidar02
 *
 */
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=GET All child Node",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/util/node/children" })
public class GetChildNodeServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 2598426539166789516L;
	private static final Logger LOGGER = LoggerFactory.getLogger(GetChildNodeServlet.class);
	private static final String PAGE_PATH = "/content/aemlab/oneweb/language-masters/en/";

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws IOException {

		resp.setContentType(AEMLABConstants.CONTENTTYPE_TXT_HTML);

		try {

			List<Node> childrenList = new ArrayList<>();
			PrintWriter pw = resp.getWriter();
			Node node = req.getResourceResolver().getResource(PAGE_PATH + JcrConstants.JCR_CONTENT).adaptTo(Node.class);

			collectChildList(childrenList, node);

			Iterator<Node> it = childrenList.iterator();
			while (it.hasNext()) {
				pw.write(it.next().getPath() + "<br>");
			}
		} catch (Exception e) {
			LOGGER.error("Error while collecting nodes", e);
		}
	}

	private void collectChildList(List<Node> childrenList, Node node) {
		try {
			childrenList.add(node);
			if (node.hasNodes()) {
				NodeIterator ni = node.getNodes();
				while (ni.hasNext()) {
					collectChildList(childrenList, ni.nextNode());
				}
			}
		} catch (RepositoryException e) {
			LOGGER.error("[collectChildList] Error while collecting nodes", e);
		}
	}

}