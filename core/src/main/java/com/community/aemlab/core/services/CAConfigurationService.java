package com.community.aemlab.core.services;

import static com.community.aemlab.core.utils.AEMLABConstants.FORWARD_SLASH;
import static com.community.aemlab.core.utils.AEMLABConstants.SEARCH_SEQ;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.management.ConfigurationData;
import org.apache.sling.caconfig.management.ValueInfo;

import com.day.cq.wcm.api.Page;

public interface CAConfigurationService {

	/**
	 * Get configuration data for supplied resource context
	 *
	 * @param contextResource
	 * @param configName
	 *                cac config name
	 * @param property
	 * 				  one of the cac property
	 * @return: configuration data for given configName and property
	 */
	ValueInfo<?> getConfigValue(Resource contextResource, String configName, String property);

	/**
	 * Get configuration data for supplied resource context
	 *
	 * @param contextResource
	 * @param configName
	 * @return: configuration data for given configName
	 */
	ConfigurationData getConfigurationData(Resource contextResource, String configName);
	
	/**
	 * Get configuration for supplied page
	 *
	 * @param page
	 * @param cacClass
	 *            class of the Context aware configuration
	 * @param <T>
	 *            type of class of the Context aware configuration
	 * @return object of cac retrieved from the configuration
	 */
	<T> T getConfiguration(final Page page, Class<T> cacClass);
	
	/**
	 * Get configuration for supplied resource context
	 *
	 * @param resource
	 * @param cacClass
	 *            class of the Context aware configuration
	 * @param <T>
	 *            type of class of the Context aware configuration
	 * @return object of cac retrieved from the configuration
	 */
	<T> T getConfiguration(final Resource resource, Class<T> cacClass);

	/**
	 * Get list of all configuration items in given configuration/sub-configuration
	 *
	 * @return: collection of config items for given context resource
	 */

	public default boolean isConfigInvalid(final String configName) {
		return StringUtils.isBlank(configName) || StringUtils.startsWith(configName, FORWARD_SLASH)
				|| StringUtils.contains(configName, SEARCH_SEQ);
	}

}
