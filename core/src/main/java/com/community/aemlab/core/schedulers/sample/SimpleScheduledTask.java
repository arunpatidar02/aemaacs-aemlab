package com.community.aemlab.core.schedulers.sample;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple demo for cron-job like tasks that get executed regularly. It also
 * demonstrates how property values can be set. Users can set the property
 * values in /system/console/configMgr
 */
@Designate(ocd = SimpleScheduledTask.Config.class)
@Component(service = Runnable.class)
public class SimpleScheduledTask implements Runnable {

	@ObjectClassDefinition(name = "Annotation Demo Scheduled Task - OSGi", description = "Simple demo for cron-job like task with properties")
	public static @interface Config {

		@AttributeDefinition(name = "Cron-job expression", defaultValue = "0 0 12 1 1 ? *")
		String scheduler_expression();// default "*/20 * * * * ?";

		@AttributeDefinition(name = "Concurrent task", defaultValue = "false", description = "Whether or not to schedule this task concurrently")
		boolean scheduler_concurrent() default true;

		@AttributeDefinition(name = "A parameter", description = "Can be configured in /system/console/configMgr")
		String myParameter() default "";
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleScheduledTask.class);

	private String myParameter;

	@Override
	public void run() {
		LOGGER.trace("SimpleScheduledTask is now running, myParameter='{}'", myParameter);
	}

	@Activate
	@Modified
	protected void activate(final Config config) {
		myParameter = config.myParameter();
	}

}