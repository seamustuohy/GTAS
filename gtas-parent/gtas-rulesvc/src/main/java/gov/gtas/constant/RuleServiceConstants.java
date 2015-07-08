package gov.gtas.constant;

/**
 * Constants used in the Rule Service module.
 * 
 * @author GTAS3 (AB)
 *
 */
public class RuleServiceConstants {
	// //////////////////////////////////////////////////////////////////////////////////////
	// KNOWLEDGE Management
	// //////////////////////////////////////////////////////////////////////////////////////
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
