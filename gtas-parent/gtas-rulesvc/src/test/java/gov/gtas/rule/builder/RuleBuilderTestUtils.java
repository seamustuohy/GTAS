package gov.gtas.rule.builder;

import gov.gtas.model.udr.EntityAttributeConstants;
import gov.gtas.model.udr.Rule;
import gov.gtas.model.udr.RuleCond;
import gov.gtas.model.udr.RuleCondPk;
import gov.gtas.model.udr.UdrRule;
import gov.gtas.model.udr.enumtype.EntityLookupEnum;
import gov.gtas.model.udr.enumtype.OperatorCodeEnum;
import gov.gtas.model.udr.enumtype.ValueTypesEnum;
import gov.gtas.model.udr.enumtype.YesNoEnum;

import java.text.ParseException;
import java.util.Date;

public class RuleBuilderTestUtils {
	public static final String UDR_RULE_TITLE="UDR_TEST_RULE";
	public static final long UDR_RULE_ID=33L;
	public static final int ENGINE_RULE_INDX1=1;
	public static final int ENGINE_RULE_INDX2=2;
	public static final int ENGINE_RULE_INDX3=3;
	public static final int ENGINE_RULE_INDX4=4;
	public static final int ENGINE_RULE_INDX5=5;
	
	public static UdrRule createSimpleUdrRule(int indx) throws ParseException{
		UdrRule ret = new UdrRule(UDR_RULE_ID, YesNoEnum.N, null, new Date());
		ret.setTitle(UDR_RULE_TITLE);
		Rule engineRule = createRule(ret, indx);		
		ret.addEngineRule(engineRule);
		
		return ret;
	}
	public static RuleCond createRuleCondition(EntityLookupEnum entity,
			String attribute, OperatorCodeEnum op, String value,
			ValueTypesEnum type) throws ParseException {
		RuleCondPk pk = new RuleCondPk(1L, 1);
		RuleCond ret = new RuleCond(pk, entity, attribute, op);
		ret.addValueToCondition(value, type);
		return ret;
	}
	public static RuleCond createRuleCondition(EntityLookupEnum entity,
			String attribute, OperatorCodeEnum op, String[] values,
			ValueTypesEnum type) throws ParseException {
		RuleCondPk pk = new RuleCondPk(1L, 1);
		RuleCond ret = new RuleCond(pk, entity, attribute, op);
		for(String value:values){
		   ret.addValueToCondition(value, type);
		}
		return ret;
	}

	//////////////////////////////////////////////////////
	//RULES
	/////////////////////////////////////////////////////
	private static Rule createRule(UdrRule parent, int indx) throws ParseException{
		Rule engineRule = new Rule(parent,indx,null);
		switch(indx){
			case 1:/* doc.iso2 != US && doc.issueDate > 2012-01-01 && flight# == 0012  */
				RuleCond cond = createRuleCondition(EntityLookupEnum.Document,
						EntityAttributeConstants.DOCUMENT_ATTR_ISO2,
						OperatorCodeEnum.NOT_EQUAL, "US", ValueTypesEnum.STRING);
				engineRule.addConditionToRule(cond);
				cond = createRuleCondition(EntityLookupEnum.Document,
						EntityAttributeConstants.DOCUMENT_ATTR_ISSUANCE_DATE,
						OperatorCodeEnum.GREATER_OR_EQUAL, "2012-01-01", ValueTypesEnum.DATE);
				engineRule.addConditionToRule(cond);
				cond = createRuleCondition(EntityLookupEnum.Flight,
						EntityAttributeConstants.FLIGHT_ATTR_FLIGHT_NUMBER,
						OperatorCodeEnum.EQUAL, "0012", ValueTypesEnum.STRING);
				engineRule.addConditionToRule(cond);
				break;
			case 2:/* doc.iso2 in (YE,GB) && flight.origin.iata == LHR && flight.carrier.iata==CO  */
				cond = createRuleCondition(EntityLookupEnum.Document,
						EntityAttributeConstants.DOCUMENT_ATTR_ISO2,
						OperatorCodeEnum.IN, new String[]{"YE", "GB"}, ValueTypesEnum.STRING);
				engineRule.addConditionToRule(cond);
				cond = createRuleCondition(EntityLookupEnum.Flight,
						EntityAttributeConstants.FLIGHT_ATTR_ORIGIN_IATA,
						OperatorCodeEnum.EQUAL, "LHR", ValueTypesEnum.STRING);
				engineRule.addConditionToRule(cond);
				cond = createRuleCondition(EntityLookupEnum.Flight,
						EntityAttributeConstants.FLIGHT_ATTR_CARRIER_IATA,
						OperatorCodeEnum.EQUAL, "CO", ValueTypesEnum.STRING);
				break;
			case 3:
			case 4:
			case 5:
		}
		return engineRule;
	}
}
