package gov.gtas.error.udr;

/**
 * Constants used in the Rule Service module.
 * 
 * @author GTAS3 (AB)
 *
 */
public class UdrErrorConstants {
	// //////////////////////////////////////////////////////////////////////////////////////
	// ERROR CODES
	// //////////////////////////////////////////////////////////////////////////////////////
	/*
	 * The Input JSON Query Specification from UI is missing the meta data summary.
	 */
	public static final String NO_META_ERROR_CODE = "NO_META";
	/*
	 * The Input JSON Query Specification from UI is missing the rule title in the meta data summary.
	 */
	public static final String NO_TITLE_ERROR_CODE = "NO_TITLE";

	/*
	 * The Input JSON Query Specification from UI is missing the start date in the meta data summary
	 * or the start date is invalid.
	 */
	public static final String INVALID_START_DATE_ERROR_CODE = "INVALID_START_DATE";

	/*
	 * The Input JSON Query Specification from UI has an invalid rule detail structure.
	 * (i.e., it is not a valid boolean tree structure.)
	 */
	public static final String INVALID_RULE_STRUCTURE_ERROR_CODE = "INVALID_RULE_STRUCTURE";
	
	// //////////////////////////////////////////////////////////////////////////////////////
	// ERROR Messages
	// //////////////////////////////////////////////////////////////////////////////////////
	/*
	 * The Input JSON Query Specification from UI is missing the meta data summary.
	 */
	public static final String NO_META_ERROR_MESSAGE = "The JSON UDR specification is missing the summary meta data object.";
	/*
	 * The Input JSON Query Specification from UI is missing the rule title in the meta data summary.
	 */
	public static final String NO_TITLE_ERROR_MESSAGE = "The JSON UDR specification is missing a title field in the summary.";

	/*
	 * The Input JSON Query Specification from UI is missing the start date in the meta data summary
	 * or the start date is invalid.
	 */
	public static final String INVALID_START_DATE_ERROR_MESSAGE = "The JSON UDR specification is missing the start date in the summary.";

	/*
	 * The Input JSON Query Specification from UI has an invalid rule detail structure.
	 * (i.e., it is not a valid boolean tree structure.)
	 */
	public static final String INVALID_RULE_STRUCTURE_ERROR_MESSAGE = "The JSON UDR specification has an invalid rule details structure.";
	
}
