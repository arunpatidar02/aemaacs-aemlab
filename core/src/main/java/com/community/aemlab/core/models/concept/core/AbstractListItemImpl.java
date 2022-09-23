
package com.community.aemlab.core.models.concept.core;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.jetbrains.annotations.NotNull;

import com.adobe.cq.wcm.core.components.models.ListItem;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.adobe.cq.wcm.core.components.util.ComponentUtils;
import com.day.cq.wcm.api.components.Component;

/**
 * Abstract helper class for ListItem implementations. Generates an ID for the
 * item, using the ID of its parent as a prefix
 */
public abstract class AbstractListItemImpl extends AbstractComponentImpl implements ListItem {

	/**
	 * Prefix prepended to the item ID.
	 */
	private static final String ITEM_ID_PREFIX = "item";

	/**
	 * The ID of the component that contains this list item.
	 */
	protected String parentId;

	/**
	 * The path of this list item.
	 */
	protected String path;

	/**
	 * Construct a list item.
	 *
	 * @param parentId  The ID of the containing component.
	 * @param resource  The resource of the list item.
	 * @param component The component that contains this list item.
	 */
	protected AbstractListItemImpl(String parentId, Resource resource, Component component) {
		this.parentId = parentId;
		if (resource != null) {
			this.path = resource.getPath();
		}

		this.resource = resource;
	}

	@NotNull
	@Override
	public String getId() {
		return ComponentUtils.generateId(StringUtils.join(parentId, ComponentUtils.ID_SEPARATOR, ITEM_ID_PREFIX), path);
	}

}