package com.community.aemlab.core.services.impl;

import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.community.aemlab.core.services.transformer.LinkRewriterPrefixerTransformerFactory.LinkRewriterPrefixerTransformer;
import com.community.aemlab.core.conf.SiteConfiguration;
import com.community.aemlab.core.services.LinkRewriter;
import com.community.aemlab.core.services.transformer.ShortenLinkRewriterTransformer;
import com.community.aemlab.core.services.transformer.ShortenLinkRewriterTransformerFactoryConfig;
import com.community.aemlab.core.utils.ShortenLinkUtil;
import com.community.aemlab.oneweb.core.services.EnvironmentTypeProvider;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.components.ComponentContext;
import com.day.cq.wcm.commons.WCMUtils;

@Component
public class LinkRewriterImpl implements LinkRewriter {

	@Reference
	ShortenLinkRewriterTransformerFactoryConfig shortenLinkRewriterTransformerFactoryConfig;

	@Reference
	private EnvironmentTypeProvider envType;

	Iterable<Pattern> whitelistedPathPatterns;

	private static final Logger LOGGER = LoggerFactory.getLogger(LinkRewriterImpl.class);
	boolean isPublish;

	@Activate
	public void activate() {
		whitelistedPathPatterns = shortenLinkRewriterTransformerFactoryConfig.getPathPatterns();
		isPublish = !envType.isAuthor();
	}

	@Override
	public String rewrite(@Nonnull String path, @Nonnull SlingHttpServletRequest request) {
		return rewrite(path, request, request.getResourceResolver(),
				ShortenLinkUtil.getCustomShortenLinksWithNumberOfPathSegmentsToCutOff(request), true);
	}

	@Override
	public String rewriteForPublish(@Nonnull String path, @Nonnull SlingHttpServletRequest request) {
		return rewrite(path, request, request.getResourceResolver(), Collections.emptyMap(), false);
	}

	@Override
	public String rewriteForPublish(@Nonnull String path, @Nonnull ResourceResolver resourceResolver) {
		return rewrite(path, null, resourceResolver, Collections.emptyMap(), false);
	}

	/**
	 * 
	 * @param request
	 * @return the content resource of the main page (i.e. the page addressed by the
	 *         initial request url), this will not change if we are currently in the
	 *         context of e.g. an ExperienceFragment being referenced from some
	 *         page. In case the component context is not set, falls back to the
	 *         current request's path.
	 */
	String getCurrentPagePath(SlingHttpServletRequest request) {
		ComponentContext componentContext = WCMUtils.getComponentContext(request);
		if (componentContext == null) {
			LOGGER.debug("Could not get component context from request, falling back to the current request resource");
			return request.getResource().getPath();
		}
		Page currentPage = componentContext.getPage();
		if (currentPage == null) {
			throw new IllegalStateException("Could not get page from component context");
		}
		return currentPage.getPath();
	}

	String rewrite(@Nonnull String path, SlingHttpServletRequest request, @Nonnull ResourceResolver resourceResolver,
			@Nonnull Map<String, Integer> customShortenLinks, boolean onlyOnPublish) {
		if (onlyOnPublish && !isPublish) {
			LOGGER.debug("Disable link rewriting  as this is not a publish instance");
			return path;
		}

		SiteConfiguration targetSiteConfiguration = LinkRewriterPrefixerTransformer
				.getSiteConfigurationForLink(resourceResolver, path);

		String urlPrefix = null;
		int numberOfPathSegmentsToCutOff = 0;
		final SiteConfiguration currentSiteConfiguration;
		if (request != null) {
			numberOfPathSegmentsToCutOff = ShortenLinkUtil.getNumberOfPathSegmentsToCutOff(request);
			currentSiteConfiguration = LinkRewriterPrefixerTransformer.getSiteConfigurationForLink(resourceResolver,
					getCurrentPagePath(request));
		} else {
			// in case no request is available use targetSiteConfiguration as source
			currentSiteConfiguration = targetSiteConfiguration;
		}
		// only use the prefixer if enabled for the current site
		if (!onlyOnPublish
				|| (currentSiteConfiguration != null && currentSiteConfiguration.useFullyQualifiedURLsInLinks())) {
			LOGGER.debug(
					"Enable link prefixing as the source site configuration enables that feature or rewriteForPublish "
							+ "was called");
			if (targetSiteConfiguration != null) {
				urlPrefix = targetSiteConfiguration.publishDomain();
				// always consider number of Segments to cut off
				numberOfPathSegmentsToCutOff = targetSiteConfiguration.numberOfSegmentsToCutOff();
				LOGGER.debug("Found site configuration for link '{}', use urlPrefix: {}.", path, urlPrefix);
			}
		}
		String rewrittenLink = path = resourceResolver.map(path);
		// only shorten if the path matches the configuration
		if (ShortenLinkRewriterTransformer.matchesOneOf(path, whitelistedPathPatterns)) {
			// how to shorten with prefixing disabled?
			rewrittenLink = ShortenLinkUtil.shortenLink(path, numberOfPathSegmentsToCutOff, customShortenLinks);
		}
		// prepend prefix
		if (StringUtils.isNotBlank(urlPrefix)) {
			rewrittenLink = urlPrefix + rewrittenLink;
		}
		return rewrittenLink;
	}

}
