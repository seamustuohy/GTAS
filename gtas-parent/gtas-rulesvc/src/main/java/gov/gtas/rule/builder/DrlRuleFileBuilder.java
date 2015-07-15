package gov.gtas.rule.builder;

import static gov.gtas.rule.builder.RuleTemplateConstants.ACTION_PASSENGER_HIT;
import static gov.gtas.rule.builder.RuleTemplateConstants.GLOBAL_RESULT_DECLARATION;
import static gov.gtas.rule.builder.RuleTemplateConstants.IMPORT_PREFIX;
import static gov.gtas.rule.builder.RuleTemplateConstants.NEW_LINE;
import static gov.gtas.rule.builder.RuleTemplateConstants.RULE_PACKAGE_NAME;

import java.text.ParseException;

import gov.gtas.bo.RuleHitDetail;
import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.Traveler;
import gov.gtas.model.udr.Rule;
import gov.gtas.model.udr.RuleCond;
import gov.gtas.model.udr.UdrRule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class DrlRuleFileBuilder {
	/*
	 * The logger for the DrlRuleBuilder.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(DrlRuleFileBuilder.class);
	
	private StringBuilder stringBuilder;
	private RuleConditionBuilder ruleConditionBuilder;
	
	public DrlRuleFileBuilder(){
		this.stringBuilder = new StringBuilder();
		this.ruleConditionBuilder = new RuleConditionBuilder();
		addPackageAndImport();
		//add the global result declaration;
		this.stringBuilder.append(GLOBAL_RESULT_DECLARATION);
	}  
	public DrlRuleFileBuilder addRule(final UdrRule udrRule){
		for(Rule rule: udrRule.getEngineRules()){
			addRuleHeader(udrRule, rule);
			for(RuleCond cond:rule.getRuleConds()){
				this.ruleConditionBuilder.addRuleCondition(cond);
			}
			try{
			    this.ruleConditionBuilder.buildConditionsAndApppend(this.stringBuilder);
			} catch (ParseException pe){
				//TODO
			}
			addRuleAction(udrRule, rule);
		}
		return this;
	}
	public String build(){
		
		return this.stringBuilder.toString();
	}
	private void addPackageAndImport(){
		this.stringBuilder.append(RULE_PACKAGE_NAME)
		.append(IMPORT_PREFIX).append(RuleHitDetail.class.getName()).append(";").append(NEW_LINE)
		.append(IMPORT_PREFIX).append(Traveler.class.getName()).append(";").append(NEW_LINE)
		.append(IMPORT_PREFIX).append(Flight.class.getName()).append(";").append(NEW_LINE)
		.append(IMPORT_PREFIX).append(Document.class.getName()).append(";").append(NEW_LINE)
		.append("\n");
	}
    private void addRuleHeader(UdrRule parent, Rule rule){
    	this.stringBuilder.append("rule \"")
    	.append(parent.getTitle()).append(":").append(rule.getRuleIndex()).append("\"").append(NEW_LINE)
    	.append("when\n");    	
    }
    private void addRuleAction(UdrRule parent, Rule rule){
    	this.stringBuilder.append("then\n")
    	.append(String.format(ACTION_PASSENGER_HIT, parent.getId(), rule.getRuleIndex())).append("\n")
    	.append("end\n");
    }
}
