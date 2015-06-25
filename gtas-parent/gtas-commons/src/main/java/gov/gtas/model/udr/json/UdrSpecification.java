package gov.gtas.model.udr.json;

import gov.gtas.model.udr.json.QueryObject;

import java.io.Serializable;
/**
 * JSON object format for communicating with the UI.
 * @author GTAS3 (AB)
 *
 */
public class UdrSpecification implements Serializable {
	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = -8100474892751630855L;

	private QueryObject details;
	private MetaData summary;
	
	/**
	 * Default constructor for Spring to use.
	 */
	public UdrSpecification(){
		
	}
	public UdrSpecification(QueryObject queryObject, MetaData meta){
		this.details = queryObject;
		this.summary = meta;
	}

	/**
	 * @return the details
	 */
	public QueryObject getDetails() {
		return details;
	}

	/**
	 * @param details the details to set
	 */
	public void setDetails(QueryObject details) {
		this.details = details;
	}

	/**
	 * @return the summary
	 */
	public MetaData getSummary() {
		return summary;
	}

	/**
	 * @param summary the summary to set
	 */
	public void setSummary(MetaData summary) {
		this.summary = summary;
	}

}
