package com.community.aemlab.core.services.config;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Option;

/**
 * @author arunpatidar02
 *
 */
@ObjectClassDefinition(name = "Annotation Demo Service - OSGi", description = "Sample services")
public @interface Configuration {

	@AttributeDefinition(name = "Boolean Property", description = "Sample boolean value", type = AttributeType.BOOLEAN)
	boolean servicename_propertyname_boolean() default true;

	@AttributeDefinition(name = "String Property", description = "Sample String property", type = AttributeType.STRING)
	String servicename_propertyname_string() default "foo";

	@AttributeDefinition(name = "Dropdown property", description = "Sample dropdown property", options = {
			@Option(label = "DAYS", value = "DAYS"), @Option(label = "HOURS", value = "HOURS"),
			@Option(label = "MILLISECONDS", value = "MILLISECONDS"), @Option(label = "MINUTES", value = "MINUTES"),
			@Option(label = "SECONDS", value = "SECONDS") })
	String servicename_propertyname_dropdown() default StringUtils.EMPTY;

	@AttributeDefinition(name = "String Array Property", description = "Sample String array property", type = AttributeType.STRING)
	String[] servicename_propertyname_string_array() default { "foo", "bar" };

	/*
	 * To create password field, either set the AttributeType or have the property
	 * name end with "*.password" (or both).
	 */
	@AttributeDefinition(name = "Password Property", description = "Sample password property", type = AttributeType.PASSWORD)
	String servicename_propertyname_password() default StringUtils.EMPTY;

	@AttributeDefinition(name = "Long Property", description = "Sample long property", type = AttributeType.LONG)
	long servicename_propertyname_long() default 0L;

}
