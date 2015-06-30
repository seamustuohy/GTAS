package gov.gtas.model.udr.json;

import java.io.Serializable;

public class JsonUdrListElement implements Serializable {

	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = -4512984413526659992L;

	private long id;
	private MetaData summary;
	
	public JsonUdrListElement(long id, MetaData meta){
		this.id = id;
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
	
}
