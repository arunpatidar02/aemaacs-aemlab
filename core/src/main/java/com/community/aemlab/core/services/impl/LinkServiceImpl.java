package com.community.aemlab.core.services.impl;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.community.aemlab.core.services.LinkService;
import com.community.aemlab.core.utils.AEMLABConstants;
import com.day.cq.commons.PathInfo;

/**
 *
 * LinkService implementation
 */
@Component(service = LinkService.class, property = {
		Constants.SERVICE_DESCRIPTION + "=LinkService for externalizing links" }, immediate = true)
public class LinkServiceImpl implements LinkService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LinkServiceImpl.class);

	@Override
	public boolean isExternal(String link) {
		return !StringUtils.startsWith(link, AEMLABConstants.AEMLAB_CONTENT_ROOT);
	}

	@Override
	public boolean isDamLink(String link) {
		return StringUtils.startsWith(link, AEMLABConstants.AEMLAB_DAM_ROOT);
	}

	@Override
	public String rewriteRelativeLink(ResourceResolver resolver, String link) {
		return rewriteRelativeLink(resolver, link, null, null, null, null);
	}

	@Override
	public String rewriteRelativeLink(ResourceResolver resolver, String link, String selectors, String extension,
			String suffix, String anchor) {
		// handle anchor only
		if (isAnchorOnlyLink(link, anchor)) {
			return AEMLABConstants.HASH + anchor;
		}

		// Only map absolute paths (starting w /), avoid relative-scheme URLs
		// starting w //
		String rewrittenLink = link;
		if (isRewriteLinkRequired(link)) {
			if (isContentLink(link)) {
				rewrittenLink = rewriteContentLink(resolver, link, selectors, extension, suffix, anchor);
			} else {
				// Apply Sling Mappings
				rewrittenLink = resolver.map(link);
			}
		}
		return rewrittenLink;
	}

	private String rewriteContentLink(ResourceResolver resolver, String link, String selectors, String extension,
			String suffix, String anchor) {

		URI originalUri = null;
		String path;
		// isolate the path part of the URI
		try {
			originalUri = new URI(link);
			path = originalUri.getPath();

			if (StringUtils.isEmpty(anchor)) {
				anchor = originalUri.getFragment();
			}
		} catch (URISyntaxException e) {
			LOGGER.warn("Given link '{}' is not a valid URI, potentially incorrectly rewritten as the whole "
					+ "link is assumed to be the path!", link);
			path = link;
		}

		final PathInfo pathInfo = getPathInfo(resolver, path);
		String resourcePath = pathInfo.getResourcePath();
		String pathInfoExtension = pathInfo.getExtension();
		String pathInfoSelectors = StringUtils.join(pathInfo.getSelectors(), AEMLABConstants.DOT);
		String pathInfoSuffix = pathInfo.getSuffix();

		if ((StringUtils.isNotEmpty(pathInfoSelectors) || StringUtils.isNotEmpty(pathInfoExtension)
				|| StringUtils.isNotEmpty(pathInfoSuffix)) && StringUtils.equals(resourcePath, link)) {
			// resource does not exist, do not rewrite link
			return link;
		}

		if (StringUtils.isEmpty(resourcePath)) {
			resourcePath = link;
		}
		if (StringUtils.isEmpty(pathInfoExtension)) {
			pathInfoExtension = extension;
		}
		if (StringUtils.isEmpty(pathInfoSelectors)) {
			pathInfoSelectors = selectors;
		}
		if (StringUtils.isEmpty(pathInfoSuffix)) {
			pathInfoSuffix = AEMLABConstants.FORWARD_SLASH + suffix;
		}

		// Apply Sling Mappings
		String rewrittenLinkPath = resolver.map(resourcePath);
		// Add selectors, extension, suffix, anchor
		rewrittenLinkPath = buildLink(pathInfoSelectors, pathInfoExtension, pathInfoSuffix, rewrittenLinkPath);

		if (originalUri != null) {
			// enrich with URL params and fragment
			URI rewrittenUri;
			try {
				rewrittenUri = new URI(null, null, rewrittenLinkPath, originalUri.getQuery(), anchor);
				return rewrittenUri.toString();
			} catch (URISyntaxException e) {
				LOGGER.error(
						"Could not generate URI from parameters path '{}', query '{}' and fragment '{}', "
								+ "returning  just rewritten link path '{}'",
						rewrittenLinkPath, originalUri.getQuery(), anchor, rewrittenLinkPath);
			}

		}
		// optionally extend with anchor
		if (!StringUtils.isEmpty(anchor)) {
			rewrittenLinkPath = rewrittenLinkPath + AEMLABConstants.HASH + anchor;
		}
		return rewrittenLinkPath;
	}

	protected PathInfo getPathInfo(ResourceResolver resolver, String linkPath) {
		return new PathInfo(resolver, linkPath);
	}

	private String buildLink(String selectors, String extension, String suffix, String resourcePath) {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(resourcePath);
		if (StringUtils.isNotEmpty(selectors)) {
			stringBuilder.append(AEMLABConstants.DOT);
			stringBuilder.append(selectors);
		}
		if (StringUtils.isNotEmpty(extension)) {
			stringBuilder.append(AEMLABConstants.DOT);
			stringBuilder.append(extension);
		} else {
			stringBuilder.append(AEMLABConstants.EXTENSION_HTML);
		}
		if (StringUtils.isNotEmpty(suffix)) {
			stringBuilder.append(suffix);
		}
		return stringBuilder.toString();
	}

	private boolean isContentLink(String link) {
		return !isExternal(link) && !isDamLink(link);
	}

	private boolean isRewriteLinkRequired(String link) {
		return StringUtils.isNotEmpty(link) && StringUtils.startsWith(link, AEMLABConstants.FORWARD_SLASH)
				&& !StringUtils.startsWith(link, AEMLABConstants.DOUBLE_SLASH);
	}

	private boolean isAnchorOnlyLink(String link, String anchor) {
		return (StringUtils.isEmpty(link) || StringUtils.equals(link, AEMLABConstants.HASH))
				&& StringUtils.isNotEmpty(anchor);
	}
}
