package com.community.aemlab.core.services.transformer;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public interface ShortenLinkRewriterTransformerFactoryConfig {

  /**
   * 
   * @return a map with keys = element names and values = attribute names. Only within those
   * elements/attributes the link should be rewritten.
   */
  Map<String, String[]> getElementsAndAttributesToRewrite();

  /**
   * 
   * @return the whitelisted path patterns. Only matching links are rewritten!
   */
  List<Pattern> getPathPatterns();
}
