package com.community.aemlab.core.models.concept.core;

import java.util.Calendar;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.jetbrains.annotations.NotNull;

import com.adobe.cq.wcm.core.components.models.ListItem;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.components.Component;

/**
 * Resource-backed list item implementation.
 */
public class ResourceListItemImpl extends AbstractListItemImpl implements ListItem {

    /**
     * The title.
     */
    protected String title;

    /**
     * The description.
     */
    protected String description;

    /**
     * The last modified date.
     */
    protected Calendar lastModified;

    /**
     * The name.
     */
    protected String name;

    /**
     * Construct a resource-backed list item.
     *
     * @param resource The resource.
     * @param parentId The ID of the containing component.
     */
    public ResourceListItemImpl(@NotNull Resource resource,
                                String parentId, Component component) {
        super(parentId, resource, component);
        ValueMap valueMap = resource.getValueMap();
        title = valueMap.get(JcrConstants.JCR_TITLE, String.class);
        description = valueMap.get(JcrConstants.JCR_DESCRIPTION, String.class);
        lastModified = valueMap.get(JcrConstants.JCR_LASTMODIFIED, Calendar.class);
        path = resource.getPath();
        name = resource.getName();
    }


    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Calendar getLastModified() {
        return lastModified;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getName() {
        return name;
    }
}