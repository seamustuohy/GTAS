package gov.gtas.wrapper;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class Rule {

	@Expose
	private String logicalOpeator;
	@Expose
	private List<Condition> conditions = new ArrayList<Condition>();
	@Expose
	private Metadata metadata;

	/**
	 * 
	 * @return The logicalOpeator
	 */
	public String getLogicalOpeator() {
		return logicalOpeator;
	}

	/**
	 * 
	 * @param logicalOpeator
	 *            The logicalOpeator
	 */
	public void setLogicalOpeator(String logicalOpeator) {
		this.logicalOpeator = logicalOpeator;
	}

	/**
	 * 
	 * @return The conditions
	 */
	public List<Condition> getConditions() {
		return conditions;
	}

	/**
	 * 
	 * @param conditions
	 *            The conditions
	 */
	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}

	/**
	 * 
	 * @return The metadata
	 */
	public Metadata getMetadata() {
		return metadata;
	}

	/**
	 * 
	 * @param metadata
	 *            The metadata
	 */
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

}
