package gov.gtas.rule.builder;

import gov.gtas.model.udr.CondValue;
import gov.gtas.model.udr.RuleCond;
import gov.gtas.model.udr.RuleCondPk;
import gov.gtas.model.udr.enumtype.EntityLookupEnum;
import gov.gtas.model.udr.enumtype.OperatorCodeEnum;
import gov.gtas.model.udr.enumtype.ValueTypesEnum;
import gov.gtas.util.DateCalendarUtils;

import java.text.ParseException;
import java.util.List;

public class RuleConditionBuilderHelper {
    public static void addConditionValue(final CondValue val, StringBuilder bldr){
     	switch(val.getValType()){
     	case OBJECT_REF:
     		bldr.append(val.getCharVal());
     		break;
    	case BOOLEAN:
    		bldr.append(val.getCharVal().charAt(0)=='Y'?"true":"false");
    		break;
    	case STRING:
    		bldr.append("\"").append(val.getCharVal()).append("\"");
    		break;
   	    case DATE:
   	    	bldr.append("\"").append(DateCalendarUtils.formatRuleEngineDate(val.getDtVal())).append("\"");
   	    	break;
   	    case TIMESTAMP:
   	    	bldr.append("\"").append(DateCalendarUtils.formatRuleEngineDate(val.getDtVal())).append("\"");
   	    	break;
    	case INTEGER:
    	case LONG:
    	case DOUBLE:
    		bldr.append(val.getNumVal());
    		break;
    	}
    }
    public static void addConditionValues(final RuleCond cond, StringBuilder bldr){
    	List<CondValue> values = cond.getValues();
    	bldr.append("(");
    	boolean firstTime = true;
    	for(CondValue val:values){
    		if(firstTime){
    			firstTime = false;
    		}else{
    			bldr.append(",");
    		}
    		addConditionValue(val,bldr);
    	}
    	bldr.append(")");
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

}
