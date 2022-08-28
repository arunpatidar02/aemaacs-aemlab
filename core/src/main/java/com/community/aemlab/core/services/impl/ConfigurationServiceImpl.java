package com.community.aemlab.core.services.impl;

import static com.community.aemlab.core.utils.AEMLABConstants.FORWARD_SLASH;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.management.ConfigurationCollectionData;
import org.apache.sling.caconfig.management.ConfigurationData;
import org.apache.sling.caconfig.management.ConfigurationManager;
import org.apache.sling.caconfig.management.ValueInfo;
import org.apache.sling.caconfig.management.multiplexer.ConfigurationPersistenceStrategyMultiplexer;
import org.apache.sling.caconfig.spi.metadata.PropertyMetadata;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.community.aemlab.core.services.ConfigurationService;

@Component(service = ConfigurationService.class, property = {
		Constants.SERVICE_DESCRIPTION + "= Leverages CA Config SPI to get caconfig details" }, immediate = true)
public class ConfigurationServiceImpl implements ConfigurationService {

	@Reference
	private ConfigurationManager configManager;

	@Reference
	private ConfigurationPersistenceStrategyMultiplexer configurationPersistenceStrategy;

	@Override
	public ValueInfo<?> getConfigValue(Resource contextResource, String configName, String property) {
		if (isConfigInvalid(configName)) {
			return null;
		}
		ConfigurationData configData = configManager.getConfiguration(contextResource, configName);
		return configData != null ? configData.getValueInfo(property) : null;
	}

	@Override
	public List<String> getConfigCollection(Resource contextResource, String configName, String property) {

		List<String> configList = new ArrayList<>();
		if (isConfigInvalid(configName)) {
			return configList;
		}
		ConfigurationData config = configManager.getConfiguration(contextResource, configName);
		ValueInfo<?> configItem = config != null ? config.getValueInfo(property) : null;
		PropertyMetadata<?> itemMetadata = configItem != null ? configItem.getPropertyMetadata() : null;
		String subConfigName = configName;
		if (itemMetadata != null && itemMetadata.isNestedConfiguration()) {
			String parentConfigName = configurationPersistenceStrategy
					.getCollectionParentConfigName(config.getConfigName(), config.getResourcePath());
			String itemConfigName = itemMetadata.getConfigurationMetadata().getName();

			subConfigName = StringUtils.join(parentConfigName, FORWARD_SLASH, itemConfigName);
		}
		if (itemMetadata != null && itemMetadata.getType().isArray()) {
			ConfigurationCollectionData subConfigCollection = configManager.getConfigurationCollection(contextResource,
					subConfigName);
			subConfigCollection.getItems().forEach(
					c -> configList.add(c.getConfigName().concat(FORWARD_SLASH).concat(c.getCollectionItemName())));
		}
		return configList;
	}

}
