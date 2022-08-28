package com.community.aemlab.core.conf;

import static com.community.aemlab.core.utils.AEMLABConstants.CONTENT_XF_AEMLAB_ROOT;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

/**
 * Configuration definition for header and footer elements
 *
 * @author arupatidar02
 */
@Configuration(label = "AEMLAB - Header and Footer Configuration", 
				description = "Template configuration for header and Footer", 
				name = "template/headerfooter")

public @interface HeaderFooterConfiguration {

	/**
	 * @return Path parameter
	 */
	@Property(label = "Header", description = "Browse path to experience fragment for header", order = 1, property = {
			"widgetType=pathbrowser", "pathbrowserRootPath=" + CONTENT_XF_AEMLAB_ROOT })
	String headerPath();

	/**
	 * @return Path parameter
	 */
	@Property(label = "Footer", description = "Browse path to experience fragment for footer", order = 2, property = {
			"widgetType=pathbrowser", "pathbrowserRootPath=" + CONTENT_XF_AEMLAB_ROOT })
	String footerPath();

}
