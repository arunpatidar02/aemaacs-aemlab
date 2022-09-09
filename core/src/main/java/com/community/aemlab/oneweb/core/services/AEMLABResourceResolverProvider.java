package com.community.aemlab.oneweb.core.services;

import org.apache.sling.api.resource.ResourceResolver;

public interface AEMLABResourceResolverProvider {

	/**
	 * @return resourceResolver
	 */
	public ResourceResolver getReadResourceResolver();

	/**
	 * @return resourceResolver
	 */
	public ResourceResolver getWriteResourceResolver();

	/**
	 * @param resourceResolver
	 */
	public void closeResourceResolver(ResourceResolver resourceResolver);

}
