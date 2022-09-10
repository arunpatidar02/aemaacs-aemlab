package com.community.aemlab.oneweb.core.conf;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

/**
 * Configuration definition for Go To Top button
 *
 * @author arupatidar02
 */
@Configuration(label = "AEMLAB - Go To Top button Configuration", description = "AEMLAB - Go To Top button Configuration")

public @interface GoToTopCaConfig {

	/**
	 * @return boolean isEnabled
	 */
	@Property(label = "Enabled", description = "Enable or Disable Go To Top Button", order = 1)
	boolean isEnabled();

	/**
	 * @return String emptyTitle
	 */
	@Property(label = "Title", description = "No configuration message for Authors", order = 2)
	String title();

}
