package com.community.aemlab.core.services.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.community.aemlab.core.services.EnvironmentTypeProvider;

/**
 * @author arunpatidar02
 *
 */
@Component(service = EnvironmentTypeProvider.class, immediate = true, name = "Environment Type")
public class EnvironmentTypeProviderImpl implements EnvironmentTypeProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(EnvironmentTypeProviderImpl.class);

	private static final String ENVIRONMENT_TYPE = "environment.type";
	private static final String DEFAULT_ENVIRONMENT_TYPE = "localhost";
	private String environmentType;

	@Activate
	@Modified
	protected void activate(final Map<String, String> config) {
		environmentType = config.get(ENVIRONMENT_TYPE);
		if (StringUtils.isBlank(environmentType)) {
			environmentType = DEFAULT_ENVIRONMENT_TYPE;
		}
		LOGGER.debug("Environment Type Service Activated with Environment Type of {}", environmentType);
	}

	@Override
	public String getEnvironmentType() {
		return environmentType;
	}

}