package com.community.aemlab.oneweb.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class IframeModel {

	@ValueMapValue
	private String title;

	@ValueMapValue
	private String path;

	@ValueMapValue
	private String height;

	@ValueMapValue
	private String width;

	@ValueMapValue
	private String scrolling;

	
	/**
	 * @return title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @return path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @return height
	 */
	public String getHeight() {
		return height;
	}

	/**
	 * @return width
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * @return scrolling
	 */
	public String getScrolling() {
		return scrolling;
	}

}