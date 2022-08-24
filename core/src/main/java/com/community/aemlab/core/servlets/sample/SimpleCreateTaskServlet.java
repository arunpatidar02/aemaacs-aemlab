package com.community.aemlab.core.servlets.sample;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.taskmanagement.Task;
import com.adobe.granite.taskmanagement.TaskManager;
import com.adobe.granite.taskmanagement.TaskManagerException;

@Component(service = Servlet.class, immediate = true, property = {
		Constants.SERVICE_DESCRIPTION + "= Simple Task Servlet", "sling.servlet.paths=/bin/task/create" })
public class SimpleCreateTaskServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleCreateTaskServlet.class);

	private static final String TASK_TYPE = "Notification";

	private static final String TASK_NAME = "Demo Task";
	private static final String TASK_CONTENTPATH = "/content/aemlab/en?showCustom=true";
	private static final String TASK_DESCRIPTION = "Demo Task Description";
	private static final String TASK_INSTRUCTIONS = "Demo Task instruction";
	private static final String TASK_ASSIGNEE = "admin";

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		TaskManager taskManager = request.getResourceResolver().adaptTo(TaskManager.class);
		try {
			Task newTask = taskManager.getTaskManagerFactory().newTask(TASK_TYPE);
			newTask.setName(TASK_NAME);
			newTask.setContentPath(TASK_CONTENTPATH);
			newTask.setDescription(TASK_DESCRIPTION);
			newTask.setInstructions(TASK_INSTRUCTIONS);
			newTask.setCurrentAssignee(TASK_ASSIGNEE);
			taskManager.createTask(newTask);
			LOGGER.trace("Task is created");

		} catch (TaskManagerException e) {
			LOGGER.error("Error while creating Task", e);
		}

	}

}