package com.community.aemlab.core.services.config;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * @author arunpatidar02
 *
 */
@ObjectClassDefinition(
		name = "WebComponent Filter Registration Service Configuration", 
		description = "WebComponent Filter Registration Service Configuration to add web components")
public @interface WebComponentFilterServiceConfig {

	@AttributeDefinition(name = "enabled", description = "Enable", type = AttributeType.BOOLEAN)
	boolean enabled() default false;

	@AttributeDefinition(name = "Resource Types", description = "Resource Type to include in the filter", type = AttributeType.STRING)
	String[] resourcetype_list() default "";

}