package gov.gtas.rule.builder.util;

import static gov.gtas.rule.builder.RuleTemplateConstants.ADDRESS_VARIABLE_NAME;
import static gov.gtas.rule.builder.RuleTemplateConstants.CREDIT_CARD_VARIABLE_NAME;
import static gov.gtas.rule.builder.RuleTemplateConstants.DOCUMENT_VARIABLE_NAME;
import static gov.gtas.rule.builder.RuleTemplateConstants.EMAIL_VARIABLE_NAME;
import static gov.gtas.rule.builder.RuleTemplateConstants.FLIGHT_VARIABLE_NAME;
import static gov.gtas.rule.builder.RuleTemplateConstants.FREQUENT_FLYER_VARIABLE_NAME;
import static gov.gtas.rule.builder.RuleTemplateConstants.PASSENGER_VARIABLE_NAME;
import static gov.gtas.rule.builder.RuleTemplateConstants.PHONE_VARIABLE_NAME;
import static gov.gtas.rule.builder.RuleTemplateConstants.PNR_VARIABLE_NAME;
import static gov.gtas.rule.builder.RuleTemplateConstants.TRAVEL_AGENCY_VARIABLE_NAME;
import gov.gtas.enumtype.EntityEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Organizes variable names used in rules.
 * @author GTAS3
 *
 */
public class RuleVariablesUtil {
	/**
	 * Creates a map of rule variables to use when generating engine rules.
	 * 
	 * @return the rule variable map.
	 */
	public static Map<EntityEnum, String> createEngineRuleVariableMap2() {
		Map<EntityEnum, String> ret = new HashMap<EntityEnum, String>();
		ret.put(EntityEnum.PASSENGER, PASSENGER_VARIABLE_NAME);
		ret.put(EntityEnum.DOCUMENT, DOCUMENT_VARIABLE_NAME);
		ret.put(EntityEnum.FLIGHT, FLIGHT_VARIABLE_NAME);
		ret.put(EntityEnum.PNR, PNR_VARIABLE_NAME);
		ret.put(EntityEnum.ADDRESS, ADDRESS_VARIABLE_NAME);
		ret.put(EntityEnum.PHONE, PHONE_VARIABLE_NAME);
		ret.put(EntityEnum.EMAIL, EMAIL_VARIABLE_NAME);
		ret.put(EntityEnum.FREQUENT_FLYER, FREQUENT_FLYER_VARIABLE_NAME);
		ret.put(EntityEnum.TRAVEL_AGENCY, TRAVEL_AGENCY_VARIABLE_NAME);
		ret.put(EntityEnum.CREDIT_CARD, CREDIT_CARD_VARIABLE_NAME);

		return ret;
	}

}
