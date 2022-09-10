package com.community.aemlab.core.services.transformer;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.oak.commons.PropertiesUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.rewriter.ProcessingComponentConfiguration;
import org.apache.sling.rewriter.ProcessingContext;
import org.apache.sling.rewriter.Transformer;
import org.apache.sling.rewriter.TransformerFactory;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

import com.adobe.acs.commons.rewriter.ContentHandlerBasedTransformer;
import com.adobe.acs.commons.util.ParameterUtil;
import com.community.aemlab.core.services.LinkService;

/**
 *
 * Rewriter pipeline which rewrites href links applying the sling mappings and
 * adding the .html extension
 *
 */
@Component(service = TransformerFactory.class, property = {
		Constants.SERVICE_DESCRIPTION + "=" + LinkRewriterTransformerFactory.SERVICE,
		"pipeline.type=linkrewriter" }, immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = LinkRewriterTransformerFactory.Config.class)
public final class LinkRewriterTransformerFactory implements TransformerFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(LinkRewriterTransformerFactory.class);
	public static final String SERVICE = "AEMLAB - Link Rewriter Transformer Factory";

	private static final String[] DEFAULT_ATTRIBUTES = new String[] { "a:href" };

	@ObjectClassDefinition(name = "LinkRewriterTransformer Factory", description = "Rewriter pipeline component which rewrites href links applying the sling mappings and "
			+ "adding the .html extension")
	public @interface Config {
		@AttributeDefinition(name = "Rewrite Attributes", description = "List of element/attribute pairs to rewrite", defaultValue = "a:href")
		String[] getAttributes();
	}

	protected enum LinkParameter {
		ANCHOR("data-link-anchor"), SELECTOR("data-link-selector"), EXTENSION("data-link-extension"),
		SUFFIX("data-link-suffix");

		private String parameter;

		LinkParameter(String parameter) {
			this.parameter = parameter;
		}

		public String getParameter() {
			return this.parameter;
		}

		@Override
		public String toString() {
			return this.parameter;
		}

		public static LinkParameter getLinkParameter(String name) {
			LinkParameter[] linkParameters = LinkParameter.values();
			for (LinkParameter p : linkParameters) {
				if (p.getParameter().equals(name)) {
					return p;
				}
			}
			return null;
		}
	}

	private Map<String, String[]> attributes;

	@Reference
	LinkService linkService;

	@Override
	public Transformer createTransformer() {
		return new LinkRewriterTransformer();
	}

	@Activate
	protected void activate(final Config config) {
		final String[] attrProp = PropertiesUtil.toStringArray(config.getAttributes(), DEFAULT_ATTRIBUTES);
		this.attributes = ParameterUtil.toMap(attrProp, ":", ";");
		LOGGER.debug("LinkRewriterTransformer");
	}

	private final class LinkRewriterTransformer extends ContentHandlerBasedTransformer {

		private SlingHttpServletRequest slingRequest;

		@Override
		public void init(ProcessingContext context, ProcessingComponentConfiguration config) throws IOException {
			super.init(context, config);
			this.slingRequest = context.getRequest();
		}

		@Override
		public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
			try {
				getContentHandler().startElement(namespaceURI, localName, qName, rebuildAttributes(localName, atts));
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}

		/**
		 * Rewrites the given element configured attributes
		 *
		 * @param elementName
		 * @param saxAttributes
		 * @return
		 */
		private Attributes rebuildAttributes(final String elementName, final Attributes saxAttributes) {
			if (slingRequest == null || !attributes.containsKey(elementName)) {
				// element is not defined as a candidate to rewrite
				return saxAttributes;
			}

			// 1. get configured attributes for the given element
			final String[] modifiableAttributes = attributes.get(elementName);

			// 2. clone the attributes
			final AttributesImpl newSaxAttributes = new AttributesImpl(saxAttributes);

			// 3. read additional link parameters
			Parameters additionalParameters = new Parameters(newSaxAttributes);

			// 4. rewrite the configured attributes
			for (int i = 0; i < newSaxAttributes.getLength(); i++) {
				final String attrName = newSaxAttributes.getLocalName(i);
				if (ArrayUtils.contains(modifiableAttributes, attrName)) {
					String attrValue = newSaxAttributes.getValue(i);
					// rewrite the link
					String rewrittenAttrValue = linkService.rewriteRelativeLink(this.slingRequest.getResourceResolver(),
							attrValue, additionalParameters.getSelector(), additionalParameters.getExtension(),
							additionalParameters.getSuffix(), additionalParameters.getAnchor());

					// set the new link value the the attribute
					newSaxAttributes.setValue(i, rewrittenAttrValue);

					if (LOGGER.isDebugEnabled() && !StringUtils.equals(attrValue, rewrittenAttrValue)) {
						LOGGER.debug("Attribute rewritten: <{} {}=\"{}\">", elementName, attrName, rewrittenAttrValue);
						LOGGER.trace("old value: {}", attrValue);
					}
				}
			}

			return newSaxAttributes;
		}
	}

	/**
	 * Class encapsulating the data- parameters
	 */
	private static final class Parameters {
		private String anchor = null;
		private String selector = null;
		private String extension = null;
		private String suffix = null;

		public Parameters(AttributesImpl newSaxAttributes) {
			// 1. init
			for (int i = 0; i < newSaxAttributes.getLength(); i++) {
				final LinkParameter attrName = LinkParameter.getLinkParameter(newSaxAttributes.getLocalName(i));
				if (attrName != null) {
					switch (attrName) {
					case ANCHOR:
						this.anchor = newSaxAttributes.getValue(i);
						break;
					case SELECTOR:
						this.selector = newSaxAttributes.getValue(i);
						break;
					case EXTENSION:
						this.extension = newSaxAttributes.getValue(i);
						break;
					case SUFFIX:
						this.suffix = newSaxAttributes.getValue(i);
						break;
					}
				}
			}

			// 2. delete additional link parameters. Only one parameter can be deleted by
			// iteration.
			// That's the reason for the 2 for loops
			LinkParameter[] linkParameters = LinkParameter.values();
			for (LinkParameter p : linkParameters) {
				for (int i = 0; i < newSaxAttributes.getLength(); i++) {
					final String attrName = newSaxAttributes.getLocalName(i);
					if (p.getParameter().equals(attrName)) {
						newSaxAttributes.removeAttribute(i);
						break;
					}
				}
			}
		}

		public String getAnchor() {
			return anchor;
		}

		public String getSelector() {
			return selector;
		}

		public String getExtension() {
			return extension;
		}

		public String getSuffix() {
			return suffix;
		}
	}
}
