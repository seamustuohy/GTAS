package gov.gtas.rule.builder;

import static gov.gtas.rule.builder.RuleTemplateConstants.GLOBAL_RESULT_DECLARATION;
import static gov.gtas.rule.builder.RuleTemplateConstants.IMPORT_PREFIX;
import static gov.gtas.rule.builder.RuleTemplateConstants.NEW_LINE;
import static gov.gtas.rule.builder.RuleTemplateConstants.RULE_PACKAGE_NAME;
import gov.gtas.bo.RuleHitDetail;
import gov.gtas.error.CommonErrorConstants;
import gov.gtas.error.ErrorHandlerFactory;
import gov.gtas.model.Document;
import gov.gtas.model.Flight;
import gov.gtas.model.Pax;
import gov.gtas.model.Traveler;
import gov.gtas.model.udr.Rule;
import gov.gtas.model.udr.RuleCond;
import gov.gtas.model.udr.UdrRule;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A builder pattern class for constructing a Drools rule "file" (actually a
 * text string) from one or more UDR objects.
 * This DRL string is then compiled into a Knowledge Base (KieBase object).
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

	private StringBuilder stringBuilder;
	private RuleConditionBuilder ruleConditionBuilder;

	public DrlRuleFileBuilder() {
		this.stringBuilder = new StringBuilder();
		this.ruleConditionBuilder = new RuleConditionBuilder();
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
					udrRule, rule);
		}
		return this;
	}

	public String build() {

		return this.stringBuilder.toString();
	}

	private void addPackageAndImport() {
		this.stringBuilder.append(RULE_PACKAGE_NAME).append(IMPORT_PREFIX)
				.append(RuleHitDetail.class.getName()).append(";")
				.append(NEW_LINE).append(IMPORT_PREFIX)
				.append(Traveler.class.getName()).append(";").append(NEW_LINE)
				.append(IMPORT_PREFIX).append(Pax.class.getName()).append(";")
				.append(NEW_LINE).append(IMPORT_PREFIX)
				.append(Flight.class.getName()).append(";").append(NEW_LINE)
				.append(IMPORT_PREFIX).append(Document.class.getName())
				.append(";").append(NEW_LINE).append("\n");
	}

	private void addRuleHeader(UdrRule parent, Rule rule) {
		this.stringBuilder.append("rule \"").append(parent.getTitle())
				.append(":").append(rule.getRuleIndex()).append("\"")
				.append(NEW_LINE).append("when\n");
	}

	// private void addRuleAction(UdrRule parent, Rule rule) {
	// this.stringBuilder
	// .append("then\n")
	// .append(String.format(ACTION_TRAVELER_HIT, parent.getId(),
	// rule.getRuleIndex())).append("end\n");
	// }
}
