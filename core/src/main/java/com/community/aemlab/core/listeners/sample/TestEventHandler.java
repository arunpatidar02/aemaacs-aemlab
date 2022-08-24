package com.community.aemlab.core.listeners.sample;

import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author arunpatidar02
 * 
 *         This is just for explicit use case. Use ResourceChangeListener
 *         instead e.g.
 *         com/community/aemlab/core/listeners/sample/SampleResourceChangeListener.java
 * 
 */
@Component(service = EventHandler.class, immediate = true, property = {
		Constants.SERVICE_DESCRIPTION + "=Demo to jcr:title listen event.topics on page creation and modification ",
		EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/ADDED",
		EventConstants.EVENT_TOPIC + "=org/apache/sling/api/resource/Resource/CHANGED",
		EventConstants.EVENT_FILTER + "(&" + "(path=/content/we-retail/us/en/*/jcr:content) (|("
				+ SlingConstants.PROPERTY_CHANGED_ATTRIBUTES + "=*jcr:title) " + "(" + ResourceChangeListener.CHANGES
				+ "=*jcr:title)))" })
public class TestEventHandler implements EventHandler {

	private static final Logger LOG = LoggerFactory.getLogger(TestEventHandler.class);

	@Override
	public void handleEvent(Event event) {
		LOG.trace("Hi event is called ......");
	}

}
