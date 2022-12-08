package com.community.aemlab.core.services.sample.impl;

import java.util.Arrays;
import java.util.List;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.community.aemlab.core.services.sample.CRXDERestrictionService;
import com.community.aemlab.core.services.sample.impl.CRXDERestrictionServiceImpl.CRXDERestrictionServiceConfig;
import com.community.aemlab.oneweb.core.services.AEMLABResourceResolverProvider;

@Component(service = CRXDERestrictionService.class, immediate = true)
@Designate(ocd = CRXDERestrictionServiceConfig.class)
public class CRXDERestrictionServiceImpl implements CRXDERestrictionService {

	@Reference
	private AEMLABResourceResolverProvider resolverProvider;

	private static final Logger LOGGER = LoggerFactory.getLogger(CRXDERestrictionServiceImpl.class);

	// local variables to hold OSGi config values
	private boolean enabled;
	private List<String> restrictedGroups;
	private List<String> excludedUsers;

	@Activate
	@Modified
	protected void activate(final CRXDERestrictionServiceConfig crxConfiguration) {
		this.enabled = crxConfiguration.isEnabled();
		this.excludedUsers = Arrays.asList(crxConfiguration.getExcludedUsers());
		this.restrictedGroups = Arrays.asList(crxConfiguration.getRestrictedGroups());

		LOGGER.debug("Enabled - {}, excludedUsers - {}, restrictedGroups - {}", enabled,
				excludedUsers, restrictedGroups);
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public boolean isExcludedUser(String currentUserId) {
		return excludedUsers.contains(currentUserId);
	}

	@Override
	public boolean isRestrictedUser(List<String> currentUserGroups, String currentUserId) {
		if(isExcludedUser(currentUserId)) {
			return false;
		}
		
		currentUserGroups.retainAll(restrictedGroups);
		LOGGER.debug("Common groups {}", currentUserGroups);
		return restrictedGroups.contains(currentUserId) || !currentUserGroups.isEmpty();
	}


	@ObjectClassDefinition(name = "CRXDE Restriction Configuration", description = "Configuration to restrict user to access CRXDE")
	public @interface CRXDERestrictionServiceConfig {

		@AttributeDefinition(name = "enabled", description = "If checked, CRXDE restriction will be applied based on configurations", type = AttributeType.BOOLEAN)
		boolean isEnabled() default false;


		@AttributeDefinition(name = "Restricted Groups", description = "List of Groups to disable CRXDE access", type = AttributeType.STRING)
		String[] getRestrictedGroups() default {};

		@AttributeDefinition(name = "Excluded Users", description = "List of Users to exclude to restrict CRXDE", type = AttributeType.STRING)
		String[] getExcludedUsers() default {};

	}

}