package com.community.aemlab.core.services.sample;

import java.util.List;

public interface CRXDERestrictionService {
	public boolean isRestrictedUser(List<String> currentUserGroups, String currentUserId);

	public boolean isEnabled();
}