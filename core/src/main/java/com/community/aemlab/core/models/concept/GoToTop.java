
package com.community.aemlab.core.models.concept;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.caconfig.management.ConfigurationData;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;

import com.community.aemlab.core.conf.GoToTopCaConfig;
import com.community.aemlab.core.services.CAConfigurationService;
import com.day.cq.wcm.api.Page;

@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class GoToTop {

	@Inject
	private CAConfigurationService configService;

	@Inject
	private Page currentPage;

	GoToTopCaConfig caConfig;

	@Inject
	@Named("log")
	private Logger logger;

	private boolean enabled;
	private String title;
	private boolean isConfig;

	private static final String ENABLE_PROP = "isEnabled";
	private static final String TITLE_PROP = "title";

	@PostConstruct
	protected void init() {
		logger.debug("Processing data for {}", currentPage.getPath());

		ConfigurationData configData = configService.getConfigurationData(currentPage.adaptTo(Resource.class),
				GoToTopCaConfig.class.getName());
		if (configData != null) {
			ValueMap vm = configData.getEffectiveValues();
			if (vm.isEmpty()) {
				return;
			}
			enabled = vm.get(ENABLE_PROP, Boolean.class);
			title = vm.get(TITLE_PROP, String.class);
			isConfig = true;
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isConfig() {
		return isConfig;
	}

	public String getTitle() {
		return title;
	}

}
