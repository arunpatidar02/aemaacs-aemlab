package com.community.aemlab.core.services.sample;

import java.util.Calendar;
import java.util.Optional;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.sitemap.SitemapException;
import org.apache.sling.sitemap.builder.Sitemap;
import org.apache.sling.sitemap.builder.Url;
import org.apache.sling.sitemap.spi.common.SitemapLinkExternalizer;
import org.apache.sling.sitemap.spi.generator.ResourceTreeSitemapGenerator;
import org.apache.sling.sitemap.spi.generator.SitemapGenerator;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceRanking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;

@Component(service = SitemapGenerator.class)
@ServiceRanking(20)
public class MySitemapGenerator extends ResourceTreeSitemapGenerator {

	private static final Logger LOGGER = LoggerFactory.getLogger(MySitemapGenerator.class);

	@Reference
	private SitemapLinkExternalizer externalizer;

	@Override
	protected void addResource(final String name, final Sitemap sitemap, final Resource resource)
			throws SitemapException {

		final Page page = resource.adaptTo(Page.class);
		final String location = this.externalizer.externalize(resource);
		final Url url = sitemap.addUrl(location);
		final Calendar lastmod = Optional.ofNullable(page.getLastModified())
				.orElse(page.getContentResource().getValueMap().get(JcrConstants.JCR_CREATED, Calendar.class));
		if (lastmod != null) {
			url.setLastModified(lastmod.toInstant());
			url.setPriority(2);

		}
		LOGGER.debug("Added the {} to Extended Sitemap", url);
	}
}