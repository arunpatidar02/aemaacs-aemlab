package com.community.aemlab.core.models.sample;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;

/**
 * @author arunpatidar02
 * 
 *         Sightly code
 * 
 *         <div data-sly-use.myObj="${'com.acc.aem64.core.models.ParamModel' @text='Sometext'}">${myObj.reversed}</div>
 *
 */
@Model(adaptables = { SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ParamModel {

	@RequestAttribute(name = "text")
	private String text;

	private String reversed;

	@PostConstruct
	protected void init() {
		reversed = new StringBuilder(text).reverse().toString();
	}

	/**
	 * @return
	 */
	public String getReversed() {
		return reversed;
	}
}
