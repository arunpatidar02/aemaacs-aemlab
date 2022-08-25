package com.community.aemlab.core.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.community.aemlab.core.services.AEMLABResourceResolverProvider;
import com.community.aemlab.core.utils.AEMLABConstants;

/**
 * @author arunpatidar02
 *
 */
@Component(service = AEMLABResourceResolverProvider.class, immediate = true, name = "AEMLABResourceResolverProvider Factory")
public class AEMLABResourceResolverProviderImpl implements AEMLABResourceResolverProvider {

	@Reference
	private ResourceResolverFactory resourceFactory;

	private static final Logger LOGGER = LoggerFactory.getLogger(AEMLABResourceResolverProviderImpl.class);

	@Override
	public ResourceResolver getResourceResolver(String subserviceName) {
		return getResourceResourceResolver(subserviceName);
	}

	@Override
	public ResourceResolver getReadResourceResolver() {
		return getResourceResourceResolver(AEMLABConstants.AEMLAB_SUBSERVICE_READ);
	}

	@Override
	public ResourceResolver getWriteResourceResolver() {
		return getResourceResourceResolver(AEMLABConstants.AEMLAB_SUBSERVICE_WRITE);
	}

	@Override
	public void closeResourceResolver(ResourceResolver resourceResolver) {
		closeResResolver(resourceResolver);
	}

	/**
	 * @param isRead
	 * @return ResourceResolver
	 */
	private ResourceResolver getResourceResourceResolver(String subserviceName) {
		Map<String, Object> map = new HashMap<>();
		map.put(ResourceResolverFactory.SUBSERVICE, subserviceName);
		try {
			return resourceFactory.getServiceResourceResolver(map);
		} catch (LoginException e) {
			LOGGER.error("[AEMLABResourceResolverProvider] not able to get resourceResolver from subservice session",
					e);
		}

		return null;
	}

	/**
	 * @param resourceResolver
	 */
	private void closeResResolver(ResourceResolver resourceResolver) {
		try {
			if (resourceResolver != null && resourceResolver.isLive()) {
				LOGGER.trace("[AEMLABResourceResolverProvider] closing resourceResolver");
				resourceResolver.close();
			}
		} catch (Exception e) {
			LOGGER.error("[AEMLABResourceResolverProvider] not able to close resourceResolver ", e);
		}
	}

}