package gov.gtas.rule.listener;

import gov.gtas.bo.RuleExecutionStatistics;

import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;

public class GtasRuleRuntimeEventListener implements RuleRuntimeEventListener {
	
    private RuleExecutionStatistics ruleExecutionStatistics;
    
    /**
     * constructor.
     * @param stats the data structure to collect statistics.
     */
	public GtasRuleRuntimeEventListener(final RuleExecutionStatistics stats){
		this.ruleExecutionStatistics = stats;
	}
	
	@Override
	public void objectInserted(ObjectInsertedEvent event) {
		this.ruleExecutionStatistics.incrementTotalObjectsModified();
		this.ruleExecutionStatistics.addInsertedObject(event.getObject());
	}

	@Override
	public void objectUpdated(ObjectUpdatedEvent event) {
		this.ruleExecutionStatistics.incrementTotalObjectsModified();
		this.ruleExecutionStatistics.addModifiedObject(event.getObject());
	}

	@Override
	public void objectDeleted(ObjectDeletedEvent event) {
		this.ruleExecutionStatistics.incrementTotalObjectsModified();
		this.ruleExecutionStatistics.addDeletedObject(event.getOldObject());
	}

}
