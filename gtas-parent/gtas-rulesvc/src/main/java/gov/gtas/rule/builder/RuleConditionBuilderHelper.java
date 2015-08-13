package gov.gtas.rule.builder;

import static gov.gtas.rule.builder.RuleTemplateConstants.COMMA_CHAR;
import static gov.gtas.rule.builder.RuleTemplateConstants.DOUBLE_QUOTE_CHAR;
import static gov.gtas.rule.builder.RuleTemplateConstants.FALSE_STRING;
import static gov.gtas.rule.builder.RuleTemplateConstants.LEFT_PAREN_CHAR;
import static gov.gtas.rule.builder.RuleTemplateConstants.RIGHT_PAREN_CHAR;
import static gov.gtas.rule.builder.RuleTemplateConstants.SPACE_CHAR;
import static gov.gtas.rule.builder.RuleTemplateConstants.TRUE_STRING;
import gov.gtas.enumtype.TypeEnum;
import gov.gtas.model.udr.enumtype.OperatorCodeEnum;
import gov.gtas.model.udr.json.QueryTerm;
import gov.gtas.util.DateCalendarUtils;

import java.text.ParseException;
import java.util.Date;
/**
 * Utility class to construct
 * @author GTAS3 (AB)
 *
 */
public class RuleConditionBuilderHelper {

	private static String convertJsonStringVal(final TypeEnum type, final String value) throws ParseException{
		String ret = null;
     	switch(type){
    	case BOOLEAN:
    		if(value.charAt(0)=='Y'){
    		   ret = TRUE_STRING;
    		} else if (value.charAt(0)=='N'){
    			ret = FALSE_STRING;   		
    		} else if(TRUE_STRING.equalsIgnoreCase(value)){
    			ret = TRUE_STRING;
    		} else {
    			ret = FALSE_STRING;
    		}
    		break;
    	case STRING:
    		ret = DOUBLE_QUOTE_CHAR + value + DOUBLE_QUOTE_CHAR;
    		break;
   	    case DATE:
   	    	Date date = DateCalendarUtils.parseJsonDate(value);
   	    	ret = DOUBLE_QUOTE_CHAR + DateCalendarUtils.formatRuleEngineDate(date) + DOUBLE_QUOTE_CHAR;
   	    	break;
   	    case TIME:
   	    	date = DateCalendarUtils.parseJsonDate(value);
   	    	ret = DOUBLE_QUOTE_CHAR + DateCalendarUtils.formatRuleEngineDateTime(date) + DOUBLE_QUOTE_CHAR;
   	    	break;
   	    case DATETIME:
   	    	date = DateCalendarUtils.parseJsonDate(value);
   	    	ret = DOUBLE_QUOTE_CHAR + DateCalendarUtils.formatRuleEngineDateTime(date) + DOUBLE_QUOTE_CHAR;
   	    	break;
    	case INTEGER:
    	case LONG:
    	case DOUBLE:
    		ret = value;
    		break;
    	}
		return ret;
	}
    public static void addConditionValue(final TypeEnum type, final String val, final StringBuilder bldr) throws ParseException{
    	bldr.append(convertJsonStringVal(type, val));
    }
    public static void addConditionValues(final TypeEnum type, final String[] values, StringBuilder bldr) throws ParseException{
    	bldr.append(LEFT_PAREN_CHAR);
    	if(values != null && values.length > 0){
    		bldr.append(convertJsonStringVal(type, values[0]));
	    	for(int i = 1; i  < values.length; ++i){
	    		bldr.append(COMMA_CHAR).append(SPACE_CHAR)
	    		.append(convertJsonStringVal(type, values[i]));
	    	}
    	}
    	bldr.append(RIGHT_PAREN_CHAR);
    }
    
    public static void addConditionDescription(final QueryTerm cond, StringBuilder bldr) throws ParseException{    	
    	OperatorCodeEnum opCode = OperatorCodeEnum.getEnum(cond.getOperator());
    	TypeEnum attributeType = TypeEnum.getEnum(cond.getType());
    	
    	bldr.append(cond.getEntity()).append(SPACE_CHAR)
    	     .append(cond.getField()).append(SPACE_CHAR)
    	     .append(opCode.getDisplayName()).append(SPACE_CHAR);
    	
		String[] values = cond.getValue();
        if(values != null && values.length > 0){
			switch (opCode) {
			case IN:
			case NOT_IN:
			case BETWEEN:
			case NOT_BETWEEN:
		    	addConditionValues(attributeType, values, bldr);
		    	break;
			default:
				//single value
				addConditionValue(attributeType, values[0], bldr);
				break;
			}
        }
    }

}
