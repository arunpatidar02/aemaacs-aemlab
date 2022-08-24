package com.community.aemlab.core.predicate.sample;

import javax.jcr.query.Row;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.Predicate;
import com.day.cq.search.eval.AbstractPredicateEvaluator;
import com.day.cq.search.eval.EvaluationContext;

/**
 * @author arunpatidar02@author arunpatidar02
 * 
 * Custom case insensitive predicate for like operation e.g.
 * 
 * caseinsensitive.property=jcr:content/@jcr:title
 * caseinsensitive.value=queryString %
 *
 */
@Component(factory = "com.day.cq.search.eval.PredicateEvaluator/caseinsensitive")
public class CaseInsensitiveLikePredicate extends AbstractPredicateEvaluator {
	public static final String PROPERTY = "property";
	public static final String VALUE = "value";
	public static final String WILDCARD = "%";
	private static final Logger LOGGER = LoggerFactory.getLogger(CaseInsensitiveLikePredicate.class);

	@Override
	public boolean includes(Predicate predicate, Row row, EvaluationContext context) {
		if (predicate.hasNonEmptyValue(PROPERTY)) {
			return true;
		}
		return super.includes(predicate, row, context);
	}

	@Override
	public String getXPathExpression(Predicate predicate, EvaluationContext context) {
		if (!predicate.hasNonEmptyValue(PROPERTY)) {
			return null;
		}
		if (predicate.hasNonEmptyValue(PROPERTY) && null == predicate.get(VALUE)) {
			return super.getXPathExpression(predicate, context);
		}
		if (predicate.hasNonEmptyValue(PROPERTY)) {
			if (WILDCARD.equals(predicate.get(VALUE))) {
				LOGGER.debug("Case Insensitive Query only has wildcard, ignoring predicate");
				return "";
			}

			StringBuilder sb = new StringBuilder();
			sb.append("jcr:like(fn:lower-case(");
			sb.append(predicate.get(PROPERTY));
			sb.append("\"), '");
			sb.append(predicate.get(VALUE).toLowerCase());
			sb.append("')");

			LOGGER.debug("XPATH Query is - {}", sb);
			
			return sb.toString();
		}
		return null;
	}
}