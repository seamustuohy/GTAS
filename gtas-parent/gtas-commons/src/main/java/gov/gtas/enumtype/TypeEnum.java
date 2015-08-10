package gov.gtas.enumtype;

public enum TypeEnum {
	STRING ("string"),
	INTEGER ("integer"),
	DOUBLE ("double"),
	DATE ("date"), 
	TIME ("time"),
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
