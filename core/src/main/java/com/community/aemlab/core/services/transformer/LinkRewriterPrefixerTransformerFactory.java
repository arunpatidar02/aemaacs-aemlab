package com.community.aemlab.core.services.transformer;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.rewriter.ProcessingComponentConfiguration;
import org.apache.sling.rewriter.ProcessingContext;
import org.apache.sling.rewriter.Transformer;
import org.apache.sling.rewriter.TransformerFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.adobe.acs.commons.rewriter.ContentHandlerBasedTransformer;
import com.community.aemlab.core.conf.SiteConfiguration;
import com.community.aemlab.core.utils.AEMLABConstants;
import com.community.aemlab.core.utils.PathHelper;
import com.community.aemlab.oneweb.core.services.CAConfigurationService;
import com.community.aemlab.oneweb.core.services.EnvironmentTypeProvider;
import com.community.aemlab.oneweb.core.services.impl.CAConfigurationServiceImpl;
import com.day.text.Text;

/**
 * This transformer factory is supposed to be called twice. One time prior to
 * any mapping (i.e. with the raw repository path still in the target) and a 2nd
 * time after the mapping/linkchecking.
 *
 */
@Component(service = TransformerFactory.class, property = { "pipeline.type=linkrewriterprefixer" }, immediate = true)
public class LinkRewriterPrefixerTransformerFactory implements TransformerFactory {

	@Reference
	private ShortenLinkRewriterTransformerFactoryConfig shortenLinkRewriterTransformerFactoryConfig;

	@Reference
	private EnvironmentTypeProvider envType;

	@Reference
	private CAConfigurationService configService;

	@Override
	public Transformer createTransformer() {
		return new LinkRewriterPrefixerTransformer(
				shortenLinkRewriterTransformerFactoryConfig.getElementsAndAttributesToRewrite(), !envType.isAuthor());
	}

	public static final class LinkRewriterPrefixerTransformer extends ContentHandlerBasedTransformer {
		/**
		 * if set to true, this link rewriter will not touch the actual href yet, but
		 * only give an additional artificial link attribute, which is later on removed
		 * by the same link rewriter
		 */
		boolean isPreprocessOnly = false;
		final Map<String, String[]> elementsAndAttributesToRewrite;
		ResourceResolver resourceResolver;
		boolean isEnabled;
		private static final String PN_PREPROCESS = "preprocess";

		static final String URL_PREFIX_ATTRIBUTE_NAME_PREFIX = "url-prefix-";

		private static final Logger LOGGER = LoggerFactory.getLogger(LinkRewriterPrefixerTransformer.class);

		final boolean isPublish;

		public LinkRewriterPrefixerTransformer(Map<String, String[]> elementsAndAttributesToRewrite,
				boolean isPublish) {
			super();
			this.elementsAndAttributesToRewrite = elementsAndAttributesToRewrite;
			this.isPublish = isPublish;
		}

		@Override
		public void init(ProcessingContext context, ProcessingComponentConfiguration config) throws IOException {
			if (config.getConfiguration().containsKey(PN_PREPROCESS)) {
				LOGGER.debug("LinkRewriterPrefixerTransformer called with option 'preprocess'");
				isPreprocessOnly = true;
			} else {
				LOGGER.debug("LinkRewriterPrefixerTransformer called without option 'preprocess'");
			}
			resourceResolver = context.getRequest().getResourceResolver();
			Resource resource = getResourceForCacLookup(context);
			SiteConfiguration currentSiteConfiguration = getSiteConfigurationForResource(resource);
			if (currentSiteConfiguration != null && currentSiteConfiguration.useFullyQualifiedURLsInLinks()) {
				LOGGER.debug("Enable link prefixing as the site configuration linked to {} enables that feature",
						context.getRequest().getResource().getPath());
				isEnabled = true;
			} else {
				LOGGER.debug("Disable link prefixing as the site configuration linked to {} is either not there "
						+ "or has disabled that feature", context.getRequest().getResource().getPath());
				isEnabled = false;
			}
			super.init(context, config);
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes)
				throws SAXException {
			if (!isPublish) {
				LOGGER.debug("Skip prefixer rewriter functionality as this is not a publish instance!");
				super.startElement(uri, localName, qName, attributes);
				return;
			}
			if (!isEnabled) {
				LOGGER.debug(
						"Skip prefixer rewriter functionality as the feature is not enabled on the current site's CAC!");
				super.startElement(uri, localName, qName, attributes);
				return;
			}
			final Attributes newAttributes;
			if (isPreprocessOnly) {
				newAttributes = preprocessLinkForElement(qName, attributes);
			} else {
				newAttributes = rewriteLinkAccordingToPreprocessedAttributes(attributes);
			}
			super.startElement(uri, localName, qName, newAttributes);
		}

