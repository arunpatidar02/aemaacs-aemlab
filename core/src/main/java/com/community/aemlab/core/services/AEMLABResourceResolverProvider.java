package com.community.aemlab.core.services;

import org.apache.sling.api.resource.ResourceResolver;

public interface AEMLABResourceResolverProvider {

	/**
	 * @param subserviceName
	 * @return resourceResolver
	 */
	public ResourceResolver getResourceResolver(String subserviceName);
	
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
