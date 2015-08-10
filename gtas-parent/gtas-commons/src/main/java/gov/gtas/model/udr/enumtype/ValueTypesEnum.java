package gov.gtas.model.udr.enumtype;
/**
 * Enumeration of JSON value types.
 * @author GTAS3 (AB)
 *
 */
public enum ValueTypesEnum {
  INTEGER("integer"),
  LONG("long"),
  DOUBLE("double"),
  STRING("string"),
  DATE("date"),
  DATETIME("datetime"),
  BOOLEAN("boolean");
  
  private String value;
  /**
   * Constructor.
   * @param extval
   */
  private ValueTypesEnum(String extval){
	  this.value = extval;
  }
  
  /**
   * @return the value.
   */
	public String getValue() {
		return value;
	}

    public static ValueTypesEnum convertFromExternalValue(String value){
        for(ValueTypesEnum v : values())
            if(v.getValue().equalsIgnoreCase(value)) return v;
        throw new IllegalArgumentException();
    }

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return getValue();
	}
    
}
