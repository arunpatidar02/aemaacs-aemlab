package com.community.aemlab.core.services.transformer;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.rewriter.ProcessingComponentConfiguration;
import org.apache.sling.rewriter.ProcessingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

import com.adobe.acs.commons.rewriter.ContentHandlerBasedTransformer;
import com.community.aemlab.core.utils.ShortenLinkUtil;
import com.day.cq.wcm.api.WCMMode;

/**
 * Linkrewriter for link shortening
 */
public class ShortenLinkRewriterTransformer extends ContentHandlerBasedTransformer {

  private static final Logger LOGGER = LoggerFactory.getLogger(ShortenLinkRewriterTransformer.class);
  private SlingHttpServletRequest slingRequest;
  private final Map<String, String[]> attributes;
  private int numberOfPathSegmentsToCutOff;
  private Map<String, Integer> customShortenLinks;
  private final List<Pattern> whitelistedPathPatterns;
  private WCMMode wcmMode;
  
  public static final String NUMBER_OF_PATH_SEGMENTS_TO_CUT_OFF_ATTRIBUTE_NAME_PREFIX = "no-of-path-segments-to-cut-off-";


  /**
   * Constructor
   *
   *
   * @param attributes Map of elements and attributes to rewrite
   * @param matcherList list of regular expressions
   * @param isPublishInstance whether it is a publish instance
   */
  public ShortenLinkRewriterTransformer(Map<String, String[]> attributes, List<Pattern> whitelistedPathPatterns) {
    LOGGER.debug("LinkRewriterTransformer created.");
    this.attributes = attributes;
    this.whitelistedPathPatterns = whitelistedPathPatterns;
  }

  /**
   * Initialization method
   */
  @Override
  public void init(final ProcessingContext context, final ProcessingComponentConfiguration config) throws IOException {
    super.init(context, config);
    this.slingRequest = context.getRequest();
    this.numberOfPathSegmentsToCutOff = ShortenLinkUtil.getNumberOfPathSegmentsToCutOff(slingRequest);
    this.customShortenLinks = ShortenLinkUtil.getCustomShortenLinksWithNumberOfPathSegmentsToCutOff(slingRequest);
    wcmMode = WCMMode.fromRequest(context.getRequest());
  }

  @Override
  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
    try {
      if (wcmMode == WCMMode.DISABLED) {
        getContentHandler().startElement(namespaceURI, localName, qName, rebuildAttributes(qName, atts));
      }
      else {
        LOGGER.debug("Skip link shortening transformer is disabled due to wcmmode = {}!", wcmMode);
        getContentHandler().startElement(namespaceURI, localName, qName, atts);
      }
    }
    catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    }
  }

  /**
   * Rewrites the configured attributes
   *
   * @param elementName String
   * @param saxAttributes Attributes
   * @return the cloned attributes
   */
  private Attributes rebuildAttributes(final String elementName, final Attributes saxAttributes) {
    if (slingRequest == null || !attributes.containsKey(elementName)) {
      // element is not defined as a candidate to rewrite
      return saxAttributes;
    }

    // 1. get configured attributes for the given element
    final String[] attributeNamesToModify = attributes.get(elementName);

    // 2. clone the attributes
    final AttributesImpl modifiableAttributes = new AttributesImpl(saxAttributes);

    // 4. rewrite the configured attributes
    for (String attributeName : attributeNamesToModify) {
      int attributeIndex = modifiableAttributes.getIndex(attributeName);
      if (attributeIndex >= 0) {
        String attributeValue = modifiableAttributes.getValue(attributeIndex);
        if (shouldProcessPath(attributeValue)) {
          rewriteAttribute(elementName, modifiableAttributes, attributeName, attributeIndex, attributeValue);
        }
      }
    }
    return modifiableAttributes;
  }

  private void rewriteAttribute(final String elementName, final AttributesImpl modifiableAttributes, String attributeName,
      int attributeIndex, String attrValue) {
    // is there an additional link attribute which overwrites the shortening value from the request?
    String numberOfPathSegmentsToCutOffAttributeName = NUMBER_OF_PATH_SEGMENTS_TO_CUT_OFF_ATTRIBUTE_NAME_PREFIX + attributeName;
    int indexOfCutOffAttribute = modifiableAttributes.getIndex(numberOfPathSegmentsToCutOffAttributeName);
    int effectiveNumberOfPathSegmentsToCutOff = this.numberOfPathSegmentsToCutOff;
    if (indexOfCutOffAttribute != -1) {
      String localNumberOfPathSegmentsToCutOff = modifiableAttributes.getValue(indexOfCutOffAttribute);
      if (localNumberOfPathSegmentsToCutOff != null) {
        try {
          effectiveNumberOfPathSegmentsToCutOff = Integer.parseInt(localNumberOfPathSegmentsToCutOff);
          LOGGER.debug(
              "Found local attribute '{}' in element '{}' which overwrites the request based link shortening " + "with value '{}'",
              numberOfPathSegmentsToCutOffAttributeName, elementName, effectiveNumberOfPathSegmentsToCutOff);
        }
        catch (NumberFormatException e) {
          LOGGER.warn("The local attribute '{}' in element '{}' is supposed to contain an integer value but cannot be "
              + "converted to an integer", numberOfPathSegmentsToCutOffAttributeName, elementName, e);
        }
      }
    }
    if (effectiveNumberOfPathSegmentsToCutOff < 1) {
      LOGGER.debug("Do not shorten link as neither request header is found nor a local attribute!");
    }
    else {
      String rewrittenAttrValue = ShortenLinkUtil.shortenLink(attrValue, effectiveNumberOfPathSegmentsToCutOff,
          this.customShortenLinks);
      // set the new link value the the attribute
      modifiableAttributes.setValue(attributeIndex, rewrittenAttrValue);
      if (LOGGER.isDebugEnabled() && !StringUtils.equals(attrValue, rewrittenAttrValue)) {
        LOGGER.debug("Attribute rewritten: <{} {}=\"{}\">", elementName, attributeName, rewrittenAttrValue);
      }
    }
    if (indexOfCutOffAttribute >= 0) {
      // remove cut-off attribute
      modifiableAttributes.removeAttribute(indexOfCutOffAttribute);
    }
  }

  private boolean shouldProcessPath(String path) {
    return matchesOneOf(path, whitelistedPathPatterns);
  }

  public static boolean matchesOneOf(String value, Iterable<Pattern> patterns)  {
    for (Pattern pattern : patterns) {
      if (pattern.matcher(value).matches()) {
        return true;
      }
    }
    return false;
  }
}
