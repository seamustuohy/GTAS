package gov.gtas.model.udr.json;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
/**
 * JSON object representing a single or multiple values.
 * @author GTAS3 (AB)
 *
 */
public class ValueObject implements Serializable {

	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = 5773375191445696668L;

	private String type;
	private String[] values;
	
	@JsonCreator
	public ValueObject(@JsonProperty("type") final String type, @JsonProperty("values") final String[] values){
		this.type = ValueTypesEnum.valueOf(type).toString();//validate type
		this.values = values;
	}

	public ValueObject(final int value){
		this.type = ValueTypesEnum.Integer.toString();
		values = new String[]{String.valueOf(value)};
	}
	public ValueObject(final long value){
		this.type = ValueTypesEnum.Long.toString();
		values = new String[]{String.valueOf(value)};
	}
	public ValueObject(final double value){
		this.type = ValueTypesEnum.Double.toString();
		values = new String[]{String.valueOf(value)};
	}
	public ValueObject(final boolean value){
		this.type = ValueTypesEnum.Boolean.toString();
		values = new String[]{String.valueOf(value)};
	}
	public ValueObject(final Date value){
		this.type = ValueTypesEnum.Date.toString();
		values = new String[]{String.valueOf(value)};
	}
	public ValueObject(final java.sql.Date value){
		this.type = ValueTypesEnum.Date.toString();
		values = new String[]{String.valueOf(value)};
	}
	public ValueObject(final String value){
		this.type = ValueTypesEnum.String.toString();
		values = new String[]{value};
	}
	public ValueObject(final Timestamp value){
		this.type = ValueTypesEnum.Timestamp.toString();
		values = new String[]{String.valueOf(value)};
	}
	public ValueObject(final ValueTypesEnum type, final Collection<? extends Serializable> values){
		this.type = type.toString();
		if(values != null){
			int arrLength = values.size();		
		    this.values = new String[arrLength];
		    int i  =0;
		    for(Serializable ser:values){
		    	this.values[i++] = ser.toString();
		    }
		}
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
	 * @return the values
	 */
	public String[] getValues() {
		return values;
	}
	/**
	 * @param values the values to set
	 */
	public void setValues(String[] values) {
		this.values = values;
	}
	
}
