package com.community.aemlab.core.services.sample.impl;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.Filter;

import org.apache.sling.engine.EngineConstants;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

import com.community.aemlab.core.filters.sample.WebcomponentDynamicFilter;
import com.community.aemlab.core.services.config.WebComponentFilterServiceConfig;

/**
 * @author arunpatidar02
 *
 */
@Component(service = WebComponentsFilterRegistrationService.class, immediate = true)
@Designate(ocd = WebComponentFilterServiceConfig.class)
public class WebComponentsFilterRegistrationService {

	String[] resourseTypeList = {};
	boolean enabled = true;
	BundleContext bundleContext;
	private List<ServiceRegistration<?>> serviceRegistrations = new ArrayList<>();

	@Activate
	public void activate(BundleContext bundleContext, WebComponentFilterServiceConfig config) {
		this.resourseTypeList = config.resourcetype_list();
		this.enabled = config.enabled();
		this.bundleContext = bundleContext;
		registerAllServlets();
	}

	@Modified
	protected void modified(WebComponentFilterServiceConfig config) {
		this.enabled = config.enabled();
		this.resourseTypeList = config.resourcetype_list();
		updateServletRegistrations();
		registerAllServlets();
	}

	@Deactivate
	public void deactivate() {
		updateServletRegistrations();
	}

	/**
	 * @param res
	 * @return Filter registration properties
	 */
	private Dictionary<String, Object> getPropertiesMap(String res) {
		final Dictionary<String, Object> properties = new Hashtable<>();
		properties.put(EngineConstants.SLING_FILTER_SCOPE, EngineConstants.FILTER_SCOPE_COMPONENT);
		properties.put(Constants.SERVICE_RANKING + ":Integer=", 0);
		properties.put("sling.filter.resourceTypes", res);

		return properties;
	}

	/**
	 * Register Filter for all the configured resource types
	 */
	private void registerAllServlets() {
		if (this.enabled) {
			for (String res : resourseTypeList) {
				serviceRegistrations.add(bundleContext.registerService(Filter.class.getName(),
						new WebcomponentDynamicFilter(), getPropertiesMap(res)));
			}

		}
	}

	/**
	 * unregister Filter for all the configured resource types
	 */
	private void updateServletRegistrations() {
		for (ServiceRegistration<?> serviceRegistration : serviceRegistrations) {
			if (serviceRegistration != null) {
				serviceRegistration.unregister();
			}
		}
		serviceRegistrations.clear();
	}
}