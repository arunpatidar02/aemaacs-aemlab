package com.community.aemlab.core.models.sample;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * @author arunpatidar02
 *
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public interface InterfaceModel {

	String FNAME = "fname";

	@Self
	Resource getSelf();

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL, name = FNAME)
	String getFname();
}