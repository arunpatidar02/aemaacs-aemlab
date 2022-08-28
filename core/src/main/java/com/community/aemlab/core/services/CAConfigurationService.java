package com.community.aemlab.core.services;

import static com.community.aemlab.core.utils.AEMLABConstants.FORWARD_SLASH;
import static com.community.aemlab.core.utils.AEMLABConstants.SEARCH_SEQ;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.management.ValueInfo;

public interface CAConfigurationService {

	/**
	 * Get configuration data for supplied resource context
	 *
	 * @return: configuration data for given configName
	 */
	ValueInfo<?> getConfigValue(Resource contextResource, String configName, String property);

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
