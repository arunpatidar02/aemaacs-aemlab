
package com.community.aemlab.core.models.concept;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SdiExample {

	private String text = "The current time is : ";

	/**
	 * @return text
	 */
	public String getText() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		text = text + sdf.format(timestamp);
		return text;
	}

}
