package gov.gtas.querybuilder.mappings;

import gov.gtas.enumtype.TypeEnum;

public enum HitsMapping implements IEntityMapping {

	HAS_RULE_HIT ("has_rule_hit", "Has Rule Hit", TypeEnum.BOOLEAN.getType()),
	HAS_WATCHLIST_HIT ("", "Has Watch List Hit", TypeEnum.BOOLEAN.getType()), // not yet available
	RULE_ID ("ruleId", "Rules Hit Id", TypeEnum.STRING.getType());
	
	private String fieldName;
	private String friendlyName;
	private String fieldType;
	private boolean displayField;
	
	private HitsMapping(String fieldName, String friendlyName, String fieldType, boolean displayField) {
		this.fieldName = fieldName;
		this.friendlyName = friendlyName;
		this.fieldType = fieldType;
		this.displayField = displayField;
	}
	private HitsMapping(String fieldName, String friendlyName, String fieldType) {
		this(fieldName, friendlyName, fieldType, true);
	}
	public String getFieldName() {
		return fieldName;
	}

	public String getFriendlyName() {
		return friendlyName;
	}

	public String getFieldType() {
		return fieldType;
	}
	
	/**
	 * @return the displayField
	 */
	public boolean isDisplayField() {
		return displayField;
	}
	
}
