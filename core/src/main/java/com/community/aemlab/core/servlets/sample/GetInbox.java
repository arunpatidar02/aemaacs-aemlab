package com.community.aemlab.core.servlets.sample;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.taskmanagement.Filter;
import com.adobe.granite.taskmanagement.Task;
import com.adobe.granite.taskmanagement.TaskManager;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.community.aemlab.core.utils.AEMLABConstants;

/**
 * @author arunpatidar02
 *
 */
@Component(service = Servlet.class, immediate = true, property = {
		Constants.SERVICE_DESCRIPTION + "= Simple Inbox Servlet", "sling.servlet.paths=/bin/user/inbox" })
public class GetInbox extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(GetInbox.class);

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType(AEMLABConstants.CONTENTTYPE_TXT_HTML);
		StringBuilder sb = new StringBuilder();

		try {

			TaskManager taskManager = request.getResourceResolver().adaptTo(TaskManager.class);
			Iterator<Task> ti = taskManager.getTasks(new Filter());
			response.getWriter().write("<h2>Task Items</h2>");
			sb.append(
					"<table border='1'><tr><td>S.No</td><td>Task Id</td><td>Created by</td><td>Assignee</td><td>Task Description</td></tr>");
			int c = 1;
			while (ti.hasNext()) {
				sb.append("<tr>");
				sb.append("<td>").append(c++).append("</td>");
				Task task = ti.next();

				sb.append("<td>" + task.getId() + "</td>");
				sb.append("<td>" + task.getCreatedBy() + "</td>");
				sb.append("<td>" + task.getCurrentAssignee() + "</td>");
				sb.append("<td>" + task.getDescription() + "</td>");

				sb.append("</tr>");
			}
			sb.append("</table>");

			response.getWriter().write(sb.toString());

			WorkflowSession graniteWorkflowSession = request.getResourceResolver().adaptTo(WorkflowSession.class);
			WorkItem[] workItems = graniteWorkflowSession.getActiveWorkItems();
			sb.setLength(0);
			sb.append("<h2>Workflow Items(" + workItems.length + ")</h2>");
			sb.append(
					"<table border='1'><tr><td>S.No</td><td>workflow Id</td><td>Initaited by</td><td>Assignee</td><td>Type</td><td>Due Date</td></tr>");
			for (int i = 0; i < workItems.length; i++) {
				sb.append("<tr>");
				sb.append("<td>").append(i + 1).append("</td>");

				sb.append("<td>" + workItems[i].getId() + "</td>");
				sb.append("<td>" + workItems[i].getWorkflow().getInitiator() + "</td>");
				sb.append("<td>" + workItems[i].getCurrentAssignee() + "</td>");
				sb.append("<td>" + workItems[i].getItemType() + "</td>");
				sb.append("<td>" + workItems[i].getDueTime() + "</td>"); // dueTime property
				sb.append("</tr>");
			}
			sb.append("</table>");

			response.getWriter().write(sb.toString());

		} catch (Exception e) {
			LOGGER.error("Error white getting inbox items", e);
			response.getWriter().write(e.getMessage());
		}

	}

}