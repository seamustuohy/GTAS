package gov.gtas.querybuilder.mappings;

public interface IEntityMapping {
	
    public boolean isDisplayField();
    
	public String getFieldName();

	public String getFriendlyName();

	public String getFieldType();
}
