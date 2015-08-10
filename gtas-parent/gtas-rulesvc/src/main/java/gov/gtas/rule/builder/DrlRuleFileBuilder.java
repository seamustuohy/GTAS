package gov.gtas.rule.builder;

import static gov.gtas.rule.builder.RuleTemplateConstants.GLOBAL_RESULT_DECLARATION;
import static gov.gtas.rule.builder.RuleTemplateConstants.IMPORT_PREFIX;
import static gov.gtas.rule.builder.RuleTemplateConstants.NEW_LINE;
import static gov.gtas.rule.builder.RuleTemplateConstants.RULE_PACKAGE_NAME;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.gtas.bo.RuleHitDetail;
import gov.gtas.error.CommonErrorConstants;
import gov.gtas.error.ErrorHandlerFactory;
import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.Passenger;
import gov.gtas.model.udr.Rule;
import gov.gtas.model.udr.RuleCond;
import gov.gtas.model.udr.UdrRule;

/**
 * A builder pattern class for constructing a Drools rule "file" (actually a
 * text string) from one or more UDR objects. This DRL string is then compiled
 * into a Knowledge Base (KieBase object).
 * 
 * @author GTAS3 (AB)
 *
 */
public class DrlRuleFileBuilder {
	/*
	 * The logger for the DrlRuleBuilder.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(DrlRuleFileBuilder.class);

	private static final Class<?>[] IMPORT_LIST = { Flight.class, Passenger.class, Document.class };
	private static final String PASSENGER_VARIABLE_NAME="$p";
	private static final String DOCUMENT_VARIABLE_NAME="$d";
	private static final String FLIGHT_VARIABLE_NAME="$f";

	private StringBuilder stringBuilder;
	private RuleConditionBuilder ruleConditionBuilder;

	public DrlRuleFileBuilder() {
		this.stringBuilder = new StringBuilder();
		this.ruleConditionBuilder = new RuleConditionBuilder(PASSENGER_VARIABLE_NAME, FLIGHT_VARIABLE_NAME, DOCUMENT_VARIABLE_NAME);
		addPackageAndImport();
		// add the global result declaration;
		this.stringBuilder.append(GLOBAL_RESULT_DECLARATION);
	}

	public DrlRuleFileBuilder addRule(final UdrRule udrRule) {
		logger.info("DrlRuleFileBuilder - generating DRL code for UDR with title:"
				+ udrRule.getTitle());
		for (Rule rule : udrRule.getEngineRules()) {
			addRuleHeader(udrRule, rule);
			for (RuleCond cond : rule.getRuleConds()) {
				this.ruleConditionBuilder.addRuleCondition(cond);
			}
			try {
				this.ruleConditionBuilder
						.buildConditionsAndApppend(this.stringBuilder);
			} catch (ParseException pe) {
				throw ErrorHandlerFactory.getErrorHandler().createException(
						CommonErrorConstants.INVALID_ARGUMENT_ERROR_CODE, pe,
						this.stringBuilder, "DrlRuleFileBuilder.addRule");
			}
			this.ruleConditionBuilder.addRuleAction(this.stringBuilder,
					udrRule, rule, PASSENGER_VARIABLE_NAME);
		}
		return this;
	}

	public String build() {

		return this.stringBuilder.toString();
	}

	private void addPackageAndImport() {
		this.stringBuilder.append(RULE_PACKAGE_NAME).append(IMPORT_PREFIX)
				.append(RuleHitDetail.class.getName()).append(";")
				.append(NEW_LINE);
		        for(Class<?> clazz: IMPORT_LIST){
		        	this.stringBuilder.append(IMPORT_PREFIX)
		        	.append(clazz.getName()).append(";").append(NEW_LINE);
		        }
	}

	private void addRuleHeader(UdrRule parent, Rule rule) {
		this.stringBuilder.append("rule \"").append(parent.getTitle())
				.append(":").append(rule.getRuleIndex()).append("\"")
				.append(NEW_LINE).append("when\n");
	}
}
