package gov.gtas.rule.listener;

import gov.gtas.bo.RuleExecutionStatistics;

import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.AgendaGroupPoppedEvent;
import org.kie.api.event.rule.AgendaGroupPushedEvent;
import org.kie.api.event.rule.BeforeMatchFiredEvent;
import org.kie.api.event.rule.MatchCancelledEvent;
import org.kie.api.event.rule.MatchCreatedEvent;
import org.kie.api.event.rule.RuleFlowGroupActivatedEvent;
import org.kie.api.event.rule.RuleFlowGroupDeactivatedEvent;

public class GtasAgendaEventListener implements AgendaEventListener {
    private RuleExecutionStatistics ruleExecutionStatistics;
    
    /**
     * constructor.
     * @param stats the data structure to collect statistics.
     */
	public GtasAgendaEventListener(final RuleExecutionStatistics stats){
		this.ruleExecutionStatistics = stats;
	}
	
	@Override
	public void matchCreated(MatchCreatedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void matchCancelled(MatchCancelledEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeMatchFired(BeforeMatchFiredEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterMatchFired(AfterMatchFiredEvent event) {
		this.ruleExecutionStatistics.incrementTotalRulesFired();
		this.ruleExecutionStatistics.addRuleFired(event.getMatch().getRule().getName());
	}

	@Override
	public void agendaGroupPopped(AgendaGroupPoppedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void agendaGroupPushed(AgendaGroupPushedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeRuleFlowGroupDeactivated(
			RuleFlowGroupDeactivatedEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterRuleFlowGroupDeactivated(
			RuleFlowGroupDeactivatedEvent event) {
		// TODO Auto-generated method stub

	}

}
