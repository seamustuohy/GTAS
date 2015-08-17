package gov.gtas.rule.builder;

import gov.gtas.enumtype.EntityEnum;
import gov.gtas.enumtype.YesNoEnum;
import gov.gtas.model.udr.Rule;
//import gov.gtas.model.udr.RuleCond;
//import gov.gtas.model.udr.RuleCondPk;
import gov.gtas.model.udr.UdrRule;
import gov.gtas.model.udr.enumtype.OperatorCodeEnum;
import gov.gtas.model.udr.enumtype.ValueTypesEnum;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.querybuilder.mappings.DocumentMapping;
import gov.gtas.querybuilder.mappings.FlightMapping;
import gov.gtas.querybuilder.mappings.IEntityMapping;
import gov.gtas.svc.UdrServiceHelper;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class RuleBuilderTestUtils {
	public static final String UDR_RULE_TITLE="UDR_TEST_RULE";
	public static final long UDR_RULE_ID=33L;
	public static final long ENGINE_RULE_ID=21L;
	public static final int ENGINE_RULE_INDX1=1;
	public static final int ENGINE_RULE_INDX2=2;
	public static final int ENGINE_RULE_INDX3=3;
	public static final int ENGINE_RULE_INDX4=4;
	public static final int ENGINE_RULE_INDX5=5;
	
	public static UdrRule createSimpleUdrRule(int indx) throws ParseException{
		UdrRule ret = new UdrRule(UDR_RULE_ID, YesNoEnum.N, null, new Date());
		ret.setTitle(UDR_RULE_TITLE);
		Rule engineRule = createRule(ENGINE_RULE_ID, ret, indx);		
		ret.addEngineRule(engineRule);
		
		return ret;
	}
	/**
	 * Create a Rule condition object using common (query and criteria)
	 * enums.
	 * @param ent
	 * @param attr
	 * @param op
	 * @param value
	 * @param type
	 * @return
	 * @throws ParseException
	 */
	public static QueryTerm createQueryTerm(EntityEnum entity,
			IEntityMapping attr, OperatorCodeEnum op, String value,
			ValueTypesEnum type) throws ParseException {
		QueryTerm ret = new QueryTerm(entity.getEntityName(), attr.getFieldName(), type.getValue(), op.toString(), new String[]{value});
		return ret;
	}
	public static QueryTerm createQueryTerm(EntityEnum entity,
			IEntityMapping attr, OperatorCodeEnum op, String[] values,
			ValueTypesEnum type) throws ParseException {
		QueryTerm ret = new QueryTerm(entity.getEntityName(), attr.getFieldName(), type.getValue(), op.toString(), values);
		return ret;
	}

	//////////////////////////////////////////////////////
	//RULES
	/////////////////////////////////////////////////////
	private static Rule createRule(Long id, UdrRule parent, int indx) throws ParseException{
		Rule engineRule = null;
		List<QueryTerm> ruleMinTerm = new LinkedList<QueryTerm>();
		switch(indx){
			case ENGINE_RULE_INDX1:/* doc.iso2 != US && doc.issueDate > 2012-01-01 && flight# == 0012  */
				QueryTerm cond = createQueryTerm(EntityEnum.DOCUMENT,
						DocumentMapping.ISSUANCE_COUNTRY,
						OperatorCodeEnum.NOT_EQUAL, "US", ValueTypesEnum.STRING);
				ruleMinTerm.add(cond);
				cond = createQueryTerm(EntityEnum.DOCUMENT,
						DocumentMapping.ISSUANCE_DATE,
						OperatorCodeEnum.GREATER_OR_EQUAL, "2012-01-01", ValueTypesEnum.DATE);
				ruleMinTerm.add(cond);
				cond = createQueryTerm(EntityEnum.FLIGHT,
						FlightMapping.FLIGHT_NUMBER,
						OperatorCodeEnum.EQUAL, "0012", ValueTypesEnum.STRING);				
				ruleMinTerm.add(cond);
				engineRule = UdrServiceHelper.createEngineRule(ruleMinTerm, parent, indx);
				engineRule.setId(ENGINE_RULE_ID);
				break;
			case ENGINE_RULE_INDX2:/* doc.iso2 in (YE,GB) && flight.origin.iata == LHR && flight.carrier.iata==CO  */
				cond = createQueryTerm(EntityEnum.DOCUMENT,
						DocumentMapping.ISSUANCE_COUNTRY,
						OperatorCodeEnum.IN, new String[]{"YE", "GB"}, ValueTypesEnum.STRING);
				ruleMinTerm.add(cond);
				cond = createQueryTerm(EntityEnum.FLIGHT,
						FlightMapping.AIRPORT_ORIGIN,
						OperatorCodeEnum.EQUAL, "LHR", ValueTypesEnum.STRING);
				ruleMinTerm.add(cond);
				cond = createQueryTerm(EntityEnum.FLIGHT,
						FlightMapping.CARRIER,
						OperatorCodeEnum.EQUAL, "CO", ValueTypesEnum.STRING);
				ruleMinTerm.add(cond);
				engineRule = UdrServiceHelper.createEngineRule(ruleMinTerm, parent, indx);
				engineRule.setId(ENGINE_RULE_ID);
				break;
			case ENGINE_RULE_INDX3:/* flight.origin.iata == LHR && flight.carrier.iata==CO  */
				cond = createQueryTerm(EntityEnum.FLIGHT,
						FlightMapping.AIRPORT_ORIGIN,
						OperatorCodeEnum.EQUAL, "LHR", ValueTypesEnum.STRING);
				ruleMinTerm.add(cond);
				cond = createQueryTerm(EntityEnum.FLIGHT,
						FlightMapping.CARRIER,
						OperatorCodeEnum.EQUAL, "CO", ValueTypesEnum.STRING);
				ruleMinTerm.add(cond);
				cond = createQueryTerm(EntityEnum.FLIGHT,
						FlightMapping.FLIGHT_DATE,
						OperatorCodeEnum.GREATER, "2015-07-20 14:00:00", ValueTypesEnum.DATETIME);
				ruleMinTerm.add(cond);
				engineRule = UdrServiceHelper.createEngineRule(ruleMinTerm, parent, indx);
				engineRule.setId(ENGINE_RULE_ID);
				break;
			case ENGINE_RULE_INDX4:
			case ENGINE_RULE_INDX5:
				break;
		}
		return engineRule;
	}
}
