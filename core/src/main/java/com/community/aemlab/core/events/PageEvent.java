package com.community.aemlab.core.events;

import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = EventHandler.class, immediate = true, property = {
		Constants.SERVICE_DESCRIPTION + "=Demo to listen event on page modification ",
		EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/ADDED",
		EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/CHANGED",
		EventConstants.EVENT_FILTER + "=(path=/content/aemlab/us/*/jcr:content)" })
public class PageEvent implements EventHandler {

	private static final Logger LOG = LoggerFactory.getLogger(PageEvent.class);

	@Override
	public void handleEvent(Event event) {
		LOG.info("Event is called with paths PageEvent Test ......");
	}

}