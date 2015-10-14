package gov.gtas.model.udr.json;

import gov.gtas.util.DateCalendarUtils;

import java.io.Serializable;
import java.util.Date;
/**
 * Class representing summary listing element for UDR.
 * @author GTAS3
 *
 */
public class JsonUdrListElement implements Serializable {

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = -4512984413526659992L;

	private long id;
	private String modifiedBy;
	private String modifiedOn;
	private MetaData summary;
	
	public JsonUdrListElement(long id, String modifiedBy, Date modifiedOn, MetaData meta){
		this.id = id;
		this.modifiedBy = modifiedBy;
		this.modifiedOn = DateCalendarUtils.formatJsonDate(modifiedOn);
		this.summary = meta;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the summary
	 */
	public MetaData getSummary() {
		return summary;
	}

	/**
	 * @return the modifiedBy
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @return the modifiedOn
	 */
	public String getModifiedOn() {
		return modifiedOn;
	}
	
}
