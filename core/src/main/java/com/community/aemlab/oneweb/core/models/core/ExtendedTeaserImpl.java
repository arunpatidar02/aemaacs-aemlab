package com.community.aemlab.oneweb.core.models.core;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;
import org.jetbrains.annotations.Nullable;

import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.ListItem;
import com.adobe.cq.wcm.core.components.models.Teaser;
import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { Teaser.class,
		ExtendedTeaserImpl.class }, resourceType = ExtendedTeaserImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ExtendedTeaserImpl implements Teaser {

	@Self
	@Via(type = ResourceSuperType.class)
	private Teaser teaser;

	@Inject
	@ChildResource
	private List<TeaserCustomAction> actions;

	public static final String RESOURCE_TYPE = "aemlab/oneweb/core-extend/components/teaser";

	public List<TeaserCustomAction> getCustomActions() {
		return new ArrayList<>(actions);
	}

	@Override
	public String getTitle() {
		return teaser.getTitle();
	}

	@Override
	public String getTitleType() {
		return teaser.getTitleType();
	}

	@Override
	public String getDescription() {
		return teaser.getDescription();
	}

	@Override
	public String getId() {
		return teaser.getId();
	}

	@Override
	public String getPretitle() {
		return teaser.getPretitle();
	}

	@Override
	public String getLinkURL() {
		return teaser.getLinkURL();
	}

	@Override
	public boolean isActionsEnabled() {
		return teaser.isActionsEnabled();
	}

	@Override
	public List<ListItem> getActions() {
		return teaser.getActions();
	}

	@Override
	public Resource getImageResource() {
		return teaser.getImageResource();
	}

	@Override
	public boolean isImageLinkHidden() {
		return teaser.isImageLinkHidden();
	}

	@Override
	public boolean isTitleLinkHidden() {
		return teaser.isTitleLinkHidden();
	}

	@Override
	public String getExportedType() {
		return teaser.getExportedType();
	}

	@Override
	public @Nullable ComponentData getData() {
		return teaser.getData();
	}

}