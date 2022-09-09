
package com.community.aemlab.core.models.sample;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
//import org.apache.sling.models.caconfig.annotations.ContextAwareConfiguration;
import org.slf4j.Logger;

import com.community.aemlab.core.conf.GoToTopCaConfig;
import com.community.aemlab.core.services.CAConfigurationService;
import com.day.cq.wcm.api.Page;

/**
 * @author arunpatidar02
 * 
 * Not in used, created only for @ContextAwareConfiguration Syntax
 *
 */
@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CaConfigModel {

//	@ContextAwareConfiguration
//	private GoToTopCaConfig caConfig;

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

		// This will be removed due to line 30
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