		Attributes preprocessLinkForElement(String qName, Attributes attributes) {
			Attributes newAttributes = attributes;
			String[] attributeNames = getRewriteAttributeNames(qName);
			if (attributeNames != null && attributeNames.length > 0) {
				Map<String, String> newAttributesMap = new LinkedHashMap<>();
				// theoretically multiple attributes per element could get rewritten
				for (String attributeName : attributeNames) {
					// get original link value
					String link = attributes.getValue(attributeName);
					if (link == null) {
						continue;
					}
					SiteConfiguration siteConfiguration = getSiteConfigurationForLink(resourceResolver, link);
					if (siteConfiguration != null && siteConfiguration.publishDomain() != null) {
						newAttributesMap.put(URL_PREFIX_ATTRIBUTE_NAME_PREFIX + attributeName,
								siteConfiguration.publishDomain());

						// regular link shortening needs to be overwritten as this potentially works
						// differently for other sites!
						newAttributesMap.put(
								ShortenLinkRewriterTransformer.NUMBER_OF_PATH_SEGMENTS_TO_CUT_OFF_ATTRIBUTE_NAME_PREFIX
										+ attributeName,
								Integer.toString(siteConfiguration.numberOfSegmentsToCutOff()));

						LOGGER.debug(
								"Preprocessing: Adding additional attributes '{}{}' to link '{}' with value '{}' "
										+ "and '{}{}' to value '{}' due to prefixing enabled",
								URL_PREFIX_ATTRIBUTE_NAME_PREFIX, attributeName, link,
								siteConfiguration.publishDomain(),
								ShortenLinkRewriterTransformer.NUMBER_OF_PATH_SEGMENTS_TO_CUT_OFF_ATTRIBUTE_NAME_PREFIX,
								attributeName, siteConfiguration.numberOfSegmentsToCutOff());
					}
				}
				if (!newAttributesMap.isEmpty()) {
					newAttributes = rebuildAttributes(attributes, newAttributesMap);
				}
			}
			return newAttributes;
		}

		Resource getResourceForCacLookup(ProcessingContext context) {
			SlingHttpServletRequest request = context.getRequest();
			Resource resource = request.getResource();
			return getResourceForCacLookup(resource);
		}

		/**
		 * Returns either the passed Resource or, if resource is an experience fragment
		 * (XF), the homepage resource of the respective site the XF belongs to, in
		 * order to use it for subsequent CAC lookup.
		 * 
		 * @param resource subject for the check
		 * @return either resource from context or homepage resource
		 */
		private static Resource getResourceForCacLookup(Resource resource) {
			String resourcePath = resource.getPath();

			if (resourcePath.startsWith(AEMLABConstants.AEMLAB_CONTENT_XF_ROOT)) {
				PathHelper pathHelper = new PathHelper(resource.getPath());
				String pathForCacLookup = pathHelper.getLanguageRootPath();
				if (!pathForCacLookup.isEmpty()) {
					ResourceResolver resolver = resource.getResourceResolver();
					LOGGER.debug("deduced homepage path as: {}", pathForCacLookup);
					Resource resourceForCacLookup = resolver.getResource(pathForCacLookup);
					if (resourceForCacLookup != null) {
						LOGGER.debug("found cacLookupResource: {}", resourceForCacLookup.getPath());
						return resourceForCacLookup;
					}
				}
			}
			LOGGER.debug("returning resource from context: {}", resource.getPath());
			return resource;
		}

		/**
		 *
		 * @param resourceResolver
		 * @param link
		 * @return a SiteConfiguration only in case it exists otherwise {@code null}.
		 */
		public static SiteConfiguration getSiteConfigurationForLink(ResourceResolver resourceResolver, String link) {
			// only consider links which start with "/"
			if (!link.startsWith("/")) {
				return null;
			}

			// use access rights of the current user
			Resource linkedResource;
			if (link.startsWith(AEMLABConstants.DAM_PATH_PREFIX)) {
				String siteLink = "/content/" + link.substring(AEMLABConstants.DAM_PATH_PREFIX.length());
				// look up site site CAC from site level
				siteLink = Text.getAbsoluteParent(siteLink, AEMLABConstants.LEVEL_SITE_ROOT);
				linkedResource = resourceResolver.resolve(siteLink);
				if (ResourceUtil.isNonExistingResource(linkedResource)) {
					LOGGER.debug("Could not find site level resource '{}' for dam path '{}'", link, siteLink);
					// fall back to tenant level
					siteLink = Text.getAbsoluteParent(link, AEMLABConstants.LEVEL_TENANT_ROOT);
					linkedResource = resourceResolver.resolve(siteLink);
				}
			} else {
				linkedResource = resourceResolver.resolve(link);
			}

			if (ResourceUtil.isNonExistingResource(linkedResource)) {
				LOGGER.debug("Could not find linked resource '{}'", link);
				return null;
			}
			Resource linkedResorceForCacLookup = getResourceForCacLookup(linkedResource);
			return getSiteConfigurationForResource(linkedResorceForCacLookup);
		}

