package com.community.aemlab.core.services.transformer.sample;

import java.io.IOException;

import org.apache.sling.rewriter.ProcessingComponentConfiguration;
import org.apache.sling.rewriter.ProcessingContext;
import org.apache.sling.rewriter.Transformer;
import org.apache.sling.rewriter.TransformerFactory;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * @author arunpatidar02
 *
 */
@Component(service = TransformerFactory.class, name = "Sample - Custom Link Transformer", immediate = true, property = {
		"pipeline.type=sample-link"
		// , "pipeline.mode=global"
		// This kind(pipeline.mode=global) of transformer will be chained in every
		// pipeline configured on an AEM instance, so watch out for these two
		// consequences.
})

public class SampleLinkTransformerFactory implements Transformer, TransformerFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(SampleLinkTransformerFactory.class);

	private ContentHandler contentHandler;
	Logger log = LoggerFactory.getLogger(SampleLinkTransformerFactory.class);

	public SampleLinkTransformerFactory() {
		LOGGER.trace("customlinkchecker");
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		contentHandler.characters(ch, start, length);

	}

	public void endDocument() throws SAXException {
		contentHandler.endDocument();

	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
		contentHandler.endElement(uri, localName, qName);

	}

	public void endPrefixMapping(String prefix) throws SAXException {
		contentHandler.endPrefixMapping(prefix);

	}

	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		contentHandler.ignorableWhitespace(ch, start, length);

	}

	public void processingInstruction(String target, String data) throws SAXException {
		contentHandler.processingInstruction(target, data);

	}

	public void setDocumentLocator(Locator locator) {
		contentHandler.setDocumentLocator(locator);

	}

	public void skippedEntity(String name) throws SAXException {
		contentHandler.skippedEntity(name);

	}

	public void startDocument() throws SAXException {
		contentHandler.startDocument();

	}

	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		final AttributesImpl attributes = new AttributesImpl(atts);
		final String href = attributes.getValue("href");
		if (href != null && href.startsWith("/content/aemlab")) {
			for (int i = 0; i < attributes.getLength(); i++) {
				if ("href".equalsIgnoreCase(attributes.getQName(i))) {
					attributes.setValue(i, replaceHref(attributes.getValue("href")));
				}
			}

		}
		contentHandler.startElement(uri, localName, qName, attributes);

	}

	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		contentHandler.startPrefixMapping(prefix, uri);

	}

	public Transformer createTransformer() {
		return new SampleLinkTransformerFactory();
	}

	public void dispose() {
		LOGGER.trace("dispose");
	}

	public void init(ProcessingContext arg0, ProcessingComponentConfiguration arg1) throws IOException {
		LOGGER.trace("init");

	}

	public void setContentHandler(ContentHandler arg0) {
		this.contentHandler = arg0;

	}

	public String replaceHref(String href) {
		String newHref = href;
		if (href != null) {
			return href.replace("/content/aemlab/", "/aemlab/");
		}
		return newHref;
	}

}