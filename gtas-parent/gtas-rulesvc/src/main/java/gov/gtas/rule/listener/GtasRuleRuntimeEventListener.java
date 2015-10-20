package gov.gtas.rule.listener;

import gov.gtas.bo.RuleExecutionStatistics;

import org.kie.api.event.rule.DefaultRuleRuntimeEventListener;
import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;

/**
 * This class extends DefaultRuleRuntimeEventListener that 
 * implements RuleRuntimeEventListener interface with empty methods so that you only have to 
 * override the methods that you are interested in
 * 
 *
 */
public class GtasRuleRuntimeEventListener extends
		DefaultRuleRuntimeEventListener {

	private RuleExecutionStatistics ruleExecutionStatistics;

	/**
	 * constructor.
	 * 
	 * @param stats
	 *            the data structure to collect statistics.
	 */
	public GtasRuleRuntimeEventListener(final RuleExecutionStatistics stats) {
		this.ruleExecutionStatistics = stats;
	}

	public void objectInserted(ObjectInsertedEvent event) {
		this.ruleExecutionStatistics.incrementTotalObjectsModified();
		this.ruleExecutionStatistics.addInsertedObject(event.getObject());
	}

	public void objectUpdated(ObjectUpdatedEvent event) {
		this.ruleExecutionStatistics.incrementTotalObjectsModified();
		this.ruleExecutionStatistics.addModifiedObject(event.getObject());
	}

	public void objectDeleted(ObjectDeletedEvent event) {
		this.ruleExecutionStatistics.incrementTotalObjectsModified();
		this.ruleExecutionStatistics.addDeletedObject(event.getOldObject());
	}

}
