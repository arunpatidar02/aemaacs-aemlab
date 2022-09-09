
package com.community.aemlab.oneweb.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;

import com.community.aemlab.oneweb.core.conf.GoToTopCaConfig;
import com.community.aemlab.oneweb.core.services.CAConfigurationService;
import com.day.cq.wcm.api.Page;

@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class GoToTop {

	@Inject
	private CAConfigurationService configService;

	@Inject
	private Page currentPage;

	@Inject
	@Named("log")
	private Logger logger;

	private boolean enabled;
	private String title;
	private boolean isConfig;

	@PostConstruct
	protected void init() {
		logger.debug("Processing data for {}", currentPage.getPath());

		GoToTopCaConfig caConfig = configService.getConfiguration(currentPage, GoToTopCaConfig.class);
		if (caConfig != null) {
			enabled = caConfig.isEnabled();
			title = caConfig.title();
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
