package gov.gtas.querybuilder.mappings;

import gov.gtas.querybuilder.enums.TypeEnum;

public enum HitsMapping implements IEntityMapping {

	HAS_HITS ("has_hits", "Has Hits", TypeEnum.STRING.getType()),
	HAS_LIST_RULE_HIT ("has_list_rule_hit", "Has List Rule Hit", TypeEnum.STRING.getType()),
	HAS_RULE_HIT ("has_rule_hit", "Has Rule Hit", TypeEnum.STRING.getType()),
	LIST_RULES ("master_list_id", "List Rules - Master List Id", TypeEnum.STRING.getType()),
	SUB_LIST_ID ("sub_list_id", "Sub List Id", TypeEnum.STRING.getType()),
	RULE_ID ("rule_id", "Rules - Rule Id", TypeEnum.STRING.getType());
	
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
