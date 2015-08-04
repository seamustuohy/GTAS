package gov.gtas.querybuilder.mappings;

import gov.gtas.querybuilder.enums.TypeEnum;

public enum DocumentMapping implements IEntityMapping {

	ISSUANCE_OR_CITIZENSHIP_COUNTRY ("issuanceCountry.iso2", "Issuance Country", TypeEnum.STRING.getType()),
	EXPIRATION_DATE ("expirationDate", "Expiration Date", TypeEnum.DATETIME.getType()),
	ISSUANCE_DATE ("issuanceDate", "Issuance Date", TypeEnum.DATETIME.getType()),
	DOCUMENT_NUMBER ("documentNumber", "Number", TypeEnum.STRING.getType()),
	DOCUMENT_TYPE ("class", "Type", TypeEnum.STRING.getType()),
	DOCUMENT_OWNER_ID ("traveler.id", "Owner Id", TypeEnum.STRING.getType());
	
	private String fieldName;
	private String friendlyName;
	private String fieldType;
	private boolean displayField;
	
	private DocumentMapping(String fieldName, String friendlyName, String fieldType, boolean displayField) {
		this.fieldName = fieldName;
		this.friendlyName = friendlyName;
		this.fieldType = fieldType;
		this.displayField = displayField;
	}
	private DocumentMapping(String fieldName, String friendlyName, String fieldType) {
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
