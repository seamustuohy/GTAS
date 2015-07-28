package gov.gtas.querybuilder.mappings;

import gov.gtas.querybuilder.enums.TypeEnum;

public enum DocumentMapping implements IEntityMapping {

	ISSUANCE_OR_CITIZENSHIP_COUNTRY ("issuanceCountry.iso2", "Issuance Country", TypeEnum.STRING.getType()),
	EXPIRATION_DATE ("expirationDate", "Expiration Date", TypeEnum.DATETIME.getType()),
	DOCUMENT_NUMBER ("documentNumber", "Number", TypeEnum.STRING.getType()),
	DOCUMENT_TYPE ("class", "Type", TypeEnum.STRING.getType());
	
	private String fieldName;
	private String friendlyName;
	private String fieldType;
	
	private DocumentMapping(String fieldName, String friendlyName, String fieldType) {
		this.fieldName = fieldName;
		this.friendlyName = friendlyName;
		this.fieldType = fieldType;
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
	
}
