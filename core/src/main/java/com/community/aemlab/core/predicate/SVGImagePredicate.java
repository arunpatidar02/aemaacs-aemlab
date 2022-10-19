package com.community.aemlab.core.predicate;

import java.util.Objects;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.collections.Predicate;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.commons.predicate.AbstractNodePredicate;
import com.day.cq.dam.api.DamConstants;

/**
 *
 * This Implementation class will help content authors to select only
 * image/video using touch ui
 * granite/ui/components/coral/foundation/form/pathbrowser
 */
@Component(service = Predicate.class, property = { SVGImagePredicate.PREDICATE_PROPERTY })
public class SVGImagePredicate extends AbstractNodePredicate {
    private static final Logger LOGGER = LoggerFactory.getLogger(SVGImagePredicate.class);
    private static final String SVG = "svg";
    static final String PREDICATE_PROPERTY = "predicate.name=svgpredicate";

    @Override
    public final boolean evaluate(final Node node) {
        if (Objects.nonNull(node)) {
            return isFolder(node) || found(node);
        }
        return false;
    }

    /**
     * @param node
     * @return true if node is svg image otherwise false
     */
    private static boolean found(Node node) {
        try {
            if (node.isNodeType(DamConstants.NT_DAM_ASSET)) {
                String extension = FilenameUtils.getExtension(node.getName());
                if (StringUtils.isNotBlank(extension) && extension.equalsIgnoreCase(SVG)) {
                    return true;
                }
            }

        } catch (RepositoryException re) {
            LOGGER.error("**** Unable to read node name**** - {}", re.getMessage());
        }
        return false;
    }

    /**
     * @param node
     * @return true if node is folder otherwise false
     */
    private static boolean isFolder(Node node) {
        try {
            if (node.isNodeType(JcrResourceConstants.NT_SLING_ORDERED_FOLDER)
                    || node.isNodeType(JcrResourceConstants.NT_SLING_FOLDER)
                    || node.isNodeType(JcrConstants.NT_FOLDER)) {
                return true;
            }
        } catch (RepositoryException re) {
            LOGGER.error("**** Unable to read node type**** - {}", re.getMessage());
        }
        return false;
    }

}