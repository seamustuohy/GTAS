/*
 * All GTAS code is Copyright 2016, Unisys Corporation.
 * 
 * Please see LICENSE.txt for details.
 */
package gov.gtas.rule.listener;

import gov.gtas.bo.RuleExecutionStatistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.kie.api.definition.rule.Rule;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class extends DefaultAgendaEventListener that implements
 * AgendaEventListener interface with empty methods so that you only have to
 * override the methods that you are interested in
 * 
 *
 */
public class GtasAgendaEventListener extends DefaultAgendaEventListener {

    private static final Logger logger = LoggerFactory
            .getLogger(GtasAgendaEventListener.class);

    private List<String> ruleNameList = new ArrayList<String>();

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

        Rule rule = event.getMatch().getRule();
        String ruleName = rule.getName();
        Map<String, Object> ruleMetaDataMap = rule.getMetaData();

        ruleNameList.add(ruleName);
        StringBuilder sb = new StringBuilder("Rule fired: " + ruleName);

        if (ruleMetaDataMap.size() > 0) {
            sb.append("\n  With [" + ruleMetaDataMap.size() + "] meta-data:");
            for (String key : ruleMetaDataMap.keySet()) {
                sb.append("\n    key=" + key + ", value="
                        + ruleMetaDataMap.get(key));
            }
        }

        logger.debug(sb.toString());
    }
    public boolean isRuleFired(String ruleName) {
        for (String a : ruleNameList) {
            if (a.equals(ruleName)) {
                return true;
            }
        }
        return false;
    }

    public void reset() {
        ruleNameList.clear();
    }

    public final List<String> getRuleNameList() {
        return ruleNameList;
    }

    public String ruleMatchesToString() {
        if (ruleNameList.size() == 0) {
            return "No matches occurred.";
        } else {
            StringBuilder sb = new StringBuilder("Rules Matched: ");
            for (String match : ruleNameList) {
                sb.append("\n  rule: ").append(match);
            }
            return sb.toString();
        }
    }
}
