package com.community.aemlab.core.conf;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

/**
 * CAC definition for Site configurations
 * 
 * @author arunpatidar02
 *
 */
@Configuration(label = "AEMLAB - Site Configuration",
    description = "Optional configuration for URL rewriting")
public @interface SiteConfiguration {

  @Property(label = "Publish Domain",
      description = "The name of the domain on the publisher to be used to externalizing links ")
  String publishDomain();

  @Property(label = "Use fully qualified URLs for links", 
      description = "If true, AEM-internal links in sites connected with the CAC will be rendered with fully qualified URLs ")
  boolean useFullyQualifiedURLsInLinks();

  @Property(label = "Number of segments to cut off", 
      description = "This must match the dispatcher site configuration, i.e. 3 for single-language sites and "
          + "4 for multi-language sites. Only considered if 'Use fully qualified URLs for links' is enabled on the source page, "
          + "otherwise regular link shortening (as defined by the request header) is active.")
  int numberOfSegmentsToCutOff();

}
