package com.community.aemlab.core.services.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.community.aemlab.core.services.SampleOsgiService;
import com.community.aemlab.core.services.config.Configuration;

/**
 * @author arunpatidar02
 *
 */
@Component(immediate = true, service = SampleOsgiService.class)
@Designate(ocd = Configuration.class)
public class SampleOsgiServiceImpl implements SampleOsgiService {

	private static final Logger LOG = LoggerFactory.getLogger(SampleOsgiServiceImpl.class);
	private static int counter = 0; 

	boolean booleanProp;
	String stringProp;
	String dropdownProp;
	String[] stringArrayProp;
	char[] passwordProp;
	long longProp;

	/**
	 * @return settings
	 */
	public String getSettings() {
		StringBuilder sb = new StringBuilder();
		sb.append("Sample OSGi Service:\n");
		sb.append("booleanProp: " + booleanProp + "\n");
		sb.append("stringProp: " + stringProp + "\n");
		sb.append("dropdownProp: " + dropdownProp + "\n");
		sb.append("stringArrayProp: " + ArrayUtils.toString(stringArrayProp) + "\n");
		sb.append("passwordProp: " + String.valueOf(passwordProp) + "\n");
		sb.append("longProp: " + longProp + "\n");
		LOG.info("SampleOsgiService#getSettings : {}", counter++);
		return sb.toString();
	}

	@Activate
	@Modified
	protected final void activate(Configuration config) {
		booleanProp = config.servicename_propertyname_boolean();
		stringProp = config.servicename_propertyname_string();
		dropdownProp = config.servicename_propertyname_dropdown();
		stringArrayProp = config.servicename_propertyname_string_array();
		passwordProp = config.servicename_propertyname_password().toCharArray();
		longProp = config.servicename_propertyname_long();
	}

}