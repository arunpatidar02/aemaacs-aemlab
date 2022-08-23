package com.community.aemlab.core.services.feature.sample;

import org.apache.jackrabbit.oak.spi.security.user.UserConstants;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.featureflags.ExecutionContext;
import org.apache.sling.featureflags.Feature;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.xf.ExperienceFragmentsConstants;

/**
 * @author arunpatidar02
 *
 */
@Component(service = Feature.class, immediate = true, name = "Sample Feature Flag")
public class SampleFeatureFlag implements Feature {

	private static final Logger LOGGER = LoggerFactory.getLogger(SampleFeatureFlag.class);

	@Reference
	private ResourceResolverFactory resourceFactory;

	@Override
	public String getDescription() {
		return "aemlab-custom-feature, This is sample feature flag";
	}

	@Override
	public String getName() {
		return "aemlab-custom-feature";
	}

	@Override
	public boolean isEnabled(ExecutionContext ec) {

		// if user is admin, return true to available feature for admin or if path is
		// XF, return true to available feature
		if (ec.getResourceResolver().getUserID().equalsIgnoreCase(UserConstants.DEFAULT_ADMIN_ID)
				|| ec.getRequest().getPathInfo().contains(ExperienceFragmentsConstants.CONTENT_PATH)) {
			LOGGER.trace("Flag is true");
			return true;
		}

		return false;
	}

}