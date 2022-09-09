package com.community.aemlab.oneweb.core.services.impl;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.caconfig.management.ConfigurationData;
import org.apache.sling.caconfig.management.ConfigurationManager;
import org.apache.sling.caconfig.management.ValueInfo;
import org.apache.sling.caconfig.management.multiplexer.ConfigurationPersistenceStrategyMultiplexer;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.community.aemlab.oneweb.core.services.CAConfigurationService;
import com.day.cq.wcm.api.Page;

@Component(service = CAConfigurationService.class, property = {
		Constants.SERVICE_DESCRIPTION + "= Leverages CA Config SPI to get caconfig details" }, immediate = true)
public class CAConfigurationServiceImpl implements CAConfigurationService {

	@Reference
	private ConfigurationManager configManager;

	@Reference
	private ConfigurationPersistenceStrategyMultiplexer configurationPersistenceStrategy;

	private static final Logger LOGGER = LoggerFactory.getLogger(CAConfigurationServiceImpl.class);

	@Override
	public ValueInfo<?> getConfigValue(Resource contextResource, String configName, String property) {
		ConfigurationData configData = getConfigurationData(contextResource, configName);
		return configData != null ? configData.getValueInfo(property) : null;
	}

	@Override
	public ConfigurationData getConfigurationData(Resource contextResource, String configName) {
		if (isConfigInvalid(configName)) {
			return null;
		}
		return configManager.getConfiguration(contextResource, configName);
	}

	@Override
	public <T> T getConfiguration(Page page, Class<T> cacClass) {
		return getConfiguration(page.adaptTo(Resource.class), cacClass);
	}

	@Override
	public <T> T getConfiguration(Resource resource, Class<T> cacClass) {
		ConfigurationBuilder configurationBuilder = resource.adaptTo(ConfigurationBuilder.class);

		if (configurationBuilder == null) {
			LOGGER.error("CACs are not available, i.e. ConfigurationBuilder cannot be adapted from a resource!");
			return null;
		}
		
		if (configurationBuilder.name(cacClass.getName()).asValueMap().isEmpty()) {
	        LOGGER.debug("CAC not found for resource '{}'", resource.getPath());
	        return null;
	    }

		return configurationBuilder.as(cacClass);
	}

}
