package com.community.aemlab.core.services.transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.jackrabbit.oak.commons.PropertiesUtil;
import org.apache.sling.rewriter.Transformer;
import org.apache.sling.rewriter.TransformerFactory;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.acs.commons.util.ParameterUtil;

/**
 * Link rewriting shortening, this transformer can be configured
 * in a flexible way for link rewriting See LinkRewriterTransformer
 * (http://sling.apache.org/site/output-rewriting-pipelines-orgapacheslingrewriter.html)
 * for details.
 *
 * To disable the link shortening remove the OSGI configuration.
 */
@Component(property = { Constants.SERVICE_DESCRIPTION + "=LinkRewriterTransformerFactory",
		"pipeline.type=shortenlinkrewriter",
		"service.ranking=-1" }, immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = ShortenLinkRewriterTransformerFactory.Config.class)
public class ShortenLinkRewriterTransformerFactory
		implements TransformerFactory, ShortenLinkRewriterTransformerFactoryConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(ShortenLinkRewriterTransformerFactory.class);

	private static final String[] DEFAULT_ATTRIBUTES = new String[] { "a:href" };

	private Map<String, String[]> elementsAndAttributesToRewrite;

	private List<Pattern> pathPatterns;

	@ObjectClassDefinition(name = "Link Shortening Configuration", description = "Configuration for the link shortening")
	public @interface Config {

		@AttributeDefinition(name = "Regular Expression List", description = "Only if one of the regular expressions patterns matches the to-be modified path it will be modified.")
		String[] getRegularExpressions();

		@AttributeDefinition(name = "Attributes For Rewriting", description = "List of element/attribute pairs to rewrite", defaultValue = "a:href")
		String[] getAttributesForRewriting();
	}

	/** Factory method */
	@Override
	public Transformer createTransformer() {
		LOGGER.debug("Creating {}", this.getClass());
		return new ShortenLinkRewriterTransformer(elementsAndAttributesToRewrite, pathPatterns);
	}

	@Activate
	protected void activate(final Config config) {
		final String[] attrProp = PropertiesUtil.toStringArray(config.getAttributesForRewriting(), DEFAULT_ATTRIBUTES);
		this.elementsAndAttributesToRewrite = ParameterUtil.toMap(attrProp, ":", ";");

		final String[] regExList = PropertiesUtil.toStringArray(config.getRegularExpressions());
		this.pathPatterns = new ArrayList<>();
		for (String regex : regExList) {
			Pattern pattern = Pattern.compile(regex);
			pathPatterns.add(pattern);
		}
		LOGGER.debug("ShortenLinkRewriterTransformer");
	}

	@Override
	public Map<String, String[]> getElementsAndAttributesToRewrite() {
		return elementsAndAttributesToRewrite;
	}

	@Override
	public List<Pattern> getPathPatterns() {
		return pathPatterns;
	}
}
