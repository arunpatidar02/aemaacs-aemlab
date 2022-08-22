package com.community.aemlab.core.listeners.sample;

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
 */
@Component(service = EventHandler.class, immediate = true, property = {
		Constants.SERVICE_DESCRIPTION + "=Demo to listen replication event",
		EventConstants.EVENT_TOPIC + "=org/apache/sling/distribution/importer/package/imported",
		EventConstants.EVENT_FILTER + "(|(distribution.type=ADD)(distribution.type=DELETE))" })
public class DistributionEvent implements EventHandler {

	private static final Logger LOG = LoggerFactory.getLogger(DistributionEvent.class);
	public static final String DISTRIBUTION_PATHS = "distribution.paths";

	@Override
	public void handleEvent(Event event) {
		if (event.getProperty(DISTRIBUTION_PATHS) != null) {
			String[] pagePath = (String[]) event.getProperty(DISTRIBUTION_PATHS);
			LOG.trace("Hi distribution event is called ......{}", pagePath.length);
		}

	}

}
