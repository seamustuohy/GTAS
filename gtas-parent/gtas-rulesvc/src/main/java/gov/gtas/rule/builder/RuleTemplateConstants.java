package gov.gtas.rule.builder;

public class RuleTemplateConstants {
	private RuleTemplateConstants() {
		// to prevent instantiation.
	}
	
	public static final String RULE_PACKAGE_NAME = "package gov.gtas.rule;\n";
	public static final String IMPORT_PREFIX = "import ";
	public static final String NEW_LINE = "\n";
	public static final String GLOBAL_RESULT_DECLARATION = "global java.util.List resultList;\n";
	public static final String ACTION_PASSENGER_HIT = "resultList.add(new RuleHitDetail(%dL, $p.getId());\n";
}
