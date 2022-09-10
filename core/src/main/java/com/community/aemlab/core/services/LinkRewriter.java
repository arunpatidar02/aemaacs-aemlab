package com.community.aemlab.core.services;

import javax.annotation.Nonnull;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface LinkRewriter {
	/**
	 * 
	 * @param path
	 * @param request
	 * @return the rewritten link in case rewriting was enabled, otherwise the given
	 *         path
	 */
	String rewrite(@Nonnull String path, @Nonnull SlingHttpServletRequest request);

	/**
	 * Works similar as {@link #rewrite(String, SlingHttpServletRequest)} but works
	 * independent of whether the current instance is author or publish.
	 * 
	 * @param path
	 * @param request
	 * @return the rewritten link in case rewriting was enabled, otherwise the given
	 *         path
	 */
	String rewriteForPublish(@Nonnull String path, @Nonnull SlingHttpServletRequest request);

	/**
	 * Works similar as {@link #rewriteForPublish(String, SlingHttpServletRequest)}
	 * but can be used in code where there is no request available.
	 * 
	 * @param path
	 * @param resourceResolver the resource resolver to use to resolve the site CACs
	 *                         and to apply the mapping
	 * @return the rewritten link
	 */
	String rewriteForPublish(@Nonnull String path, @Nonnull ResourceResolver resourceResolver);

}
