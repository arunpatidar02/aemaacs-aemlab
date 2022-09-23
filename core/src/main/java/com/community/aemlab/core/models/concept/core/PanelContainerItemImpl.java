package com.community.aemlab.core.models.concept.core;

import java.util.Optional;

import org.apache.sling.api.resource.Resource;
import org.jetbrains.annotations.NotNull;

import com.adobe.cq.wcm.core.components.models.ListItem;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.components.Component;

/**
 * Panel container item implementation.
 */
public class PanelContainerItemImpl extends ResourceListItemImpl implements ListItem {

    /**
     * Name of the property that contains the panel item's title.
     */
    public static final String PN_PANEL_TITLE = "cq:panelTitle";

    /**
     * Construct a panel item.
     *
     * @param resource The resource.
     * @param parentId The ID of the containing component.
     */
    public PanelContainerItemImpl(@NotNull final Resource resource, final String parentId, Component component,
                                  Page currentPage) {
        super(resource, parentId, component);
        setCurrentPage(currentPage);
        title = Optional.ofNullable(resource.getValueMap().get(PN_PANEL_TITLE, String.class))
            .orElseGet(() -> resource.getValueMap().get(JcrConstants.JCR_TITLE, String.class));
    }
}