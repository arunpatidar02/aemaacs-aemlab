
package com.community.aemlab.core.models.concept;

import static com.community.aemlab.core.utils.AEMLABConstants.FORWARD_SLASH;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.caconfig.management.ValueInfo;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;

import com.adobe.cq.xf.ExperienceFragmentsConstants;
import com.community.aemlab.core.services.ConfigurationService;
import com.day.cq.wcm.api.Page;

@Model(adaptables = { SlingHttpServletRequest.class,
		Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CacXFModel {

	@ValueMapValue(name = "configName")
	private String configName;

	@ValueMapValue(name = "property")
	private String property;

	@SlingObject
	private ResourceResolver resolver;

	@Inject
	private Page currentPage;

	@Inject
	private ConfigurationService configService;

	@Inject
	@Named("log")
	private Logger logger;

	@PostConstruct
	protected void init() {
		logger.debug("Processing data for {}", currentPage.getPath());
	}

	@SuppressWarnings("unchecked")
	public ValueInfo<String> getConfigValue() {
		return (ValueInfo<String>) configService.getConfigValue(currentPage.adaptTo(Resource.class), configName,
				property);
	}

	/**
	 * Gets master variation for configured experience fragment
	 *
	 * @return
	 * @throws PersistenceException
	 */
	public String getFragmentPath() {

		ValueInfo<?> configuration = getConfigValue();
		String fragmentPath = (String) configuration.getEffectiveValue();
		if (StringUtils.isEmpty(fragmentPath)) {
			logger.warn("Experience fragment {} is empty", fragmentPath);
			return fragmentPath;
		}
		Resource fragment = resolver.getResource(fragmentPath);
		if (fragment == null || fragment.getChild(JcrConstants.JCR_CONTENT) == null) {
			logger.warn("Experience fragment {} does not have metadata or configuration category is incorrect",
					fragmentPath);
			return null;
		}
		String variationPath = fragmentPath;
		if (fragment.getChild(JcrConstants.JCR_CONTENT)
				.isResourceType(ExperienceFragmentsConstants.RT_EXPERIENCE_FRAGMENT_MASTER)) {
			Iterator<Resource> children = fragment.getChildren().iterator();
			while (children.hasNext()) {
				Resource childResource = children.next();
				Resource content = childResource.getChild(JcrConstants.JCR_CONTENT);
				if (content != null) {
					ValueMap props = content.adaptTo(ValueMap.class);
					if (props != null
							&& props.get(ExperienceFragmentsConstants.PN_XF_MASTER_VARIATION, Boolean.class)) {
						variationPath = childResource.getPath();
					}
				}
			}
		}
		return StringUtils.join(variationPath, FORWARD_SLASH, JcrConstants.JCR_CONTENT);

	}

}
