package gov.gtas.constant;


/**
 * Constants used in the Rule Service module.
 * 
 * @author GTAS3 (AB)
 *
 */
public class RuleServiceConstants {
	public static final String DEFAULT_RULESET_NAME = "gtas.drl";
	/*
	 * All generated rules depend on this global object for returning results.
	 * When a knowledge session is created the global object should be created and associated with
	 * the session:
	 * ksession.setGlobal(RuleServiceConstants.RULE_RESULT_LIST_NAME, new ArrayList<Object>());
	 * The global can then be accessed after the rules are run by:
	 * (List<?>) ksession.getGlobal(RuleServiceConstants.RULE_RESULT_LIST_NAME);
	 */
	public static final String RULE_RESULT_LIST_NAME = "resultList";
	// //////////////////////////////////////////////////////////////////////////////////////
	// KNOWLEDGE Management
	// //////////////////////////////////////////////////////////////////////////////////////
	/* The Knowledge session name configured in META-INF/module.xml */
	public static final String KNOWLEDGE_SESSION_NAME = "GtasKS";
	
	/* The root path for the KieFileSystem files. */
	public static final String KIE_FILE_SYSTEM_ROOT = "src/main/resources/";
	// //////////////////////////////////////////////////////////////////////////////////////
	// ERROR CODES
	// //////////////////////////////////////////////////////////////////////////////////////
	/*
	 * This is the error code for an internal system error indicating invalid
	 * program logic causing the rule engine being called with a null
	 * argument.
	 */
	public static final String NULL_ARGUMENT_ERROR_CODE = "NULL_ARGUMENT";
	/*
	 * This is the error code for an internal system error indicating that the
	 * UDR generated rule could not be compiled.
	 */
	public static final String RULE_COMPILE_ERROR_CODE = "RULE_COMPILE_ERROR_CODE";

	public static final String INCOMPLETE_TREE_ERROR_CODE = "INCOMPLETE_TREE_ERROR_CODE";
	
	// //////////////////////////////////////////////////////////////////////////////////////
	// ERROR Messages
	// //////////////////////////////////////////////////////////////////////////////////////
	/*
	 * This is the error message for an internal system error indicating invalid
	 * program logic causing the rule engine being called with a null
	 * argument.
	 */
	public static final String NULL_ARGUMENT_ERROR_MESSAGE = "The parameter '%s' passed to the method '%s' should not be null.";
	/*
	 * This is the error message for an internal system error indicating that the
	 * UDR generated rule could not be compiled.
	 */
	public static final String RULE_COMPILE_ERROR_MESSAGE = "The rule file '%s' could not be compiled.";
	
	public static final String INCOMPLETE_TREE_ERROR_MESSAGE = "The query tree is incomplete at level %d.";
	
}
