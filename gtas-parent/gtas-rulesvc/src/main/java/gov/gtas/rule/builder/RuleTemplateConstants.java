package gov.gtas.rule.builder;

import java.util.HashSet;
import java.util.Set;

import gov.gtas.constant.RuleServiceConstants;

public class RuleTemplateConstants {
	private RuleTemplateConstants() {
		// to prevent instantiation.
	}
	public static final Set<String> YES_SET;
	static{
	    YES_SET = new HashSet<String>();
	    for(String member: new String[]{"Y", "y", "Yes", "YES"}){
	    	YES_SET.add(member);
	    }
		
	}
	public static final char DOUBLE_QUOTE_CHAR = '"';
	public static final char SINGLE_QUOTE_CHAR = '\'';
	public static final char LEFT_PAREN_CHAR = '(';
	public static final char RIGHT_PAREN_CHAR = ')';
	public static final char SPACE_CHAR = ' ';
	public static final char COMMA_CHAR = ',';

	public static final String NEW_LINE = "\n";
	public static final String TRUE_STRING = "true";
	public static final String FALSE_STRING = "false";
		
	public static final String RULE_PACKAGE_NAME = "package gov.gtas.rule;\n";
	public static final String IMPORT_PREFIX = "import ";
	public static final String GLOBAL_RESULT_DECLARATION = "global java.util.List "+RuleServiceConstants.RULE_RESULT_LIST_NAME+";\n\n";
}
