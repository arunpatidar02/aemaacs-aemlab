package com.community.aemlab.core.services;

import java.util.List;

public interface EnvironmentTypeProvider {

	/**
	 * @return environmentType
	 */
	public String getEnvironmentType();
	
	/**
	 * Get Runmodes based on configured osgi configurations
	 * 
	 * @return runmodes
	 */
	public List<String> getRunModes();
	
	/**
	 * Get Runmodes based on configured osgi configurations and check if this is Author
	 * 
	 * @return true if Author
	 */
	public boolean isAuthor();
	

}
