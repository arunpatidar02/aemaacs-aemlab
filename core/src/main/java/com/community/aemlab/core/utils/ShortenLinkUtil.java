package com.community.aemlab.core.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.acs.commons.util.ParameterUtil;

/**
 * Helper class for link shortening
 *
 */
public class ShortenLinkUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(ShortenLinkUtil.class);

  public static final String SHORTEN_LINKS_HEADER_NAME = "Shorten-Links";
  public static final String CUSTOM_SHORTEN_LINKS_HEADER_NAME = "Custom-Shorten-Links";
  private static final String ROOTPAGE_PREFIX_USED_WITH_SELECTORS = "rootpage";

  /**
   * Hide the constructor
   */
  private ShortenLinkUtil() {
  }

  /**
   * This method removes the segments from the passed path
   *
   * @param path the long path
   * @param request the request from which a certain header is evaluated to parameterize how exactly the link is shortened
   * @return the modified path
   */
  public static String shortenLink(String path, final SlingHttpServletRequest request) {
    int numberOfSegmentsToCutOff = getNumberOfPathSegmentsToCutOff(request);
    Map<String, Integer> customShortenLinks = getCustomShortenLinksWithNumberOfPathSegmentsToCutOff(request);
    return shortenLink(path, numberOfSegmentsToCutOff, customShortenLinks);
  }

  /**
   * rewrites attribute value with numberOfPathSegmentsToCutOff. If map customShortenLinks is set and key matches beginning of
   * attribute value, then the value of this key-value pair is used for rewriting.
   *
   * @param link the link.
   * @param numberOfPathSegmentsToCutOff number of path segments to strip off from the given link
   * @param customShortenLink map with the path and the number of path segments to be cut off. Takes precedence of {@link numberOfPathSegmentsToCutOff}
   * @return the rewritten link
   */
  public static String shortenLink(String link, int numberOfPathSegmentsToCutOff, Map<String, Integer> customShortenLinks) {
    for ( Map.Entry<String, Integer > entry : customShortenLinks.entrySet() ) {
      if (link.startsWith(entry.getKey())) {
        return shortenLink(link, entry.getValue());
      }
    }
    return shortenLink(link, numberOfPathSegmentsToCutOff);
  }

  /**
   * This method removes the segments from the passed path
   *
   * @param path the long path
   * @param numberOfSegments number of segments to cutoff
   * @return the modified path
   */
  public static String shortenLink(String path, final int numberOfSegments) {
    if (StringUtils.isBlank(path)) {
      LOGGER.error("The passed path is - {}", path);
      return path;
    }
    if (numberOfSegments == 0) {
      return path;
    }
    
    // find the nth slash
    int indexOfNthSlash = StringUtils.ordinalIndexOf(path, AEMLABConstants.FORWARD_SLASH, numberOfSegments+1);
    if (indexOfNthSlash == -1) {
        // could be that the last segment does not end with a slash (i.e. the homepage itself)
        int indexOfLastRelevantSlash = StringUtils.ordinalIndexOf(path, AEMLABConstants.FORWARD_SLASH, numberOfSegments);
        if (indexOfLastRelevantSlash == -1) {
            LOGGER.warn("Unable to remove {} segments from the path {} because path has fewer segments", numberOfSegments, path);
          int lastIndexOfSlash = path.lastIndexOf('/');
          String lastPartAfterSlash = path.substring(lastIndexOfSlash);
          long numberOfDotsInLastPart = lastPartAfterSlash.chars()
                                                .filter(c -> c == AEMLABConstants.DOT.charAt(0))
                                                .count();
                                                
          if (numberOfDotsInLastPart < 2) {
            return "/";
          }
          int indexOfFirstDot = path.indexOf(AEMLABConstants.DOT, lastIndexOfSlash);
          String afterSelector = path.substring(indexOfFirstDot);
          return "/" + ROOTPAGE_PREFIX_USED_WITH_SELECTORS + afterSelector;
        }
        // assume that the last segment points to the homepage
        String name = path.substring(indexOfLastRelevantSlash);
        // contains multiple "."?
        if (StringUtils.countMatches(name, AEMLABConstants.DOT) > 1) {
            // extract the selector
            String selector = "/" + ROOTPAGE_PREFIX_USED_WITH_SELECTORS + name.substring(name.indexOf('.'));
            LOGGER.trace("Shortening path {} to '{}' as the path is probably pointing to a site root and contains selectors!",
              path, selector);
            return selector;
        } else {
            LOGGER.trace("Shortening path {} to '/' as the path is probably pointing to a site root!", path);
            return "/";
        }
        
    }
    return path.substring(indexOfNthSlash);
    
  }

  /**
   * Retrieves the number of path segments form the request
   *
   * @param request the sling request
   * @return the number of path segments to be cut off (0 if nothing is set via request header)
   */
  public static int getNumberOfPathSegmentsToCutOff(final SlingHttpServletRequest request) {
    int numberToCutOff = 0;
    String shortenLinksHeaderValue = request.getHeader(SHORTEN_LINKS_HEADER_NAME);
    if (shortenLinksHeaderValue == null) {
      LOGGER.debug("Header{} is not present in request", SHORTEN_LINKS_HEADER_NAME);
    }
    else {
      try {
        numberToCutOff = Integer.parseInt(shortenLinksHeaderValue);
      }
      catch (NumberFormatException e) {
        LOGGER.warn("Invalid Header {}. The value {} is not an integer.", shortenLinksHeaderValue, SHORTEN_LINKS_HEADER_NAME);
      }
    }
    return numberToCutOff;
  }

  /**
   * Retrieves a map containing custom link shortening for certain path with number of path segments to remove from the link
   *
   * @param request the sling request
   * @return map with the path and the number of path segments to be cut off
   */
  public static Map<String, Integer> getCustomShortenLinksWithNumberOfPathSegmentsToCutOff(final SlingHttpServletRequest request) {
    String customShortenLinksHeaderValue = request.getHeader(CUSTOM_SHORTEN_LINKS_HEADER_NAME);
    if (customShortenLinksHeaderValue == null) {
      LOGGER.debug("Header{} is not present in request", CUSTOM_SHORTEN_LINKS_HEADER_NAME);
      return Collections.emptyMap();
    }
    else {
      try {
        Map<String, Integer> customPathWithNumberToCutOff = new HashMap<>();
        final String[] attrProp = customShortenLinksHeaderValue.split(";");
        Map<String, String> attrPropAsMap = ParameterUtil.toMap(attrProp, ":");
        attrPropAsMap.keySet().forEach(key -> {
          Integer shortenLinksBy = Integer.parseInt(attrPropAsMap.get(key));
          customPathWithNumberToCutOff.put(key, shortenLinksBy);
        });
        return customPathWithNumberToCutOff;
      }
      catch (NumberFormatException e) {
        LOGGER.warn("Invalid Header {}. The value {} is not an map<String,Integer>.", customShortenLinksHeaderValue,
          CUSTOM_SHORTEN_LINKS_HEADER_NAME);
        return Collections.emptyMap();
      }
    }
  }

}
