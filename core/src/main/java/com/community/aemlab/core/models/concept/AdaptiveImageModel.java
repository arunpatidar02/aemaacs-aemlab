package com.community.aemlab.core.models.concept;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.factory.ModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.wcm.core.components.models.Image;
import com.community.aemlab.core.utils.ImageUtils;

/**
 * @author arunpatidar02
 *
 */
@Model(adaptables = { SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AdaptiveImageModel {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdaptiveImageModel.class);

	@Self
	private SlingHttpServletRequest request;

	@ValueMapValue
	private String title;

	@ValueMapValue
	@Default(values = "span")
	private String titleStyle;

	@OSGiService
	private ModelFactory modelFactory;

	@Inject
	private Resource imageResource;

	private String imageSrc = "";
	private String imageSrcset = "";
	

	@PostConstruct
	protected void init() {
		Image image = this.modelFactory.getModelFromWrappedRequest(this.request, imageResource, Image.class);
		if (image != null) {
			this.imageSrc = image.getFileReference();
			this.imageSrcset = ImageUtils.getSrcSet(image);
			LOGGER.debug("imageSrcset - {}", imageSrcset);

		}

	}

	/**
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return titleStyle
	 */
	public String getTitleStyle() {
		return titleStyle;
	}

	/**
	 * @return imageSrc
	 */
	public String getImageSrc() {
		return this.imageSrc;
	}

	/**
	 * @return imageSrcset
	 */
	public String getImageSrcset() {
		return this.imageSrcset;
	}

}
