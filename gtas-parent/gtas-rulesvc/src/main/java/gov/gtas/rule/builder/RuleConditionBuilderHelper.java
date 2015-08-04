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
/**
 * Utility class to construct
 * @author GTAS3 (AB)
 *
 */
public class RuleConditionBuilderHelper {
	private static final char SPACE_CHAR = ' ';
	
	private static String convertCondValToString(final CondValue val){
		String ret = null;
     	switch(val.getValType()){
     	case OBJECT_REF:
     		ret = val.getCharVal();
     		break;
    	case BOOLEAN:
    		ret = val.getCharVal().charAt(0)=='Y'?"true":"false";
    		break;
    	case STRING:
    		ret = "\"" + val.getCharVal() + "\"";
    		break;
   	    case DATE:
   	    	ret = "\"" + DateCalendarUtils.formatRuleEngineDate(val.getDtVal()) + "\"";
   	    	break;
   	    case DATETIME:
   	    	ret = "\"" + DateCalendarUtils.formatRuleEngineDateTime(val.getDtVal()) + "\"";
   	    	break;
    	case INTEGER:
    	case LONG:
    	case DOUBLE:
    		ret = val.getNumVal().toString();
    		break;
    	}
		return ret;
	}
    public static void addConditionValue(final CondValue val, StringBuilder bldr){
    	bldr.append(convertCondValToString(val));
    }
    public static void addConditionValues(final RuleCond cond, StringBuilder bldr){
    	List<CondValue> values = cond.getValues();
    	bldr.append('(');
    	boolean firstTime = true;
    	for(CondValue val:values){
    		if(firstTime){
    			firstTime = false;
    		}else{
    			bldr.append(',');
    		}
    		addConditionValue(val,bldr);
    	}
    	bldr.append(')');
    }
    public static String createConditionDescription(final RuleCond cond){
    	StringBuilder bldr = new StringBuilder();
    	bldr.append(cond.getEntityName()).append(SPACE_CHAR)
    	     .append(cond.getAttrName()).append(SPACE_CHAR)
    	     .append(cond.getOpCode().getDisplayName()).append(SPACE_CHAR);
    	List<CondValue> valList = cond.getValues();
    	if( valList != null && valList.size() > 1){
    		bldr.append(" [");
    		boolean firstTime = true;
    		for(CondValue val: valList){
    			if(!firstTime){
    				bldr.append(", ");
    			}
    			bldr.append(convertCondValToString(val));
    		}
    		bldr.append(']');
    	} else if(valList != null && valList.size() == 1){
    	     bldr.append(convertCondValToString(valList.get(0)));
    	}
    	return bldr.toString();
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

	public static String getSingleStringValue(final RuleCond cond){
		if(cond != null && cond.getValues() != null && cond.getValues().size() == 1){
			return cond.getValues().get(0).getCharVal();
		} else {
			return null;
		}
	}
}
