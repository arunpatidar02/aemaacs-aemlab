package com.community.aemlab.core.services;

import org.apache.sling.api.resource.ResourceResolver;

/**
 *
 * Link service for externalizing links.
 * 
 */
public interface LinkService {
  /**
   * Check if link is external
   *
   * @param linkPath
   * @return
   */
  boolean isExternal(String link);

  /**
   * Check if link is a DAM link
   *
   * @param linkPath
   * @return
   */
  boolean isDamLink(String link);

  /**
   * Rewrite a link. This method will apply the sling mappings and add the extension .html to relative page links. returns the
   * resolved link path
   *
   * @return a rewritten relative link with the extension
   */
  String rewriteRelativeLink(ResourceResolver resolver, String link);

  /**
   * Rewrite a link. This method will apply the sling mappings and add the given parameters to relative page links. returns the
   * resolved link path. The default extension is .html
   *
   * @param resolver
   * @param link
   * @param selector
   * @param extension
   * @param suffix
   * @param anchor
   * @return
   */
  String rewriteRelativeLink(ResourceResolver resolver, String link, String selector, String extension, String suffix,
      String anchor);
}
