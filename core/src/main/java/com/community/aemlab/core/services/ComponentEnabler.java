package com.community.aemlab.core.services;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * @author arunpatidar02
 *
 */
@Component(service = ComponentEnabler.class)
public class ComponentEnabler {

	ComponentContext context;

	@Activate
	void activate(ComponentContext context) {
		this.context = context;
	}

	/**
	 * @param name
	 */
	public void enable(String name) {
		this.context.enableComponent(name);
	}

	/**
	 * @param name 
	 */
	public void disable(String name) {
		this.context.disableComponent(name);
	}
}