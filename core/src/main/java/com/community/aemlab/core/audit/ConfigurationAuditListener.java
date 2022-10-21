package com.community.aemlab.core.audit;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.community.aemlab.core.services.ComponentEnabler;
import com.day.cq.audit.AuditLog;
import com.day.cq.audit.AuditLogEntry;

/**
 * @author arunpatidar02
 *
 */
@Component(service = ResourceChangeListener.class, immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = ConfigurationAuditListener.Config.class)
public class ConfigurationAuditListener implements ResourceChangeListener {

	@Reference
	private AuditLog auditLog;

	@Reference
	private ComponentEnabler componentEnabler;

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationAuditListener.class);

	private static final String LOOKUP_PATH = "sling:configs/com.community.aemlab.core.conf.SiteConfiguration";
	private static final String EVENT = "CAC";

	@Override
	public void onChange(List<ResourceChange> list) {
		LOGGER.debug(
				"On add/change of cac config com.community.aemlab.core.conf.SiteConfiguration, listener activated with config via OCD");
		for (ResourceChange rc : list) {
			String resourcePath = rc.getPath();
			if (resourcePath.contains(LOOKUP_PATH)) {
				AuditLogEntry entry = new AuditLogEntry(EVENT, new Date(), rc.getUserId(), resourcePath,
						rc.getType().name(), null);
				auditLog.add(entry);
			}

		}
	}

	@Activate
	@Modified
	protected void activate(ConfigurationAuditListener.Config configValues) {
		String config = ArrayUtils.toString(configValues.resource_paths());
		LOGGER.debug("Config values={}", config);
		if (!configValues.enabled()) {
			componentEnabler.disable(ConfigurationAuditListener.class.getName());
		}else {
			componentEnabler.enable(ConfigurationAuditListener.class.getName());
		}
	}

	/**
	 * @author arunpatidar02
	 *
	 */
	@ObjectClassDefinition(name = "Site Context Aware Configuration Auditor", 
			description = "On add/change of cac config com.community.aemlab.core.conf.SiteConfiguration, listener activated with this config to create audit")
	public @interface Config {

		@AttributeDefinition(name = "Enabled", description = "Enable or Disabled the ConfigurationAuditListener")
		boolean enabled() default false;

		@AttributeDefinition(name = "Paths", description = "ResourceChangeListener Paths property")
		String[] resource_paths() default { "/conf/aemlab/oneweb" };

		@AttributeDefinition(name = "Changes", description = "ResourceChangeListener Changes property")
		String[] resource_change_types() default { "ADDED", "CHANGED" };

	}

}
