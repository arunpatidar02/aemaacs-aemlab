package com.community.aemlab.core.servlets.sample;

import java.io.IOException;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.community.aemlab.core.utils.AEMLABConstants;
import com.day.cq.workflow.exec.Workflow;
import com.day.cq.workflow.status.WorkflowStatus;

/**
 * @author arunpatidar02
 *
 */
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "= Check Workflow Status Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/bin/page/check-wfstatus" })
public class GetPageWorkflowStatusServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(GetPageWorkflowStatusServlet.class);
	private static final String PAGE_PATH = "/content/aemlab/language-masters/en/";

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType(AEMLABConstants.CONTENTTYPE_TXT_HTML);
		try {
			Resource rs = req.getResourceResolver().getResource(PAGE_PATH);
			WorkflowStatus ws = rs.adaptTo(WorkflowStatus.class);

			boolean status = ws.isInRunningWorkflow(true);
			resp.getWriter().write("Workflow Found : " + status);
			if (status) {
				resp.getWriter().write("<h3>Workflows are </h3>");
				List<Workflow> wfList = ws.getWorkflows(true);
				for (Workflow wf : wfList) {
					resp.getWriter().write("<br>Workflow Instance Id : " + wf.getId());
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error while checking wf", e);
		}
	}
}