		/**
		 *
		 * @param resource
		 * @return a SiteConfiguration only in case it exists otherwise {@code null}.
		 */
		public static SiteConfiguration getSiteConfigurationForResource(Resource resource) {
			CAConfigurationServiceImpl cacServiceImpl = new CAConfigurationServiceImpl();
			return cacServiceImpl.getConfiguration(resource, SiteConfiguration.class);
		}

		Attributes rewriteLinkAccordingToPreprocessedAttributes(Attributes attributes) {
			if (attributes.getLength() < 1) {
				return attributes;
			}
			// check only last attribute (should be pretty fast)
			if (!isPrefixAttribute(attributes, attributes.getLength() - 1)) {
				// return early, because this has obviously not be treated by the preprocess
				// step
				return attributes;
			}
			AttributesImpl modifiableAttributes = new AttributesImpl(attributes);
			rewritePreprocessedLinkForAttribute(modifiableAttributes, modifiableAttributes.getLength() - 1);

			int index = 0;
			// now process all remaing attributes
			while (index < modifiableAttributes.getLength()) {
				if (isPrefixAttribute(modifiableAttributes, index)) {
					rewritePreprocessedLinkForAttribute(modifiableAttributes, index);
				} else {
					index++;
				}
			}
			return modifiableAttributes;
		}

		boolean isPrefixAttribute(Attributes attributes, int index) {
			String attributeName = attributes.getQName(index);
			if (attributeName == null) {
				throw new IllegalArgumentException("Could not get attribute with index " + index);
			}
			return attributeName.startsWith(URL_PREFIX_ATTRIBUTE_NAME_PREFIX);
		}

		/**
		 *
		 * @param attributes the attributes to rewrite
		 * @param index      the index of the prefix attribute!
		 */
		void rewritePreprocessedLinkForAttribute(AttributesImpl attributes, int index) {
			// get the according attribute without the prefix
			String prefixedLinkAttributeName = attributes.getQName(index);

			// verify the prefix
			if (!prefixedLinkAttributeName.startsWith(URL_PREFIX_ATTRIBUTE_NAME_PREFIX)) {
				throw new IllegalStateException("Given attribute does not have a name starting with "
						+ URL_PREFIX_ATTRIBUTE_NAME_PREFIX + " but has name " + prefixedLinkAttributeName);
			}
			String linkAttributeName = prefixedLinkAttributeName.substring(URL_PREFIX_ATTRIBUTE_NAME_PREFIX.length());

			String prefix = attributes.getValue(index);
			// find attribute to rewrite
			String link = attributes.getValue(linkAttributeName);
			if (link == null) {
				throw new IllegalStateException("Could not find link attribute with name '" + linkAttributeName
						+ "' to which the prefix attribute '" + prefixedLinkAttributeName + "' is referring to!");
			}

			// rewrite
			attributes.setValue(attributes.getIndex(linkAttributeName), prefix + link);

			// finally remove prefix attributes created during preprocess
			attributes.removeAttribute(index);
			int indexOfCutOffAttribute = attributes
					.getIndex(ShortenLinkRewriterTransformer.NUMBER_OF_PATH_SEGMENTS_TO_CUT_OFF_ATTRIBUTE_NAME_PREFIX
							+ linkAttributeName);
			if (indexOfCutOffAttribute >= 0) {
				attributes.removeAttribute(indexOfCutOffAttribute);
			}
			LOGGER.debug("Enriching link {} with prefix {} (prefix determined in the preprocess phase)", link, prefix);

		}

		/**
		 * Creates a new set of attributes based on existing (read-only) attributes and
		 * a map with new/modified attributes. The added ones are the last ones in the
		 * list.
		 *
		 * @return the new attributes
		 */
		private AttributesImpl rebuildAttributes(Attributes attributes, Map<String, String> newAttributeMap) {
			final AttributesImpl modifiableAttributes = new AttributesImpl(attributes);

			for (Map.Entry<String, String> newAttribute : newAttributeMap.entrySet()) {
				int attributeIndex = modifiableAttributes.getIndex(newAttribute.getKey());
				if (attributeIndex == -1) {
					modifiableAttributes.addAttribute("", "", newAttribute.getKey(), "CDATA", newAttribute.getValue());
				} else {
					modifiableAttributes.setValue(attributeIndex, newAttribute.getValue());
				}
			}
			return modifiableAttributes;
		}

		private String[] getRewriteAttributeNames(String elementName) {
			return elementsAndAttributesToRewrite.get(elementName.toLowerCase());
		}

	}

}
