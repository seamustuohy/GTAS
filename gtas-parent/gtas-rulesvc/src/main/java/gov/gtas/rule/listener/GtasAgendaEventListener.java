package gov.gtas.rule.listener;

import gov.gtas.bo.RuleExecutionStatistics;

import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;


/**
 * This class extends DefaultAgendaEventListener that 
 * implements AgendaEventListener interface with empty methods so that you only have to 
 * override the methods that you are interested in
 * 
 *
 */
public class GtasAgendaEventListener extends DefaultAgendaEventListener {
	private RuleExecutionStatistics ruleExecutionStatistics;

	/**
	 * constructor.
	 * 
	 * @param stats
	 *            the data structure to collect statistics.
	 */
	public GtasAgendaEventListener(final RuleExecutionStatistics stats) {
		this.ruleExecutionStatistics = stats;
	}

	public void afterMatchFired(AfterMatchFiredEvent event) {
		this.ruleExecutionStatistics.incrementTotalRulesFired();
		this.ruleExecutionStatistics.addRuleFired(event.getMatch().getRule()
				.getName());
	}

}
