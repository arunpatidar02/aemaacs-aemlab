package com.community.aemlab.core.events;

import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*This does not work because of EVENT_FILTER*/

@Component(service = EventHandler.class, immediate = true, property = {
		Constants.SERVICE_DESCRIPTION + "=Demo to listen event.job.topic on page title change ",
		EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/ADDED",
		EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/CHANGED",
		EventConstants.EVENT_FILTER + "=(&" + "(path=/content/aemlab/us/*/jcr:content) (|("
				+ SlingConstants.PROPERTY_CHANGED_ATTRIBUTES + "=*jcr:title) " + "(" + ResourceChangeListener.CHANGES
				+ "=*jcr:title)))" })
public class TestEventHandler implements EventHandler {

	private static final Logger LOG = LoggerFactory.getLogger(TestEventHandler.class);

	@Override
	public void handleEvent(Event event) {
		LOG.info("Hi TestEventHandler event handler is called PageEvent Test......");
	}

}