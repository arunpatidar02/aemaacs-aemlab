package com.community.aemlab.core.models.concept;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.jetbrains.annotations.Nullable;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ContainerExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.Container;
import com.community.aemlab.core.models.concept.core.PanelContainerImpl;
import com.day.cq.wcm.api.components.ComponentManager;

/**
 * Tabs model implementation.
 */
@Model(adaptables = SlingHttpServletRequest.class, adapters = { ChildEditorExample.class, ComponentExporter.class,
		ContainerExporter.class }, resourceType = ChildEditorExample.RESOURCE_TYPE)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ChildEditorExample extends PanelContainerImpl implements Container {

	/**
	 * The resource type.
	 */
	public static final String RESOURCE_TYPE = "aemlab/oneweb/concept/components/children-editor";

	/**
	 * The current active tab.
	 */
	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Nullable
	private String activeItem;


	/**
	 * The current resource.
	 */
	@SlingObject
	private Resource resource;

	/**
	 * The current request.
	 */
	@Self
	SlingHttpServletRequest request;

	/**
	 * The name of the active item.
	 */
	private String activeItemName;

	
	public String getActiveItem() {
		if (activeItemName == null) {
			this.activeItemName = Optional.ofNullable(this.activeItem).map(resource::getChild).map(Resource::getName)
					.orElseGet(() -> Optional.ofNullable(request.getResourceResolver().adaptTo(ComponentManager.class))
							.flatMap(componentManager -> StreamSupport
									.stream(resource.getChildren().spliterator(), false).filter(Objects::nonNull)
									.filter(res -> Objects.nonNull(componentManager.getComponentOfResource(res)))
									.findFirst().map(Resource::getName))
							.orElse(null));
		}
		return activeItemName;
	}

}