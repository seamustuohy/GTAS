package gov.gtas.model.watchlist.json;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * The base query condition term.
 * @author GTAS3 (AB)
 *
 */
public class WatchlistTerm implements Serializable{
	
	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = -1079730850782449251L;

	 private String entity;
     private String field;
     private String type;
     private String value;
     
     public WatchlistTerm(){   	 
     }
     public WatchlistTerm(String entity, String field, String type, String val){
    	 this.entity = entity;
    	 this.field = field;
    	 this.type = type;
    	 this.value = val;
     }
      	
	/**
	 * @return the entity
	 */
	public String getEntity() {
		return entity;
	}
	/**
	 * @param entity the entity to set
	 */
	public void setEntity(String entity) {
		this.entity = entity;
	}

	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}
	/**
	 * @param field the field to set
	 */
	public void setField(String field) {
		this.field = field;
	}
		
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
    /**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE); 
    } 
}
