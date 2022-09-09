package com.community.aemlab.oneweb.core.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.util.converter.Converters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.community.aemlab.oneweb.core.services.EnvironmentTypeProvider;
import com.day.cq.commons.Externalizer;

/**
 * @author arunpatidar02
 *
 */
@Component(service = EnvironmentTypeProvider.class, immediate = true, property = {
		EnvironmentTypeProviderImpl.PRIMARY_RUNMODE + "=" + Externalizer.AUTHOR,
		EnvironmentTypeProviderImpl.SECONDARY_RUNMODE + "=" + EnvironmentTypeProviderImpl.DEFAULT_ENVIRONMENT_TYPE })
public class EnvironmentTypeProviderImpl implements EnvironmentTypeProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(EnvironmentTypeProviderImpl.class);

	private static final String ENVIRONMENT_TYPE = "environment.type";
	public static final String PRIMARY_RUNMODE = "primary.runmode";
	public static final String SECONDARY_RUNMODE = "secondary.runmodes";
	
	public static final String DEFAULT_ENVIRONMENT_TYPE = "localhost";

	private String environmentType;
	private List<String> runModesList;
	boolean isPublish = false;

	@Activate
	@Modified
	protected void activate(final Map<String, String> config) {
		environmentType = config.get(ENVIRONMENT_TYPE);
		if (StringUtils.isBlank(environmentType)) {
			environmentType = DEFAULT_ENVIRONMENT_TYPE;
		}
		LOGGER.debug("Environment Type Service Activated with Environment Type of {}", environmentType);

		runModesList = new ArrayList<>();

		runModesList.add(Converters.standardConverter().convert(config.get(PRIMARY_RUNMODE))
				.defaultValue(Externalizer.AUTHOR).to(String.class));

		runModesList.addAll(Arrays.asList(Converters.standardConverter().convert(config.get(SECONDARY_RUNMODE))
				.defaultValue(new String[] { DEFAULT_ENVIRONMENT_TYPE }).to(String[].class)));

		isPublish = runModesList.contains(Externalizer.PUBLISH);

		LOGGER.debug("Runmodes are {}", runModesList);
	}

	@Override
	public String getEnvironmentType() {
		return environmentType;
	}

	@Override
	public List<String> getRunModes() {
		return runModesList;
	}

	@Override
	public boolean isAuthor() {
		return !isPublish;
	}

}