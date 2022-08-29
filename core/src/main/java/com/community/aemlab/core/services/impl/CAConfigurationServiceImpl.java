package com.community.aemlab.core.services.impl;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.management.ConfigurationData;
import org.apache.sling.caconfig.management.ConfigurationManager;
import org.apache.sling.caconfig.management.ValueInfo;
import org.apache.sling.caconfig.management.multiplexer.ConfigurationPersistenceStrategyMultiplexer;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.community.aemlab.core.services.CAConfigurationService;

@Component(service = CAConfigurationService.class, property = {
		Constants.SERVICE_DESCRIPTION + "= Leverages CA Config SPI to get caconfig details" }, immediate = true)
public class CAConfigurationServiceImpl implements CAConfigurationService {

	@Reference
	private ConfigurationManager configManager;

	@Reference
	private ConfigurationPersistenceStrategyMultiplexer configurationPersistenceStrategy;

	@Override
	public ValueInfo<?> getConfigValue(Resource contextResource, String configName, String property) {
		ConfigurationData configData = getConfigurationData(contextResource,configName);
		return configData != null ? configData.getValueInfo(property) : null;
	}

	@Override
	public ConfigurationData getConfigurationData(Resource contextResource, String configName) {
		if (isConfigInvalid(configName)) {
			return null;
		}
		return configManager.getConfiguration(contextResource, configName);
	}

}
