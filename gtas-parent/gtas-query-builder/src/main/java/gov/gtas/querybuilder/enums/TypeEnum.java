package gov.gtas.querybuilder.enums;

public enum TypeEnum {
	STRING ("string"),
	INTEGER ("integer"),
	DATE ("date"), 
	DATETIME ("datetime"),
	BOOLEAN ("boolean");
	
	private String type;

	private TypeEnum(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
}
