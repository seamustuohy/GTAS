package gov.gtas.model.udr.enumtype;

public enum OperatorCodeEnum {
  EQUAL("==",false),
  NOT_EQUAL("!=",false),
  GREATER(">",false),
  LESS("<",false),
  GREATER_OR_EQUAL(">=",false),
  LESS_OR_EQUAL("<=",false),
  IN("in",true),
  NOT_IN("not in",true),
  BETWEEN("", true),
  NOT_BETWEEN("",true),
  BEGINS_WITH("str[startsWith]", false),
  NOT_BEGINS_WITH("not str[startsWith]",false),
  CONTAINS("contains",false),
  NOT_CONTAINS("not contains", false),
  ENDS_WITH("str[endsWith]",false),
  NOT_ENDS_WITH("not str[endsWith]",false),
  IS_EMPTY("== null",false),
  IS_NOT_EMPTY("!= null", false),
  IS_NULL("== null", false),
  IS_NOT_NULL("!= null", false),
  MEMBER_OF("memberOf", true),
  NOT_MEMBER_OF("not memberOf",true);
  
  private final String operatorString;
  private final boolean takesMultipleArguements;
  
  /**
 * @return the operatorString
 */
public String getOperatorString() {
	return operatorString;
}

/**
 * @return the takesMultipleArguements
 */
public boolean isTakesMultipleArguements() {
	return takesMultipleArguements;
}

private OperatorCodeEnum(final String opString, boolean isMultivalued){
	  this.operatorString = opString;
	  this.takesMultipleArguements = isMultivalued;
  }
  
}
